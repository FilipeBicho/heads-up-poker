package com.example.poker

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class Game : ComponentActivity() {


    @Composable
    fun StartGame() {
        val deck = Deck();
        val player1Cards = ArrayList<Card>();
        val player2Cards = ArrayList<Card>();
        val table = ArrayList<Card>();
        val dealer = Dealer();

        dealer.dealCards(deck, player1Cards, player2Cards)

        PlayerCards(cards = player1Cards, Modifier)
    }

    @Composable    
    fun PlayerCards(cards: ArrayList<Card>, modifier: Modifier) {

        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in cards) {
                CardImage(card = card, modifier)
            }
        }
    }


    @Composable
    fun ResultPreview(flop: String, turn: String, river: String, result: String) {
        Column {
            Text(text = flop)
            Text(text = turn)
            Text(text = river)
            Text(text = result)
        }
    }
    
    @SuppressLint("DiscouragedApi")
    @Composable
    fun CardImage(card: Card, modifier: Modifier) {
        val context = LocalContext.current;
        val imageId = context.resources.getIdentifier(card.getCardImagePath(), "drawable", context.packageName);
        val imageModifier = Modifier.size(80.dp)

        Image(
            modifier = imageModifier,
            painter = painterResource(id = imageId),
            contentDescription = "card"
        )

    }

}

