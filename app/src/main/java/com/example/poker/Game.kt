package com.example.poker

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.Bot
import com.example.poker.bot.CALL
import com.example.poker.bot.CHECK
import com.example.poker.bot.FOLD
import com.example.poker.bot.RAISE
import com.example.poker.cards.BOT
import com.example.poker.cards.Card
import com.example.poker.cards.Dealer
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import com.example.poker.gameplay.NewGame
import com.example.poker.gameplay.Showdown
import com.example.poker.hand.Hand
import com.example.poker.hand.HandWinnerCalculator
import com.example.poker.odds.Combinations
import com.example.poker.odds.Odds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class Game: ViewModel() {

    var player: Int = -1
    var opponent: Int = -1
    var dealer: Int = -1
    var blind: Int = -1
    var totalPotValue: Int = 0
    protected var betValue: Int = 0
    var pokerChips = intArrayOf(0, 0, 0)
    var bet = intArrayOf(0, 0)
    var checkAvailable: Boolean = true
    var round = PRE_FLOP

    var gameSummaryMap: MutableList<List<String>> = ArrayList()
    var gameSummaryList: MutableList<String> = ArrayList()
    var gameNumber: Int = -1
    protected var playerName = arrayOf("Player", "Computer")

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()

    // Game UI state
    val mutableStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = mutableStateFlow.asStateFlow()

    lateinit var cardDealer: Dealer
    lateinit var odds: Odds
    lateinit var computerBot: Bot
    lateinit var computerPreFlopBot: Bot
    var computerBotValidActions = BooleanArray(6){false}

    val newGame: NewGame = NewGame(this)
    val showdown: Showdown = Showdown(this)

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

    abstract fun raise()

    abstract fun allIn()

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
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    fun isBetAvailable(): Boolean {
        return bet[opponent] == 0 && pokerChips[player] > BIG_BLIND
    }

    /**
     * raise is available if:
     *  - there is a previous bet
     *  - player chips and player bet (if any) is bigger than 2 times opponent bet
     */
    fun isRaiseAvailable(): Boolean {
        return bet[opponent] > 0 && pokerChips[player] + bet[player] > bet[opponent] * 2
    }

    /**
     * all in is available if:
     *  - there is a previous bet:
     *      - player chips and player bet is equal or small than 2 times opponent bet
     *  - there is no previous bet:
     *      - player chips are smaller or equal to big bling
     */
    fun isAllInAvailable(): Boolean {
        return if (bet[opponent] > 0) {
            pokerChips[player] + bet[player] <= bet[opponent] * 2
        } else {
            pokerChips[player] <= BIG_BLIND
        }
    }

    /**
     * Get min bet value
     *  - there is a previous bet:
     *      - 2 times opponent bet
     *  - there is no previous bet:
     *      - Big blind
     */
    fun getMinBetValue(): Int {
        return if (bet[opponent] > 0) {
            bet[opponent] * 2
        } else {
            BIG_BLIND
        }
    }

    /**
     * update game screen
     */
    fun updateMutableStateValues() {

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState ->
            currentState.copy(
                playerText = "${bet[PLAYER]} €",
                computerText = "${bet[BOT]} €",
                playerMoney = pokerChips[PLAYER],
                computerMoney = pokerChips[BOT],
                playerBetValue = getMinBetValue(),
                currentPot = pokerChips[POT],
                totalPot = totalPotValue,
                gameSummary = gameSummaryMap
            )
        }
    }

    /**
     * Switch player turns
     */
    fun switchPlayerTurn() {
        player = if (player == PLAYER) BOT else PLAYER
        opponent = if (player == BOT) PLAYER else BOT
    }

    fun preFlopBets() {

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

                totalPotValue += pokerChips[POT]
                updateMutableStateValues()
                showdown.showdown()
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
                        displayBetButton = false,
                        displayRaiseButton = false,
                        displayAllInButton = false
                    )}
                } else {
                    computerBotValidActions[FOLD] = true
                    computerBotValidActions[CHECK] = false
                    computerBotValidActions[CALL] = true
                    computerBotValidActions[BET] = false
                    computerBotValidActions[RAISE] = false
                    computerBotValidActions[ALLIN] = false

                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        FOLD -> fold()
                        CALL -> call()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
                }
            }
        } else if (pokerChips[dealer] <= SMALL_BLIND) {

            // dealer makes all in
            bet[dealer] = pokerChips[dealer]
            pokerChips[dealer] = 0

            // blind pays all in
            bet[blind] = bet[player]
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]
            totalPotValue += pokerChips[POT]

            gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
            gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

            updateMutableStateValues()
            showdown.showdown()
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
                    displayBetButton = isBetAvailable(),
                    displayRaiseButton = isRaiseAvailable(),
                    displayAllInButton = isAllInAvailable()
                )}
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = isBetAvailable()
                computerBotValidActions[RAISE] = isRaiseAvailable()
                computerBotValidActions[ALLIN] = isAllInAvailable()

                when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                    FOLD -> fold()
                    CALL -> call()
                    BET -> {
                        betValue = computerBot.betValue
                        bet()
                    }
                    RAISE -> {
                        betValue = computerBot.betValue
                        raise()
                    }
                }
            }
        }
    }

    /**
     * Calculate winner
     */
    fun calculateWinner() {
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
                newGame.start()
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
                        displayBetButton = isBetAvailable(),
                        displayRaiseButton = isRaiseAvailable(),
                        displayAllInButton = isAllInAvailable()
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
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
                        displayBetButton = pokerChips[player] + bet[player] >= BIG_BLIND
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
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
                        displayBetButton = pokerChips[player] + bet[player] >= BIG_BLIND
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
                }
            }

            RIVER -> {
                calculateWinner()
            }
        }
    }
}