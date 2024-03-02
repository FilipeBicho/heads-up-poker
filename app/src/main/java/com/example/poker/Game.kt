package com.example.poker

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poker.helper.HandGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

abstract class Game: ViewModel() {

    protected var player: Int = -1
    protected var opponent: Int = -1
    protected var dealer: Int = -1
    protected var blind: Int = -1
    protected var totalPotValue: Int = 0
    protected var betValue: Int = 0
    protected var pokerChips = intArrayOf(0, 0, 0)
    protected var bet = intArrayOf(0, 0)
    protected var checkAvailable: Boolean = true
    protected var round = PRE_FLOP

    protected var gameSummaryMap: MutableList<List<String>> = ArrayList()
    protected var gameSummaryList: MutableList<String> = ArrayList()
    protected var gameNumber: Int = -1
    protected var playerName = arrayOf("Player", "Computer")

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()

    // Game UI state
    protected val mutableStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = mutableStateFlow.asStateFlow()

    private lateinit var cardDealer: Dealer
    private lateinit var odds: Odds
    lateinit var computerBot: Bot
    var computerBotValidActions = BooleanArray(4){false}

    /**
     * Handles fold request
     */
    abstract fun fold()

    /**
     * Handles check request
     */
    abstract fun check()

    /**
     * Handles call request
     */
    abstract fun call()

    /**
     * Handles bet request
     */
    abstract fun bet()

    /**
     * check if is player turn
     */
    abstract fun isPlayerTurn(): Boolean

    /**
     * check if player is dealer
     */
    abstract fun isPlayerDealer(): Boolean

    /**
     * Update player bet via button interaction
     */
    abstract fun updatePlayerBet(value: Int)

    /**
     * update game screen
     */
    abstract fun updateMutableStateValues()

    /**
     * Switch player turns
     */
    abstract fun switchPlayerTurn()

    /**
     * Reset all values before new game
     */
    private fun resetValues() {
        round = PRE_FLOP

        // reset values
        bet[PLAYER] = 0
        bet[BOT] = 0
        totalPotValue = 0
        checkAvailable = true

        // clear cards
        playerCards.clear()
        computerCards.clear()
        tableCards.clear()

        gameSummaryList.clear()
    }

    /**
     * Init values before new game
     */
    private fun initValues() {

        gameNumber++

        // init poker chips
        pokerChips[PLAYER] = uiState.value.playerMoney
        pokerChips[BOT] = uiState.value.computerMoney
        pokerChips[POT] = 0

        // init or change dealer
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
    }

    /**
     * Deal players and table cards
     */
    private fun dealCards() {
        cardDealer = Dealer()

        // set player and computer cards
        cardDealer.setPlayerCards(playerCards, computerCards)

        // set flop
        cardDealer.setFlopCards(tableCards)

        // set turn
        cardDealer.setTurnCard(tableCards)

        // set river
        cardDealer.setRiverCard(tableCards)
    }

    /**
     * Init and calculate odds
     */
    private fun initOdds() {

        odds = Odds(Combinations(tableCards.subList(0,3)).combinations)

        // calculate flop odds
        odds.calculateFlopOdds(computerCards, tableCards.subList(0,3))

        // calculate turn odds
        odds.calculateTurnOdds(computerCards, tableCards.subList(0,4))

        // calculate river odds
        odds.calculateRiverOdds(computerCards, tableCards)
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

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

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

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"

                updateMutableStateValues()
                player = dealer

                if (isPlayerTurn()) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false
                    )}
                } else {
                    computerBotValidActions[FOLD] = true
                    computerBotValidActions[CHECK] = false
                    computerBotValidActions[CALL] = true
                    computerBotValidActions[BET] = false
                    computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
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

            gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
            gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

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

            gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"
            gameSummaryList += "${playerName[blind]} pays big blind ${bet[blind]} €"

            updateMutableStateValues()
            player = dealer

            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = true
                computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
            }
        }
    }

    /**
     * Calculate winner
     */
    private fun calculateWinner() {
        mutableStateFlow.update { currentState -> currentState.copy(
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false,
            displayComputerCards = true,
            showdown = true
        )}

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        val computerHand = Hand(playerCards = computerCards, tableCards = tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)

        var playerHandString = ""
        playerHand.getHand().forEach {
            playerHandString += it.cardString()+" "
        }

        var computerHandString = ""
        computerHand.getHand().forEach {
            computerHandString += it.cardString()+" "
        }

        gameSummaryList += "${playerName[PLAYER]} hand:  $playerHandString - ${playerHand.resultText}"
        gameSummaryList += "${playerName[BOT]} hand:  $computerHandString - ${computerHand.resultText}"

        when (winnerCalculator.getWinner()) {
            PLAYER -> {
                pokerChips[PLAYER] += totalPotValue
                gameSummaryList += "${playerName[PLAYER]} wins $totalPotValue €"

                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 100 %",
                    computerText = "${computerHand.resultText} 0 %",
                    winnerText = "Player wins $totalPotValue €"
                )}
            }
            BOT -> {
                pokerChips[BOT] += totalPotValue
                gameSummaryList += "${playerName[BOT]} wins $totalPotValue €"
                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    computerText = "${computerHand.resultText} 100 %",
                    winnerText = "Computer wins $totalPotValue €"
                )}
            }
            else -> {
                pokerChips[PLAYER] += totalPotValue / 2
                pokerChips[BOT] += totalPotValue / 2
                gameSummaryList += "Split pot with value $totalPotValue €"

                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    computerText = "${computerHand.resultText} 0 %",
                    winnerText = "Draw, split $totalPotValue €"
                )}
            }
        }

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            playerMoney = pokerChips[PLAYER],
            computerMoney = pokerChips[BOT],
            currentPot = pokerChips[POT],
            playerBetValue = BIG_BLIND,
            totalPot = totalPotValue,
            gameSummary = gameSummaryMap
        )}

        viewModelScope.launch {
            delay(2000)
            if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
                newGame()
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

        var flopString = ""
        tableCards.subList(0,3).forEach { flopString += it.cardString()+" " }
        gameSummaryList.add("---- $flopString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayFlop = true,
            playerText = "${odds.getShowdownPlayerOdds()} %",
            computerText = "${odds.getShowdownOpponentOdds()} %",
            gameSummary = gameSummaryMap
        )}
    }

    private fun showdownTurn() {
        round = TURN

        odds.calculateShowdownTurnOdds(
            playerCards = playerCards,
            opponentCards = computerCards,
            tableCards = tableCards.subList(0, 4)
        )

        var turnString = ""
        tableCards.subList(0,4).forEach { turnString += it.cardString()+" " }
        gameSummaryList.add("---- $turnString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayTurn = true,
            playerText = "${odds.getShowdownPlayerOdds()} %",
            computerText = "${odds.getShowdownOpponentOdds()} %",
            gameSummary = gameSummaryMap
        )}
    }

    private fun showdownRiver() {
        round = RIVER

        var riverString = ""
        tableCards.forEach { riverString += it.cardString()+" " }
        gameSummaryList.add("---- $riverString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayRiver = true,
            gameSummary = gameSummaryMap
        )}
    }

    /**
     * Show all cards and calculate winner
     */
    protected fun showdown() {

        totalPotValue = pokerChips[POT]

        mutableStateFlow.update { currentState -> currentState.copy(
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
    protected fun nextRound() {

        if (player == dealer) {
            switchPlayerTurn()
        }

        totalPotValue += pokerChips[POT]
        bet[PLAYER] = 0
        bet[BOT] = 0
        checkAvailable = true

        updateMutableStateValues()

        when (round) {
            PRE_FLOP -> {
                round = FLOP

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFlop = true,
                    turnDelayTime = 0,
                    riverDelayTime = 1000,
                ) }

                var flopString = ""
                tableCards.subList(0,3).forEach { flopString += it.cardString()+" " }

                gameSummaryList.add("---- $flopString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = true
                    computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
                }
            }

            FLOP -> {
                round = TURN

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayTurn = true,
                    turnDelayTime = 0,
                    riverDelayTime = 0,
                )}
                var turnString = ""
                tableCards.subList(0,4).forEach { turnString += it.cardString()+" " }

                gameSummaryList.add("---- $turnString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = true
                    computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
                }
            }

            TURN -> {

                round = RIVER
                cardDealer.setRiverCard(tableCards)

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayRiver = true,
                )}

                var riverString = ""
                tableCards.forEach { riverString += it.cardString()+" " }

                gameSummaryList.add("---- $riverString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = true
                    computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
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
    protected fun newGame() {

        resetValues()
        initValues()
        dealCards()
        initOdds()
        computerBot = Bot(odds, computerCards.toList(), tableCards.toList(), !isPlayerDealer())

        gameSummaryList.add("Game ${gameNumber+1}")
        gameSummaryMap.add(gameNumber, gameSummaryList.toList())

        mutableStateFlow.update { currentState -> currentState.copy(
            displayComputerCards = false,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            playerBetValue = 0,
            computerBetValue = 0,
            totalPot = 0,
            currentPot = 0,
            winnerText = "",
            gameSummary = gameSummaryMap,
            showdown = false
        )}

        // pre flop bets
        preFlopBets()
    }
}