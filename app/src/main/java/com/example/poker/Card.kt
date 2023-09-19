package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

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
}