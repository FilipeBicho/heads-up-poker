package com.example.poker.bot

import com.example.poker.BIG_BLIND
import com.example.poker.cards.ACE
import com.example.poker.cards.BOT
import com.example.poker.cards.Card
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.botValidActions
import com.example.poker.game.Data.dealer
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import kotlin.math.abs

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3
const val RAISE = 4
const val ALLIN = 5

abstract class Bot {

    var action: Int = 0
    var betValue: Int = 0

    protected var playerStack: Int = 0
    protected var botStack: Int = 0

    protected var callValue: Int = 0
    protected var pot: Int = 0

    protected var isDealer: Boolean = false
    protected var hasHandPair = false
    protected var hasOpenEndStraight: Boolean = false
    protected var hasInsideStraight: Boolean = false
    protected var hasWetBoardFlush: Boolean = false
    protected var hasWetBoardStraight: Boolean = false
    protected var hasWetBoardThreeOfAKind: Boolean = false
    protected var hasWetBoardPair: Boolean = false

    protected open fun initValues() {
        isDealer = BOT == dealer
        playerStack = if (pokerChips[PLAYER] > 0) pokerChips[PLAYER]/BIG_BLIND else 0
        botStack = if (pokerChips[BOT] > 0) pokerChips[BOT]/BIG_BLIND else 0
        callValue = if (playerStack > 0 && botStack > 0) abs(bet[BOT] - bet[PLAYER]) else 0
        hasHandPair = botCards.first().rank == botCards.last().rank
    }

    protected fun allIn(): Int {
        return if (pokerChips[PLAYER] == 0) CALL else ALLIN
    }

    protected fun raiseBetByMultiplier(multiplier: Int): Int {

        if (pokerChips[PLAYER] == 0)
            return CALL

        betValue = multiplier * bet[PLAYER]

        if (betValue >= pokerChips[PLAYER])
            betValue = pokerChips[PLAYER]
        else if (betValue >= pokerChips[BOT])
            betValue = pokerChips[BOT]

        return RAISE
    }

    protected fun betBlinds(blinds: Int): Int {

        if (pokerChips[PLAYER] == 0) {
            return CALL
        }

        betValue = if (blinds * BIG_BLIND >= pokerChips[BOT]) {
            pokerChips[BOT]
        } else {
            bet[PLAYER] + blinds * BIG_BLIND
        }

        return if (bet[PLAYER] > 0) RAISE else BET
    }

    /**
     * has flush draw if has 4 cards of the same suit
     */
    protected fun hasFlushDraw(combinedCards: List<Card>): Boolean {
        val suitCount = combinedCards.groupingBy { it.suit }.eachCount()
        return suitCount.values.any { it == 4}
    }

    protected fun hasStraightDraw(combinedCards: List<Card>): Boolean {
        val sortedValues = combinedCards.map { it.rank }.distinct().sorted().toMutableList()

        if (sortedValues.any { it == ACE }) {
            sortedValues.add(13) // Simulate ACE in the end
        }

        for (i in 0 until sortedValues.size - 3) {
            val subList = sortedValues.subList(i, i + 4)

            // Check for open-ended straight draw (consecutive numbers)
            if (subList.first() == subList.last() - 3) {
                hasOpenEndStraight = true
                return true
            }

            // Check for inside straight draw (gap of one number)
            if (subList[3] - subList[0] == 4 && (subList[1] - subList[0] > 1 || subList[3] - subList[2] > 1)) {
                hasInsideStraight = true
                return true
            }
        }

        return false
    }

    abstract fun calculateAction(): Int
}