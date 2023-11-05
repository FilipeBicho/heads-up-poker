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
    private var round = PRE_FLOP

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()
    var displayComputerCards = false
    var displayFoldButton = false
    var displayCallButton = false
    var displayCheckButton = false
    var displayBetButton = false
    
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

    var playerBetValue by mutableStateOf(0)
        private set

    var potValue by mutableStateOf(0)
        private set

    private var cardDealer: Dealer = Dealer()
    private lateinit var playerOdds: Odds
    private lateinit var computerOdds: Odds

    init {
        newGame()
    }

    /**
     * Handles fold request
     */
    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT]

        // update mutable state values
        updateMutableStateValues()

        // new game
        newGame()
    }

    /**
     * Handles check request
     */
    fun check() {

        if (preFlopCheck) {

            // only valid in the first check during the pre flop bets
            preFlopCheck = false

            updateMutableStateValues()
            switchPlayerTurn()

            if (isPlayerTurn()) {
                displayFoldButton = false
                displayCheckButton = true
                displayCallButton = false
                displayBetButton = true
            }
        } else {
            updateMutableStateValues()
            nextRound()
        }
    }

    /**
     * Handles call request
     */
    fun call() {

        val currentPlayerBet: Int = bet[player]

        if (pokerChips[player] <= bet[opponent]) {

            // player makes all in
            bet[player] += pokerChips[player]
            pokerChips[player] = 0

            // opponent equals player all in
            bet[opponent] = bet[player]
            pokerChips[opponent] = pokerChips[opponent] + bet[opponent] - bet[player]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()

            // TODO Show down
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= abs(currentPlayerBet - bet[player])

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()
            switchPlayerTurn()

            if (preFlopCheck) {
                if (isPlayerTurn()) {
                    displayFoldButton = false
                    displayCheckButton = true
                    displayCallButton = false
                    displayBetButton = true
                }

            } else {
                preFlopCheck = false
                nextRound()
            }
        }
    }

    /**
     * Handles bet request
     */
    fun bet() {

        val oldBet: Int = bet[player]

        // player makes a bet
        if (playerBetValue > bet[opponent] + BIG_BLIND) {
            bet[player] = playerBetValue
        } else {
            bet[player] = playerBetValue + bet[opponent]
        }

        if (bet[player] >= pokerChips[player] + oldBet) {
            bet[player] = oldBet + pokerChips[player]
        }

        pokerChips[player] -= bet[player] - oldBet;

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        // not possible anymore to do check in pre flop
        preFlopCheck = false

        updateMutableStateValues()
        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {
            displayFoldButton = true
            displayCheckButton = false
            displayCallButton = true
            displayBetButton = false
        } else {
            displayFoldButton = true
            displayCheckButton = false
            displayCallButton = true
            displayBetButton = true
        }
    }

    fun isPlayerTurn() = true

    fun isPlayerDealer() = dealer == player

    /**
     * Update player bet via button interaction
     */
    fun updatePlayerBet(value: Int) {

        playerBetValue = if (value > pokerChips[player]) {
            pokerChips[player]
        } else {
            value
        }
    }

    /**
     * Calculate pre flop bets
     */
    private fun preFlopBets()  {

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

                if (isPlayerTurn()) {
                    displayFoldButton = true
                    displayCheckButton = false
                    displayCallButton = true
                    displayBetButton = false
                }
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

            if (isPlayerTurn()) {
                displayFoldButton = true
                displayCheckButton = false
                displayCallButton = true
                displayBetButton = true
            }
        }
    }

    /**
     * Init new game values
     */
    private fun newGame() {
        // reset values
        playerBet = 0
        computerBet = 0
        potValue = 0
        preFlopCheck = true
        round = PRE_FLOP

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

        // clear cards
        playerCards.clear()
        computerCards.clear()
        tableCards.clear()

        // set player and computer cards
        cardDealer.setPlayerCards(playerCards, computerCards)

        // pre flop bets
        preFlopBets()
    }

    /**
     * update game screen
     */
    private fun updateMutableStateValues() {
        playerBet = bet[PLAYER]
        computerBet = bet[COMPUTER]
        playerMoney = pokerChips[PLAYER]
        computerMoney = pokerChips[COMPUTER]
        potValue = pokerChips[POT]
        playerBetValue = BIG_BLIND
    }

    /**
     * Switch player turns
     */
    private fun switchPlayerTurn() {
        player = if (player == PLAYER) COMPUTER else PLAYER
        opponent = if (player == COMPUTER) PLAYER else COMPUTER
    }

    /**
     * set next round
     */
    private fun nextRound() {
        round++

        if (player == dealer) {
            switchPlayerTurn()
        }

        playerBet = 0
        computerBet = 0
        bet[PLAYER] = 0
        bet[COMPUTER] = 0

        when (round) {
            FLOP -> cardDealer.setFlopCards(tableCards)
            TURN -> cardDealer.setTurnCard(tableCards)
            RIVER -> cardDealer.setRiverCard(tableCards)
            else -> calculateWinner()
        }
    }

    /**
     * Calculate winner
     */
    private fun calculateWinner() {
        displayFoldButton = false
        displayCheckButton = false
        displayCallButton = false
        displayBetButton = false
    }
}
