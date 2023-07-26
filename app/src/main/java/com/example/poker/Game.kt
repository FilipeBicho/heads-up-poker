package com.example.poker

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Game : ComponentActivity() {

    val deck = Deck();
    val player1Cards = ArrayList<Card>();
    val player2Cards = ArrayList<Card>();
    val tableCards = ArrayList<Card>();
    val dealer = Dealer();

    @Composable
    fun StartGame() {

        dealer.dealCards(deck, player1Cards, player2Cards)
        dealer.flop(deck, tableCards)
        dealer.turn(deck, tableCards)
        dealer.river(deck, tableCards)
        Layout()
    }


    @Composable
    fun Layout() {
        Background()
        Column(modifier =Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column() {
                    Player1Cards()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    TableCards()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ){
                Column {
                    Player2Cards()
                }
            }
        }
    }

    @Composable    
    fun Player1Cards() {

        val modifier = Modifier.size(150.dp)
        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in player1Cards) {
                CardImage(card = card, modifier)
            }
        }
    }

    @Composable
    fun Player2Cards() {

        val modifier = Modifier.size(150.dp)
        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in player2Cards) {
                CardImage(card = card, modifier)
            }
        }
    }

    @Composable
    fun TableCards() {

        val modifier = Modifier.size(80.dp)
        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in tableCards) {
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

        Image(
            modifier = modifier,
            painter = painterResource(id = imageId),
            contentDescription = "card"
        )

    }

}

