package com.example.poker

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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

                    player1.add(Card(8,0))
                    player1.add(Card(10,0))
                    table.add(Card(11, 0))
                    table.add(Card(0, 0))
                    table.add(Card(9, 0))
                    table.add(Card(12, 0))

                    val player1Hand = Hand(playerCards = player1, tableCards = table).getHand()

                    player1Hand.forEach{Log.d("player_hand", it.toString())}

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokerTheme {
        Greeting("Android")
    }
}

@Composable
fun HandImage(card1: Card, card2: Card, modifier: Modifier) {
    Row {
        card1.CardImage()
        card2.CardImage()
    }
}