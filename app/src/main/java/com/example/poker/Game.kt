package com.example.poker

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

class Game : ComponentActivity() {

    private val deck = Deck()
    private val player1Cards = ArrayList<Card>()
    private val player2Cards = ArrayList<Card>()
    private val tableCards = ArrayList<Card>()
    private val dealer = Dealer()
    private val pot: Int = 700
    private val bet: Int = 300

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
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column() {
                    PlayerCards(player1Cards)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    Text(text = "Pot: $pot €", fontWeight = FontWeight.Bold)
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
                    .weight(0.5f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Text(text = "$bet €", fontSize = 15.sp)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    PlayerCards(player2Cards)
                }
            }
        }
    }

    @Composable
    fun PlayerCards(cards: ArrayList<Card>) {

        Box {
            CardImage(card = cards.first(), Modifier.padding(bottom = 5.dp))
            CardImage(
                card = cards.last(),
                Modifier
                    .zIndex(2f)
                    .padding(start = 20.dp, top = 5.dp)
            )
        }
    }

    @Composable
    fun TableCards() {
        val modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)

        Row(
            horizontalArrangement = Arrangement.Center,
        )
        {
            for (card in tableCards) {
                CardImage(card = card, modifier)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    @Composable
    fun CardImage(card: Card, modifier: Modifier) {
        val context = LocalContext.current;
        val imageId = context.resources.getIdentifier(
            card.getCardImagePath(),
            "drawable",
            context.packageName
        )

        Image(
            modifier = modifier,
            painter = painterResource(id = imageId),
            contentScale = ContentScale.Fit,
            contentDescription = "card"
        )
    }
}

