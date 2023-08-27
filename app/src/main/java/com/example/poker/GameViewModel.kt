package com.example.poker


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    var player1Cards = mutableStateListOf<Card>()

    var player2Cards = mutableStateListOf<Card>()

    var tableCards = mutableStateListOf<Card>()
    var pot by mutableStateOf(0)
        private set
    var player1Bet by mutableStateOf(0)
        private set


    var dealer: Dealer = Dealer(player1Cards, player2Cards)

    init {
        dealer.setPlayerCards(player1Cards = player1Cards, player2Cards = player2Cards)
    }

    fun dealFlop() {
        dealer.setFlopCards(tableCards = tableCards)
    }

    fun dealTurn() {
        dealer.setTurnCard(tableCards = tableCards)
    }

    fun dealRiver() {
        dealer.setRiverCard(tableCards = tableCards)
    }

}