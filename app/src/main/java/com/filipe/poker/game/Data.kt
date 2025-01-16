package com.filipe.poker.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.filipe.poker.BIG_BLIND
import com.filipe.poker.GameUiState
import com.filipe.poker.bot.ChatgptBot
import com.filipe.poker.cards.Card
import com.filipe.poker.cards.Dealer
import com.filipe.poker.cards.PRE_FLOP
import com.filipe.poker.odds.Odds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object Data {

    val uiStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = uiStateFlow.asStateFlow()

    val name: List<String> = listOf(uiStateFlow.value.playerName, uiStateFlow.value.botName)

    var playerMoney = 1500
    var botMoney = 1500
    var minPlayerBet = BIG_BLIND

    var pokerChips: MutableList<Int> = mutableListOf(0,0,0)
    var bet: MutableList<Int> = mutableListOf(0,0,0)
    var totalPotValue: Int = 0
    var action: Int = -1
    var winnerCount: MutableList<Int> = mutableListOf(0,0)

    var gameNumber: Int = 0
    var checkAvailable: Boolean = true
    var round: Int = PRE_FLOP

    var player: Int = -1
    var opponent: Int = -1
    var dealer: Int = -1
    var blind: Int = -1

    var botValidActions = BooleanArray(6){false}

    var playerCards: SnapshotStateList<Card> = mutableStateListOf()
    var botCards: SnapshotStateList<Card> = mutableStateListOf()
    var tableCards: SnapshotStateList<Card> = mutableStateListOf()

    var gameSummaryMap: MutableList<List<String>> = ArrayList()
    var gameSummaryList: MutableList<String> = mutableListOf()

    lateinit var odds: Odds
    var init: Init = Init()
    var betting: Betting = Betting()
    lateinit var cardDealer: Dealer
    var showdown: Showdown = Showdown()

    val chatGptBot = ChatgptBot()
}