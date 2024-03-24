package com.example.poker

import com.example.poker.bot.PreFlopBot
import kotlin.math.abs

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3

open class Bot(odds: Odds?, private val cards: List<Card>, tableCards: List<Card>?, isDealer: Boolean) {

   
    private var action: Int = 0
    var betValue: Int = 0
    protected var playerStack: Int = 0
    protected var botStack: Int = 0
    protected var callValue: Int = 0
    protected var totalMoney: Int = 0
    protected var pot: Int = 0
    protected var isDealer: Boolean = false
    protected val hasHandPair = cards.first().rank == cards.last().rank

    init {
        this.isDealer = isDealer
    }

    /**
     * Init stack and call value
     */
    fun initValues(pokerChips: IntArray, bet:IntArray, pot: Int) {

        totalMoney = pokerChips[BOT]

        if (pokerChips[PLAYER] > 0) {
            playerStack = pokerChips[PLAYER]/BIG_BLIND
        }

        if (totalMoney > 0) {
            botStack = pokerChips[BOT]/ BIG_BLIND
        }

        if (playerStack > 0 && botStack > 0) {
            callValue = abs(bet[BOT] - bet[PLAYER])
        }
    }

    /**
     * Reset stack and call value
     */
    fun resetValues() {
        playerStack = 0
        botStack = 0
        callValue = 0
        totalMoney = 0
    }

    protected fun allIn(): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = totalMoney
            BET
        }
    }

    protected fun betBlinds(blinds: Int): Int {
        betValue = if (blinds * BIG_BLIND >= totalMoney) {
            totalMoney
        } else {
            blinds * BIG_BLIND
        }

        return BET
    }


    open fun botAction(pokerChips: IntArray, bet: IntArray, totalPot: Int, round: Int, validActions: BooleanArray): Int {

        resetValues()
        initValues(pokerChips, bet, totalPot)

        when (round) {
            PRE_FLOP -> return PreFlopBot(cards, isDealer).botAction(pokerChips, bet, totalPot, validActions)
        }

        return action
    }
}