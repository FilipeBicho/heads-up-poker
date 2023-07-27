package com.example.poker

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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


    @Preview
    @Composable
    fun Layout() {
        Background()
        Column(modifier =Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .background(Color.Green),
                contentAlignment = Alignment.BottomCenter
            ){
                Column {

                }
            }
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
                    .weight(0.5f)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Column {

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    TableCards()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(Color.Green),
                contentAlignment = Alignment.Center
            ) {
                Column {

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .background(Color.Yellow),
                contentAlignment = Alignment.BottomCenter
            ){
                Column {

                }
            }
        }
    }

    @Composable    
    fun Player1Cards() {
        val modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        val contentScale = ContentScale.Fit
        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in player1Cards) {
                CardImage(card = card, modifier, contentScale)
            }
        }
    }

    @Composable
    fun Player2Cards() {

        val modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        val contentScale = ContentScale.Fit
        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in player2Cards) {
                CardImage(card = card, modifier, contentScale)
            }
        }
    }

    @Composable
    fun TableCards() {
        val modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        val contentScale = ContentScale.Fit

        Row (
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in tableCards) {
                CardImage(card = card, modifier, contentScale)
            }
        }
    }
    
    @SuppressLint("DiscouragedApi")
    @Composable
    fun CardImage(card: Card, modifier: Modifier, contentScale: ContentScale) {
        val context = LocalContext.current;
        val imageId = context.resources.getIdentifier(card.getCardImagePath(), "drawable", context.packageName);

        Image(
            modifier = modifier,
            painter = painterResource(id = imageId),
            contentScale = contentScale,
            contentDescription = "card"
        )

    }

}

