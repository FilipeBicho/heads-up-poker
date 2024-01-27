package com.example.poker

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

open class GameViewModel : ViewModel() {

    private var player: Int = -1
    private var opponent: Int = -1
    private var dealer: Int = -1
    private var blind: Int = -1
    private var totalPotValue: Int = 0
    private var betValue: Int = 0
    private var pokerChips = intArrayOf(0, 0, 0)
    private var bet = intArrayOf(0, 0)
    private var checkAvailable: Boolean = true
    private var round = PRE_FLOP

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()


    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var cardDealer: Dealer
    private lateinit var odds: Odds

    init {
        newGame()
    }

    /**
     * Handles fold request
     */
    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT] + totalPotValue

        // update mutable state values
        updateMutableStateValues()

        // new game
        newGame()
    }

    /**
     * Handles check request
     */
    fun check() {

        if (checkAvailable && round != PRE_FLOP) {

            checkAvailable = false
            switchPlayerTurn()

            if (isPlayerTurn()) {
                _uiState.update { currentState -> currentState.copy(
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                betValue = BIG_BLIND
                bet()
            }
        } else {
            viewModelScope.launch {
                delay(1000)
                nextRound()
            }
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
            showdown()
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= abs(currentPlayerBet - bet[player])

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()
            switchPlayerTurn()

            if (checkAvailable && round == PRE_FLOP) {
                if (isPlayerTurn()) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    betValue = BIG_BLIND
                    bet()
                }

            } else {
                viewModelScope.launch {
                    delay(1000)
                    nextRound()
                }
            }
        }
    }

    /**
     * Handles bet request
     */
    fun bet() {

        checkAvailable = false

        val oldBet: Int = bet[player]

        // player makes a bet
        if (betValue > bet[opponent] + BIG_BLIND) {
            bet[player] = betValue
        } else {
            bet[player] = betValue + bet[opponent]
        }

        if (bet[player] > pokerChips[player]) {
            bet[player] = pokerChips[player] + oldBet
        }

        // if all in add old bet
        if (pokerChips[player] == bet[player]) {
            bet[player] += oldBet
        }

        pokerChips[player] = pokerChips[player] - bet[player] + oldBet

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        updateMutableStateValues()
        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {

            if (isPlayerTurn()) {
                _uiState.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = false
                )}
            } else {
                call()
            }

        } else {
            if (isPlayerTurn()) {
                _uiState.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                betValue = BIG_BLIND
                bet()
            }
        }
    }

    /**
     * check if is player turn
     */
    fun isPlayerTurn() = player == PLAYER

    /**
     * check if player is dealer
     */
    fun isPlayerDealer() = dealer == PLAYER

    /**
     * Update player bet via button interaction
     */
    fun updatePlayerBet(value: Int) {

        betValue = if (value > pokerChips[player]) {
            pokerChips[player]
        } else {
            value
        }

        _uiState.update { currentState -> currentState.copy(
            playerBetValue = betValue
        ) }
    }

    /**
     * update game screen
     */
    private fun updateMutableStateValues() {
        _uiState.update { currentState -> currentState.copy(
            playerText = "${bet[PLAYER]} €",
            computerText = "${bet[COMPUTER]} €",
            playerMoney = pokerChips[PLAYER],
            computerMoney = pokerChips[COMPUTER],
            currentPot = pokerChips[POT],
            playerBetValue = BIG_BLIND,
            totalPot = totalPotValue
        )}
    }

    /**
     * Switch player turns
     */
    private fun switchPlayerTurn() {
        player = if (player == PLAYER) COMPUTER else PLAYER
        opponent = if (player == COMPUTER) PLAYER else COMPUTER
    }

    /**
     * Calculate winner
     */
    private fun calculateWinner() {
        _uiState.update { currentState -> currentState.copy(
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false
        )}

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        val computerHand = Hand(playerCards = computerCards, tableCards = tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)


        when (winnerCalculator.getWinner()) {
            PLAYER -> {
                pokerChips[PLAYER] += totalPotValue

                _uiState.update { currentState -> currentState.copy(
                    playerOddsValue = 100,
                    computerOddsValue = 0,
                    winnerText = "Player wins ${totalPotValue} €"
                )}
            }
            COMPUTER -> {
                pokerChips[COMPUTER] += totalPotValue

                _uiState.update { currentState -> currentState.copy(
                    playerOddsValue = 0,
                    computerOddsValue = 100,
                    winnerText = "Computer wins ${totalPotValue} €"
                )}
            }
            else -> {
                pokerChips[PLAYER] += totalPotValue / 2
                pokerChips[COMPUTER] += totalPotValue / 2

                _uiState.update { currentState -> currentState.copy(
                    playerOddsValue = 0,
                    computerOddsValue = 0,
                    winnerText = "Draw, split ${totalPotValue} €"
                )}
            }
        }

        _uiState.update { currentState -> currentState.copy(
            playerText = playerHand.resultText,
            computerText = computerHand.resultText,
            playerMoney = pokerChips[PLAYER],
            computerMoney = pokerChips[COMPUTER],
            currentPot = pokerChips[POT],
            playerBetValue = BIG_BLIND,
            totalPot = totalPotValue
        )}

        viewModelScope.launch {
            delay(2000)
            if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
                newGame()
            }
        }
    }

    /**
     * Calculate pre flop bets
     */
    private fun preFlopBets() {

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

                updateMutableStateValues()
                showdown()
            } else {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pay small blind
                bet[dealer] = SMALL_BLIND
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                updateMutableStateValues()
                player = dealer

                if (isPlayerTurn()) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false
                    )}
                } else {
                    call()
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

            updateMutableStateValues()
            showdown()
        } else {

            // dealer pay small blind
            bet[dealer] = SMALL_BLIND
            pokerChips[dealer] -= bet[dealer]

            // blind pay big blind
            bet[blind] = BIG_BLIND
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            updateMutableStateValues()
            player = dealer

            if (isPlayerTurn()) {
                _uiState.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                call()
            }
        }
    }

    private fun showdownFlop() {
        round = FLOP

        odds.calculateShowdownFlopOdds(
            playerCards = playerCards,
            opponentCards = computerCards,
            tableCards = tableCards.subList(0, 3)
        )

        _uiState.update { currentState -> currentState.copy(
            displayFlop = true,
            playerOddsValue = odds.getShowdownPlayerOdds(),
            computerOddsValue = odds.getShowdownOpponentOdds()
        )}
    }

    private fun showdownTurn() {
        round = TURN

        odds.calculateShowdownTurnOdds(
            playerCards = playerCards,
            opponentCards = computerCards,
            tableCards = tableCards.subList(0, 4)
        )
        _uiState.update { currentState -> currentState.copy(
            displayTurn = true,
            playerOddsValue = odds.getShowdownPlayerOdds(),
            computerOddsValue = odds.getShowdownOpponentOdds()
        )}
    }

    private fun showdownRiver() {
        round = RIVER
        _uiState.update { currentState -> currentState.copy(
            displayRiver = true
        )}
    }

    /**
     * Show all cards and calculate winner
     */
    private fun showdown() {

        totalPotValue = pokerChips[POT]

        _uiState.update { currentState -> currentState.copy(
            displayComputerCards = true,
            showdown = true,
            totalPot = totalPotValue
        )}

        when (round) {
            PRE_FLOP -> {
                showdownFlop()
                showdown()
            }

            FLOP -> {
                viewModelScope.launch {
                    delay(2000)
                    showdownTurn()
                    showdown()
                }
            }

            TURN -> {
                viewModelScope.launch {
                    delay(1000)
                    showdownRiver()
                    showdown()
                }
            }

            RIVER -> {
                viewModelScope.launch {
                    calculateWinner()
                }
            }
        }
    }

    /**
     * go to next round
     */
    private fun nextRound() {

        if (player == dealer) {
            switchPlayerTurn()
        }

        totalPotValue += pokerChips[POT]
        bet[PLAYER] = 0
        bet[COMPUTER] = 0
        checkAvailable = true

        updateMutableStateValues()

        when (round) {
            PRE_FLOP -> {
                round = FLOP

                _uiState.update { currentState -> currentState.copy(
                    displayFlop = true,
                    turnDelayTime = 0,
                    riverDelayTime = 1000,
                    computerOddsValue = odds.getFlopOdds()
                ) }

                if (player == PLAYER) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    betValue = BIG_BLIND
                    bet()
                }
            }

            FLOP -> {
                round = TURN

                _uiState.update { currentState -> currentState.copy(
                    displayTurn = true,
                    turnDelayTime = 0,
                    riverDelayTime = 0,
                    computerOddsValue = odds.getTurnOdds()
                )}

                if (player == PLAYER) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    betValue = BIG_BLIND
                    bet()
                }

            }

            TURN -> {

                round = RIVER
                cardDealer.setRiverCard(tableCards)

                _uiState.update { currentState -> currentState.copy(
                    displayRiver = true,
                    computerOddsValue = odds.getRiverOdds()
                )}

                if (player == PLAYER) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    betValue = BIG_BLIND
                    bet()
                }
            }

            RIVER -> {
                calculateWinner()
            }
        }
    }

    /**
     * Init new game values
     */
    private fun newGame() {
        round = PRE_FLOP

        // reset values
        bet[PLAYER] = 0
        bet[COMPUTER] = 0
        totalPotValue = 0
        checkAvailable = true
        cardDealer = Dealer()

        // init poker chips
        pokerChips[PLAYER] = uiState.value.playerMoney
        pokerChips[COMPUTER] = uiState.value.computerMoney
        pokerChips[POT] = 0

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

        // set flop
        cardDealer.setFlopCards(tableCards)

        odds = Odds(Combinations(tableCards).combinations)

        // set turn
        cardDealer.setTurnCard(tableCards)

        // set river
        cardDealer.setRiverCard(tableCards)

        // calculate flop odds
        odds.calculateFlopOdds(computerCards, tableCards.subList(0,3))

        // calculate turn odds
        odds.calculateTurnOdds(computerCards, tableCards.subList(0,4))

        // calculate river odds
        odds.calculateRiverOdds(computerCards, tableCards)

        _uiState.update { currentState -> currentState.copy(
            displayComputerCards = true,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            computerOddsValue = PreFlopOdds(computerCards).getOdds(),
            playerBetValue = 0,
            computerBetValue = 0,
            totalPot = 0,
            currentPot = 0,
            winnerText = ""
        )}

        // pre flop bets
        preFlopBets()
    }
}
