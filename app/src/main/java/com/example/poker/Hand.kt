package com.example.poker

import java.util.Collections
import java.util.TreeMap

class Hand(private var playerCards: ArrayList<Card>, var tableCards: ArrayList<Card>) {

    private var allCards = mutableListOf<Card>()
    private var hand = mutableListOf<Card>()
    private lateinit var suitRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private var rankRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private lateinit var flushCards: List<Card>
    var resultText: String = ""
    var resultValue: Int = 0

    init {
        setHand()

        // sort by rank
        allCards = allCards.sortedBy { it.rank }.toCollection(ArrayList())

        // group repeated cards
        suitRepeatedCards = allCards.groupBy { it.suit }.entries.sortedByDescending { it.value.size }.toMutableList()
        rankRepeatedCards = allCards.groupBy { it.rank }.entries.sortedByDescending { it.value.size }.toMutableList()
        flushCards = suitRepeatedCards.first().value.sortedBy { it.rank }

        evaluateHand()
    }

    private fun evaluateHand() {

        if (isRoyalStraightFlush()) {
            resultValue = 10
            resultText = "Royal Straight Flush"
            return
        }

        if (isStraightFlush()) {
            resultValue = 9
            resultText = "Straight Flush"
            return
        }

        if (isFourOfAKind()) {
            resultValue = 8
            resultText = "Four of a Kind"
            return
        }

        if (isFlush()) {
            resultValue = 6
            resultText = "Flush"
            return
        }

        if (isStraight()) {
            resultValue = 5
            resultText = "Straight"
            return
        }
    }

    /**
     * Is Royal Straight Flush
     */
    private fun isRoyalStraightFlush(): Boolean {

        // is straight flush hand first card is an Ace
        if (isStraightFlush() && hand[0].rank == 0) {
            return true
        }

        hand.clear()
        return false
    }

    /**
     * is Straight flush
     */
    private fun isStraightFlush(): Boolean {

        if (flushCards.size >= 5) {
            if (isStraight(flushCards.toMutableList())) {
                return true
            }
        }

        return false
    }

    /**
     * is Four of a Kind
     */
    private fun isFourOfAKind(): Boolean {

        if (rankRepeatedCards.first().value.size == 4) {
            hand.addAll(rankRepeatedCards.first().value)
            setKicker()
            return true
        }

        return false
    }

    /**
     * check if is has straight
     */
    private fun isFlush(): Boolean {

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
            return true
        }

        return false
    }

    /**
     * Function to check if is a straight
     */
    private fun isStraight(cards: MutableList<Card> = allCards): Boolean {

        var currentRank: Int?
        var nextCardRank: Int?
        var sequentialCardsCount: Int = 1
        var straight: Int = 1
        var lastSequentialCardRank: Int = -1
        var lastSequentialCardIndex: Int = -1

        for ((index, card) in cards.withIndex()) {
            // already checked all cards
            if (index + 1 == cards.size) {
                break
            }

            currentRank = card.rank
            nextCardRank = cards[index + 1].rank

            // next card has the same rank
            if (currentRank == nextCardRank) {
                continue;
            }

            // is a sequential card
            if (currentRank == nextCardRank - 1) {
                sequentialCardsCount++

                if (straight < sequentialCardsCount) {
                    straight = sequentialCardsCount
                    lastSequentialCardRank = nextCardRank
                    lastSequentialCardIndex = index + 1
                }


            } else {
                sequentialCardsCount = 1
            }
        }

        // has straight 10 to Ace
        if (straight >= 4 && cards.any {it.rank == 0} && lastSequentialCardRank == 12) {
            hand.add(cards[0])
            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 3)) {
                hand.add(cards[index])
            }
            return true
        }

        // assign the biggest straight
        if (straight >= 5) {
            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 4)) {
               hand.add(cards[index])
            }
            return true
        }

        return false
    }

    private fun setKicker() {

        val otherCards: ArrayList<Card> = ArrayList()

        for (card: Card in allCards) {
            if (!hand.contains(card)) {
                otherCards.add(card)
            }
        }

        for (index: Int in hand.size .. 4) {
            if (otherCards.any { it.rank == 0 }) {
                hand.add(otherCards.removeFirst())
            } else {
                hand.add(otherCards.removeLast())
            }
        }
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
    fun getHand() = hand;


}