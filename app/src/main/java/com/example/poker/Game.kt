package com.example.poker

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable

class Game : ComponentActivity() {

    private val deck = Deck()
    val player1Cards = ArrayList<Card>()
    val player2Cards = ArrayList<Card>()
    val tableCards = ArrayList<Card>()
    val dealer = Dealer()
    val pot: Int = 700
    val bet: Int = 300

    @Composable
    fun StartGame() {
        dealer.dealCards(deck, player1Cards, player2Cards)
        dealer.flop(deck, tableCards)
        dealer.turn(deck, tableCards)
        dealer.river(deck, tableCards)
        Layout(this)
    }
}