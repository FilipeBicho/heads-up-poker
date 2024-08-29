package com.example.poker.bot

import com.example.poker.cards.ACE
import com.example.poker.cards.Card
import com.example.poker.cards.JACK
import com.example.poker.cards.KING
import com.example.poker.cards.TEN
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.odds
import com.example.poker.game.Data.tableCards

class FlopDecisionMaking: Bot() {

    private var decision: Int = -1
    private var communityCards: List<Card> = tableCards.subList(0, 3)
    private var combinedCards = (botCards + tableCards.subList(0, 3))
        .sortedBy { it.rank }
        .toMutableList()
    private lateinit var suitCount: Map<Int, Int>
    private var hasFlushDraw: Boolean = false
    private var hasStraightDraw: Boolean = false
    private var hasOpenEndStraight: Boolean = false
    private var hasInsideStraight: Boolean = false

    init {
        initValues()
        calculateDecision()
    }

    /**
     * has flush draw if has 4 cards of the same suit
     */
    private fun hasFlushDraw(): Boolean {
        suitCount = combinedCards.groupingBy { it.suit }.eachCount()
        return suitCount.values.any { it == 4}
    }

    /**
     * TODO check straight draw with ACE
     */
    private fun hasStraightDraw(): Boolean {
        val sortedValues = combinedCards.map { it.rank }.distinct().sorted().toMutableList()

        if (sortedValues.any { it == ACE}) {
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

    override fun initValues() {
        hasFlushDraw = hasFlushDraw()
        hasStraightDraw = hasStraightDraw()
        super.initValues()
    }

    private fun calculateDecision(): Int {
        val botOdds = odds.getFlopOdds()
        val opponentOdds = odds.getOpponentFlopOdds()

        return 0

    }

    fun getDecision() = decision

}