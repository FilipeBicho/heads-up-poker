package com.example.poker

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
                    .weight(0.3f)
            ) {
                Row(Modifier.align(Alignment.TopCenter)) {
                    PlayerCards(player1Cards)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Row(Modifier.align(Alignment.TopCenter)) {
                    Column() {
                        Text(
                            text = "Pot: $pot €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.Bottom),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.End
                        )

                        Row(modifier = Modifier.weight(0.6f)) {
                            TableCards(Modifier.padding(all = 5.dp))
                        }

                        Text(
                            text = "$bet €", modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally),
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Row(Modifier.align(Alignment.TopCenter)) {
                    PlayerCards(player2Cards)
                }
            }
        }
    }

    @Composable
    fun PlayerCards(cards: ArrayList<Card>) {
        Box(
        ) {
            CardImage(
                card = cards.first(),
                Modifier
                    .rotate(-5f)
                    .padding(top = 5.dp, bottom = 5.dp, end = 15.dp)
                    .align(Alignment.TopCenter)
            )
            CardImage(
                card = cards.last(),
                Modifier
                    .zIndex(2f)
                    .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                    .rotate(5f)
                    .align(Alignment.TopCenter)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .zIndex(3f)
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter)
                    .width(150.dp)
                    .background(Color.Black.copy(alpha = 0.9f), shape = RoundedCornerShape(5.dp))
            ) {
                Text(text = "Filipe Bicho",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Divider(
                    color = Color.White,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Text(text = "1500 €",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center)
            }
        }
    }

    @Composable
    fun TableCards(modifier: Modifier) {
        for (card in tableCards) {
            CardImage(card = card, modifier)
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

