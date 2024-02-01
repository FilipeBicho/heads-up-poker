package com.example.poker

const val ACE = 0
const val KING = 12
const val QUEEN = 11
const val JACK = 10
const val TEN = 9
const val NINE = 8
const val EIGHT = 7
const val SEVEN = 6
const val SIX = 5
const val FIVE = 4
const val FOUR = 3
const val THREE = 2
const val TWO = 1

class Card(var rank: Int, var suit: Int) {

    private val rankArray = arrayOf("ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king")
    private val suitArray = arrayOf("hearts", "spades", "clubs", "diamonds")

    private val rankArraySymbols = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    private val suitArraySymbols = arrayOf("\u2665", "\u2660", "\u2663", "\u2666")

    /**
     * Get Card image path
     */
    fun getCardImagePath() = "card_${this.rankArray[this.rank]}_${this.suitArray[this.suit]}"

    /**
     * output card rank_suit
     */
    override fun toString(): String {
        return "${this.rankArray[this.rank]}_${this.suitArray[this.suit]}"
    }

    fun cardString(): String {
        return "${this.rankArraySymbols[this.rank]}${this.suitArraySymbols[this.suit]}"
    }
}