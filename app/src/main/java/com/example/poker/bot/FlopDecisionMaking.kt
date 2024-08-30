package com.example.poker.bot

import com.example.poker.cards.ACE
import com.example.poker.cards.Card
import com.example.poker.cards.JACK
import com.example.poker.cards.KING
import com.example.poker.cards.TEN
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.odds
import com.example.poker.game.Data.tableCards
import com.example.poker.hand.Hand
import com.example.poker.hand.STRAIGHT

class FlopDecisionMaking: Bot() {

    private var decision: Int = -1
    private var communityCards: List<Card> = tableCards.subList(0, 3)
    private var combinedCards = (botCards + tableCards.subList(0, 3))
        .sortedBy { it.rank }
        .toMutableList()
    private lateinit var suitCount: Map<Int, Int>
    private var isWetBoard: Boolean = false

    init {
        initValues()
        calculateDecision()
    }

    override fun initValues() {
        super.initValues()
        isWetBoard = isWetBoard()
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


    private fun calculateDecision(): Int {
        val botOdds = odds.getFlopOdds()
        val opponentOdds = odds.getOpponentFlopOdds()
        val botHand = Hand(botCards, communityCards)

        return 0

    }

    fun getDecision() = decision

}