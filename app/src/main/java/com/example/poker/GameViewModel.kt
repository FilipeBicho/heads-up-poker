package com.example.poker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.abs
import kotlin.random.Random

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

open class GameViewModel : ViewModel() {

    private val startMoney: Int = 1500
    private var player: Int = -1
    private var opponent: Int = -1
    private var dealer: Int = -1
    private var blind: Int = -1
    private var pokerChips = intArrayOf(0, 0, 0)
    private var bet = intArrayOf(0, 0)
    private var preFlopCheck: Boolean = true

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

    var potValue by mutableStateOf(0)
        private set

    private var cardDealer: Dealer = Dealer()
    private lateinit var playerOdds: Odds
    private lateinit var computerOdds: Odds

    init {
        newGame()
    }

    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT]

        // update mutable state values
        updateMutableStateValues()

        // new game
        newGame()
    }

    fun call() {

        val currentPlayerBet: Int = bet[player]

        // equals bet from opponent
        bet[player] = bet[opponent]

        if (pokerChips[player] <= bet[opponent]) {

            // player makes all in
            bet[player] = pokerChips[player]
            pokerChips[player] = 0

            // opponent equals player all in
            bet[opponent] = bet[player]
            pokerChips[opponent] = pokerChips[opponent] + bet[opponent] - bet[player]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= abs(currentPlayerBet - bet[player])

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()
        }

        if (preFlopCheck) {
            // TODO Opponent has the option to check or bet during pre flop
        } else {
            preFlopCheck = false

            // TODO Go to next round
        }
    }

    fun bet() {

        val currentPlayerBet: Int = bet[player]

        // player makes a bet
        bet[player] += if (player == PLAYER) playerBet else computerBet
        pokerChips[player] -= abs(bet[player] - currentPlayerBet)

        // calculate pot
        pokerChips[POT] = bet[blind] + bet[dealer]

        updateMutableStateValues()

        // TODO Go to next round
    }

    fun isPlayerTurn() = player == PLAYER

    fun isPlayerDealer() = dealer == PLAYER

    fun updatePlayerBet(value: Int) {

        playerBet = if (value > pokerChips[PLAYER]) {
            pokerChips[PLAYER]
        } else {
            value
        }
    }

    private fun preFlop()  {

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

                player = dealer

                // TODO: Opponent has the option to Fold or Call
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

            player = dealer

            // TODO: Opponent has the option to Fold, Call or Bet
        }
    }

    private fun newGame() {
        // reset values
        playerBet = 0
        computerBet = 0
        potValue = 0
        preFlopCheck = true

        // init poker chips
        pokerChips[PLAYER] = playerMoney
        pokerChips[COMPUTER] = computerMoney
        pokerChips[POT] = potValue

        // Init or change dealer
        dealer = if (dealer == -1) {
            Random(System.nanoTime()).nextInt(0, 2)
        } else {
            if (dealer == 0) 1 else 0
        }

        // init blind turn
        blind = if (dealer == 0) 1 else 0

        // set turns
        player = dealer
        opponent = blind

        // set pre flop
        preFlop()
    }

    private fun updateMutableStateValues() {
        playerBet = bet[PLAYER]
        computerBet = bet[COMPUTER]
        playerMoney = pokerChips[PLAYER]
        computerMoney = pokerChips[COMPUTER]
        potValue = pokerChips[POT]
    }
}
