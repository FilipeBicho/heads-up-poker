package com.example.poker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

open class GameViewModel : ViewModel() {

    private val startMoney: Int = 1500
    private var gameTurn: Int = -1
    private var dealer: Int = -1
    private var blind: Int = -1
    private var pokerChips = intArrayOf(0, 0, 0)
    private var bet = intArrayOf(0, 0)

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

    var playerOddsValue by mutableStateOf(0)
        private set

    var computerOddsValue by mutableStateOf(0)
        private set

    var playerBet by mutableStateOf(0)
        private set
    var computerBet by mutableStateOf(0)
        private set

    var potMoney by mutableStateOf(0)
        private set

    private var cardDealer: Dealer = Dealer()
    private lateinit var playerOdds: Odds
    private lateinit var computerOdds: Odds

    init {
        preFlop()
    }

    fun preFlop()  {

        initGameTurns()
        resetBets()
        initPokerChips()

        // set player and computer cards
        cardDealer.setPlayerCards(playerCards, computerCards)

        if (pokerChips[blind] <= BIG_BLIND) {
            if (pokerChips[blind] <= SMALL_BLIND) {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pays all in
                bet[dealer] = bet[blind]
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                // update mutable state values
                updateMutableStateValues()

                // TODO: show down
            } else {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pay small blind
                bet[dealer] = SMALL_BLIND
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                // update mutable state values
                updateMutableStateValues()

                gameTurn = dealer

                // TODO: Dealer has the option to Fold or Call
            }
        } else if (pokerChips[dealer] <= SMALL_BLIND) {

            // dealer makes all in
            bet[dealer] = pokerChips[dealer]
            pokerChips[dealer] = 0

            // blind pays all in
            bet[blind] = pokerChips[blind]
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            // update mutable state values
            updateMutableStateValues()

            // TODO: show down
        } else {

            // dealer pay small blind
            bet[dealer] = SMALL_BLIND
            pokerChips[dealer] -= bet[dealer]

            // blind pay big blind
            bet[blind] = BIG_BLIND
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            // update mutable state values
            updateMutableStateValues()

            gameTurn = dealer

            // TODO: Dealer has the option to Fold, Call or Bet
        }
    }

    fun fold() {
    }

    fun call() {
    }

    fun bet() {
    }

    fun isPlayerTurn() = gameTurn == PLAYER

    fun isPlayerDealer() = dealer == PLAYER

    fun updatePlayerBet(value: Int) {
        playerBet = value
    }

    private fun updateMutableStateValues() {
        playerBet = bet[PLAYER]
        computerBet = bet[COMPUTER]
        playerMoney = pokerChips[PLAYER]
        computerMoney = pokerChips[COMPUTER]
        potMoney = pokerChips[POT]
    }

    private fun initGameTurns() {
        // Init or change dealer
        dealer = if (dealer == -1) {
            Random(System.nanoTime()).nextInt(0, 2)
        } else {
            if (dealer == 0) 1 else 0
        }

        // init blind turn
        blind = if (dealer == 0) 1 else 0

        // set game turn
        gameTurn = dealer
    }

    private fun resetBets() {
        playerBet = 0
        computerBet = 0
        potMoney = 0
    }

    private fun initPokerChips() {
        pokerChips[PLAYER] = playerMoney
        pokerChips[COMPUTER] = computerMoney
        pokerChips[POT] = potMoney
    }
}
