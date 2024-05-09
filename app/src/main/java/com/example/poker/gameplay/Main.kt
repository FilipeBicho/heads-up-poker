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
    var playerName = arrayOf("Player", "Computer")

    var playerCards = mutableStateListOf<Card>()
    var computerCards = mutableStateListOf<Card>()
    var tableCards = mutableStateListOf<Card>()

    // Game UI state
    val mutableStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = mutableStateFlow.asStateFlow()

    lateinit var odds: Odds
    lateinit var computerBot: Bot
    var computerBotValidActions = BooleanArray(6){false}

    abstract val game: Game
    abstract val showdown: Showdown

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
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    fun isBetAvailable(): Boolean {
        return bet[opponent] == 0 && pokerChips[player] > BIG_BLIND
    }

    /**
     * check if player is dealer
     */
    fun isPlayerDealer() = dealer == PLAYER

    /**
     * check if is player turn
     */
    fun isPlayerTurn() = player == PLAYER

    /**
     * raise is available if:
     *  - there is a previous bet
     *  - player chips and player bet (if any) is bigger than 2 times opponent bet
     */
    fun isRaiseAvailable(): Boolean {
        return bet[opponent] > 0 && pokerChips[player] + bet[player] > bet[opponent] * 2
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
                playerBetValue = if (bet[opponent] > 0) bet[opponent] * 2 else BIG_BLIND,
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
}