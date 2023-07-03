package com.example.poker

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.poker.ui.theme.PokerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val deck = Deck();
                    val player1 = ArrayList<Card>();
                    val player2 = ArrayList<Card>();
                    val table = ArrayList<Card>();
                    val dealer = Dealer();

                    player1.add(Card(8,1))
                    player1.add(Card(3,2))

                    player2.add(Card(8, 0))
                    player2.add(Card(6, 3))

                    table.add(Card(1, 0))
                    table.add(Card(1, 1))
                    table.add(Card(1, 2))
                    table.add(Card(1, 3))
                    table.add(Card(4, 0))

                    val player1Hand = Hand(playerCards = player1, tableCards = table)
                    val player2Hand = Hand(playerCards = player2, tableCards = table)
                    val winnerCalculator = HandWinnerCalculator(player1Hand, player2Hand)

                    val player1HandText = "${player1Hand.getHand().onEach { it.toString() }} - ${player1Hand.resultText}"
                    val player2HandText = "${player2Hand.getHand().onEach { it.toString() }} - ${player2Hand.resultText}"

                    ResultPreview(player1HandText, player2HandText, winnerCalculator.getResult())
                }
            }
        }
    }
}


@Composable
fun ResultPreview(player1: String, player2: String, result: String) {
    Column {
        Text(text = player1)
        Text(text = player2)
        Text(text = result)
    }
}

@Composable
fun HandImage(card1: Card, card2: Card, modifier: Modifier) {
    Row {
        card1.CardImage()
        card2.CardImage()
    }
}