package com.filipebicho.pokerclash.cards

import com.filipebicho.pokerclash.R

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

const val HEARTS = 0
const val SPADES = 1
const val  CLUBS = 2
const val DIAMONDS = 3

class Card(var rank: Int, var suit: Int) {

    private val rankArray = arrayOf("ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king")
    private val suitArray = arrayOf("hearts", "spades", "clubs", "diamonds")

    private val rankArraySymbols = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    private val suitArraySymbols = arrayOf("\u2665", "\u2660", "\u2663", "\u2666")

    /**
     * output card rank_suit
     */
    override fun toString(): String {
        return "${this.rankArray[this.rank]} ${this.suitArray[this.suit]}"
    }

    fun cardString(): String {
        return "${this.rankArraySymbols[this.rank]}${this.suitArraySymbols[this.suit]}"
    }

    fun cardRank(): String {
        return this.rankArraySymbols[this.rank]
    }

    fun cardSuit(): String {
        return this.suitArraySymbols[this.suit]
    }

    fun getCardDrawableResource(): Int {
        return when (suit) {
            HEARTS -> when (rank) {
                ACE -> R.drawable.hearts_ace
                KING -> R.drawable.hearts_king
                QUEEN -> R.drawable.hearts_queen
                JACK -> R.drawable.hearts_jack
                TEN -> R.drawable.hearts_10
                NINE -> R.drawable.hearts_9
                EIGHT -> R.drawable.hearts_8
                SEVEN -> R.drawable.hearts_7
                SIX -> R.drawable.hearts_6
                FIVE -> R.drawable.hearts_5
                FOUR -> R.drawable.hearts_4
                THREE -> R.drawable.hearts_3
                TWO -> R.drawable.hearts_2
                else -> R.drawable.card_back
            }
            SPADES -> when (rank) {
                ACE -> R.drawable.spades_ace
                KING -> R.drawable.spades_king
                QUEEN -> R.drawable.spades_queen
                JACK -> R.drawable.spades_jack
                TEN -> R.drawable.spades_10
                NINE -> R.drawable.spades_9
                EIGHT -> R.drawable.spades_8
                SEVEN -> R.drawable.spades_7
                SIX -> R.drawable.spades_6
                FIVE -> R.drawable.spades_5
                FOUR -> R.drawable.spades_4
                THREE -> R.drawable.spades_3
                TWO -> R.drawable.spades_2
                else -> R.drawable.card_back
            }
            CLUBS -> when (rank) {
                ACE -> R.drawable.clubs_ace
                KING -> R.drawable.clubs_king
                QUEEN -> R.drawable.clubs_queen
                JACK -> R.drawable.clubs_jack
                TEN -> R.drawable.clubs_10
                NINE -> R.drawable.clubs_9
                EIGHT -> R.drawable.clubs_8
                SEVEN -> R.drawable.clubs_7
                SIX -> R.drawable.clubs_6
                FIVE -> R.drawable.clubs_5
                FOUR -> R.drawable.clubs_4
                THREE -> R.drawable.clubs_3
                TWO -> R.drawable.clubs_2
                else -> R.drawable.card_back
            }
            DIAMONDS -> when (rank) {
                ACE -> R.drawable.diamonds_ace
                KING -> R.drawable.diamonds_king
                QUEEN -> R.drawable.diamonds_queen
                JACK -> R.drawable.diamonds_jack
                TEN -> R.drawable.diamonds_10
                NINE -> R.drawable.diamonds_9
                EIGHT -> R.drawable.diamonds_8
                SEVEN -> R.drawable.diamonds_7
                SIX -> R.drawable.diamonds_6
                FIVE -> R.drawable.diamonds_5
                FOUR -> R.drawable.diamonds_4
                THREE -> R.drawable.diamonds_3
                TWO -> R.drawable.diamonds_2
                else -> R.drawable.card_back
            }

            else -> R.drawable.card_back
        }
    }
}