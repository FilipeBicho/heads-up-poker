package com.example.poker.bot

import com.example.poker.BIG_BLIND
import com.example.poker.POT
import com.example.poker.cards.BOT
import com.example.poker.cards.Card
import com.example.poker.cards.Dealer
import com.example.poker.cards.PLAYER
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.odds
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.tableCards
import com.example.poker.game.Data.totalPotValue
import com.example.poker.hand.FLUSH
import com.example.poker.hand.FOUR_OF_A_KIND
import com.example.poker.hand.FULL_HOUSE
import com.example.poker.hand.Hand
import com.example.poker.hand.PAIR
import com.example.poker.hand.RESULT
import com.example.poker.hand.ROYAL_STRAIGHT_FLUSH
import com.example.poker.hand.STRAIGHT
import com.example.poker.hand.STRAIGHT_FLUSH
import com.example.poker.hand.THREE_OF_A_KIND
import com.example.poker.hand.TWO_PAIR

class FlopDecisionMaking: Bot() {

    private var decision: Int = -1

    private lateinit var communityCards: List<Card>
    private lateinit var combinedCards:  List<Card>

    private lateinit var botOdds: Array<Int>
    private lateinit var opponentOdds: Array<Int>
    private lateinit var botHand: Hand

    private var isWetBoard: Boolean = false
    private var hasStraightDraw: Boolean = false
    private var hasFlushDraw: Boolean = false

    private var potOdds: Double = 0.0
    private var currentPot = pokerChips[POT]

    init {
        initValues()
        decision = calculateDecision()
    }

    override fun initValues() {
        super.initValues()
        communityCards = tableCards.subList(0, 3)
        combinedCards = (botCards + tableCards.subList(0, 3))
            .sortedBy { it.rank }
            .toMutableList()

        botOdds = odds.getFlopOdds()
        opponentOdds = odds.getOpponentFlopOdds()
        botHand = Hand(botCards, communityCards)

        isWetBoard = isWetBoard()
        hasStraightDraw = hasStraightDraw(combinedCards)
        hasFlushDraw = hasFlushDraw(combinedCards)

        potOdds = if (callValue == 0) 1.0 else (callValue / (totalPotValue + callValue)).toDouble()
    }

    private fun isWetBoard(): Boolean {
        val suits = communityCards.map { it.suit }
        val ranks = communityCards.map { it.rank }.sorted()

        val ranksCount = ranks.groupingBy { it }.eachCount()

        // three of a kind
        if (ranksCount.values.any { it == 3}) {
            hasWetBoardThreeOfAKind = true
            return true
        }

        // pair
        if (ranksCount.values.any { it == 2}) {
            hasWetBoardPair = true
            return true
        }

        // flush (2 or more cards)
        val suitCounts = suits.groupingBy { it }.eachCount()
        if (suitCounts.values.any { it >= 2 }) {
            hasWetBoardFlush = true
            return true
        }

        // straight (2 or more consecutive cards)
        for (i in 0 until ranks.size - 2) {
            if (ranks[i + 2] - ranks[i] <= 2) {
                hasWetBoardStraight = true
                return true
            }
        }

        return false
    }

    private fun monsterHand(): Int {
        return when {
            totalPotValue > 400 || currentPot > 200 -> allIn()
            botStack > 20 && playerStack > 20 ->
                if (callValue > 0)
                    betBlinds((bet[PLAYER] * 5) / BIG_BLIND)
                else
                    betBlinds(10)
            else -> allIn()
        }
    }

    private fun fullHouse() {
        when {
            botOdds[RESULT] > 90 -> if (totalPotValue > 10) allIn() else betBlinds(10)

        }
    }

    private fun calculateDecision(): Int {

        return when (botHand.resultValue) {
            ROYAL_STRAIGHT_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND -> monsterHand()
            else -> {
                if (botOdds[RESULT] > 90) {
                    monsterHand()
                } else {
                    FOLD
                }
            }
        }
    }

    fun getDecision() = decision

}