package com.example.poker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val startMoney: Int = 1500
    val smallBlind: Int = 20
    val bigBlind: Int = 40

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()
    var displayComputerCards = false

    var playerName by mutableStateOf("Filipe")
        private set

    var computerName by mutableStateOf("Computer")
        private set

    var playerMoney by mutableStateOf(startMoney)
        private set

    var computerMoney by mutableStateOf(startMoney)
        private set

    var playerBet by mutableStateOf(0)
        private set
    var computerBet by mutableStateOf(0)
        private set

    private var gameTurn by mutableStateOf(0)

    private var dealerTurn by mutableStateOf(0)

    var pot by mutableStateOf(0)
        private set

    private var dealer: Dealer = Dealer()

    init {
        dealer.setPlayerCards(playerCards = playerCards, computerCards = computerCards)
        dealerTurn = 1//Random(System.nanoTime()).nextInt(0, 2)

        gameTurn = if (dealerTurn == dealer.playerTurn) {
            dealer.computerTurn
        } else {
            dealer.playerTurn
        }
    }

    fun fold() {
        playerName = "Fold"
    }

    fun call() {
        playerName = "Call"
    }

    fun bet() {
        playerName = "Bet"
    }

    fun isPlayerTurn() = gameTurn == dealer.playerTurn

    fun isPlayerDealer() = dealerTurn == dealer.playerTurn

    fun updatePlayerBet(value: Int) {
        playerBet = value
    }

}