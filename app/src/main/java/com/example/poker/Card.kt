package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

class Card(var rank: Int, var suit: Int) {

    /**
     * Card rank
     */
    private val rankArray = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")

    /**
     * Card suit
     */
    private val suitArray = arrayOf("hearts", "spades", "clubs", "diamonds")

    /**
     * Get Card image path
     */
    private fun getCardImagePath() = "card_${this.rankArray[this.rank]}_${this.suitArray[this.suit]}"

    @SuppressLint("DiscouragedApi")
    @Composable
    fun CardImage() {
        val context = LocalContext.current;
        val imageId = context.resources.getIdentifier(this.getCardImagePath(), "drawable", context.packageName);

        Column() {
            Image(painter = painterResource(id = imageId), contentDescription = "card",)
        }
    }

    /**
     * output card rank_suit
     */
    override fun toString(): String {
        return "${this.rankArray[this.rank]}_${this.suitArray[this.suit]}"
    }
}