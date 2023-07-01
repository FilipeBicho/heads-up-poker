package com.example.poker

import java.util.Collections
import java.util.TreeMap

class Hand(private var playerCards: ArrayList<Card>, var tableCards: ArrayList<Card>) {

    private var allCards = mutableListOf<Card>()
    private var hand = mutableListOf<Card>()
    private lateinit var suitRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private var rankRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private var hasAce: Boolean = false
    private var resultText: String = ""

    init {
        setHand()

        // sort by rank
        allCards = allCards.sortedBy { it.rank }.toCollection(ArrayList())

        // group repeated cards
        suitRepeatedCards = allCards.groupBy { it.suit }.entries.sortedByDescending { it.value.size }.toMutableList()
        rankRepeatedCards = allCards.groupBy { it.rank }.entries.sortedByDescending { it.value.size }.toMutableList()

        hasAce = allCards.any { it.rank == 0}

        isFlush()
    }

    /**
     * check if is has straight
     */
    private fun isFlush(): Boolean {

        val flushCards = suitRepeatedCards.first().value.sortedBy { it.rank }

        // more than 5 cards with the same suit
        if (flushCards.size >= 5) {

            // has Ace
            if (flushCards.any { it.rank == 0 }) {
                 hand.add(flushCards[0])
                 for (index in flushCards.size - 1 downTo flushCards.size - 4) {
                     hand.add(flushCards[index])
                 }
            } else {
                 for (index in flushCards.size - 1 downTo flushCards.size - 5) {
                     hand.add(flushCards[index])
                 }
            }
            resultText = "Flush"
            return true
        }

        return false
    }

    /**
     * Function to check if is a straight
     */
    private fun isStraight(): Boolean {

        var currentRank: Int?
        var nextCardRank: Int?
        var straight: Int = 1
        var lastSequentialCardRank: Int = -1
        var lastSequentialCardIndex: Int = -1

        for ((index, card) in allCards.withIndex()) {
            // already checked all cards
            if (index + 1 == allCards.size) {
                break
            }

            currentRank = card.rank
            nextCardRank = allCards[index + 1].rank

            // next card has the same rank
            if (currentRank == nextCardRank) {
                continue;
            }

            // is a sequential card
            if (currentRank == nextCardRank - 1) {
                straight++
                lastSequentialCardRank = nextCardRank
                lastSequentialCardIndex = index + 1
            } else {
                straight = 1
            }
        }

        // has straight 10 to Ace
        if (straight >= 4 && hasAce && lastSequentialCardRank == 12) {
            hand.add(allCards[0])
            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 3)) {
                hand.add(allCards[index])
            }

            resultText = "Straight"
            return true
        }

        // assign the biggest straight
        if (straight >= 5) {

            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 4)) {
               hand.add(allCards[index])
            }

            resultText = "Straight"
            return true
        }

        return false

    }

    /**
     * Set hand by merging player and table cards
     */
    private fun setHand() {
        allCards.addAll(0, this.playerCards)
        allCards.addAll(allCards.size, this.tableCards)
    }

    /**
     * Return player hand
     */
    fun getHand() = allCards;


}