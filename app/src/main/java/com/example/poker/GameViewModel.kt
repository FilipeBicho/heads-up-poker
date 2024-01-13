package com.example.poker

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private var totalPot: Int = 0
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
    private lateinit var combinations: MutableList<ArrayList<Card>>

    init {
        newGame()
    }

    /**
     * Handles fold request
     */
    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT] + totalPot

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

            updateMutableStateValues()
            switchPlayerTurn()

            if (isPlayerTurn()) {
                _uiState.update { currentState -> currentState.copy(
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                bet[COMPUTER] = BIG_BLIND
                bet()
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
                    bet[COMPUTER] = pokerChips[COMPUTER]
                    bet()
                }

            } else {
                nextRound()
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
        val winnerCalculator =
            HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)

        when (winnerCalculator.getWinner()) {
            PLAYER -> pokerChips[PLAYER] += totalPot
            COMPUTER -> pokerChips[COMPUTER] += totalPot
            else -> {
                pokerChips[PLAYER] += totalPot / 2
                pokerChips[COMPUTER] += totalPot / 2
            }
        }

        updateMutableStateValues()

        if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
            newGame()
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
        totalPot = 0
        checkAvailable = true
        cardDealer = Dealer()

        _uiState.update { currentState -> currentState.copy(
            playerBet = 0,
            computerBet = 0,
            playerBetValue = 0,
            computerBetValue = 0,
            totalPot = 0,
            currentPot = 0,
            computerOddsValue = 0,
            playerOddsValue = 0
        ) }

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

        // init table cards

        // flop
        cardDealer.setFlopCards(tableCards)

        val time = measureTimeMillis {
            combinations = Combinations(tableCards).combinations
        }
        Log.d("ODDS combinations time", time.toString())


        odds = Odds(combinations)

        // turn
        cardDealer.setTurnCard(tableCards)

        //river
        cardDealer.setRiverCard(tableCards)

        val time1 = measureTimeMillis {
            odds.calculateFlopOdds(computerCards, tableCards.subList(0,3))
        }
        Log.d("ODDS flop odds time", time1.toString())

        val time3 = measureTimeMillis {
            odds.calculateTurnOdds(computerCards, tableCards.subList(0,4))
        }
        Log.d("ODDS turn odds time", time3.toString())

        val time5 = measureTimeMillis {
            odds.calculateRiverOdds(computerCards, tableCards)
        }
        Log.d("ODDS turn odds time", time5.toString())

        _uiState.update { currentState -> currentState.copy(
            displayComputerCards = true,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            playerOddsValue = PreFlopOdds(playerCards).getOdds(),
            computerOddsValue = PreFlopOdds(computerCards).getOdds()
        )}

        // pre flop bets
        preFlopBets()
    }

    /**
     * go to next round
     */
    private fun nextRound() {

        if (player == dealer) {
            switchPlayerTurn()
        }

        totalPot += pokerChips[POT]
        bet[PLAYER] = 0
        bet[COMPUTER] = 0
        checkAvailable = true

        _uiState.update { currentState -> currentState.copy(
            playerBet = 0,
            computerBet = 0,
            totalPot = totalPot
        ) }

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
                    bet[COMPUTER] = BIG_BLIND
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
                    bet[COMPUTER] = BIG_BLIND
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
                    bet[COMPUTER] = BIG_BLIND
                    bet()
                }
            }

            RIVER -> {
                showdown()
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

                // update mutable state values
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

                // update mutable state values
                updateMutableStateValues()

                player = dealer

                if (isPlayerTurn()) {
                    _uiState.update { currentState -> currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false
                    )}
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

            // update mutable state values
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

    /**
     * update game screen
     */
    private fun updateMutableStateValues() {
        _uiState.update { currentState -> currentState.copy(
            playerBet = bet[PLAYER],
            computerBet = bet[COMPUTER],
            playerMoney = pokerChips[PLAYER],
            computerMoney = pokerChips[COMPUTER],
            currentPot = pokerChips[POT],
            playerBetValue = BIG_BLIND
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
     * Show all cards and calculate winner
     */
    private fun showdown() {

        _uiState.update { currentState -> currentState.copy(
            displayComputerCards = true,
            turnDelayTime = 2000,
            riverDelayTime = 3000,
        )}

        when (round) {
            PRE_FLOP -> {
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

                showdown()
            }

            FLOP -> {
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

                showdown()
            }

            TURN -> {
                round = RIVER

                _uiState.update { currentState -> currentState.copy(
                    displayRiver = true
                )}

                showdown()
            }

            RIVER -> {
                calculateWinner()
            }
        }
    }
}
