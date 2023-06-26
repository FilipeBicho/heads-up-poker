package com.example.poker

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

class Card(private var rank: Int, private var suit: Int) {

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
    fun getCardImagePath(): String {

        return "card_${this.rankArray[this.rank]}_${this.suitArray[this.suit]}";
    }

    @Composable
    fun CardImage(filePath: String) {
        val context = LocalContext.current;
        val imageId = context.resources.getIdentifier(filePath, "drawable", context.packageName);

        Image(painter = painterResource(id = imageId), contentDescription = "card")
    }
}