package com.example.poker.gameplay

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poker.BIG_BLIND
import com.example.poker.GameUiState
import com.example.poker.POT
import com.example.poker.bot.Bot
import com.example.poker.cards.BOT
import com.example.poker.cards.Card
import com.example.poker.cards.Dealer
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.hand.Hand
import com.example.poker.hand.HandWinnerCalculator
import com.example.poker.odds.Odds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class Main : ViewModel() {

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

    abstract val newGame: NewGame
    abstract val showdown: Showdown;

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
    private fun getMinBetValue(): Int {
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
     * check if is player turn
     */
    fun isPlayerTurn() = player == PLAYER

    /**
     * check if player is dealer
     */
    fun isPlayerDealer() = dealer == PLAYER

    /**
     * Switch player turns
     */
    fun switchPlayerTurn() {
        player = if (player == PLAYER) BOT else PLAYER
        opponent = if (player == BOT) PLAYER else BOT
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
}