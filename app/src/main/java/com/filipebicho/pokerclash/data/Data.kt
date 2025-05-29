package com.filipebicho.pokerclash.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.bot.ChatgptBot
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.Dealer
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.game.Betting
import com.filipebicho.pokerclash.game.Init
import com.filipebicho.pokerclash.game.Round
import com.filipebicho.pokerclash.odds.Odds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.ArrayList

object Data {

    val uiStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = uiStateFlow.asStateFlow()

    // Classes
    lateinit var odds: Odds
    var init: Init = Init()
    var betting: Betting = Betting()
    lateinit var cardDealer: Dealer
    var showdown: Round = Round()
    val chatGptBot = ChatgptBot()

    // Bot models
    val botOptions = listOf(
        Pair("GPT 4o latest", "chatgpt-4o-latest"),
        Pair("GPT 4o", "gpt-4o"),
        Pair("GPT 4o mini", "gpt-4o-mini"),
        Pair("GPT 3 turbo", "gpt-3.5-turbo")
    )
    var botModel = ""

    // Actions
    var actionText : MutableList<String> = mutableListOf("", "")

    // Money
    var playerMoney = 1500
    var botMoney = 1500
    var pokerChips: MutableList<Int> = mutableListOf(0,0)
    var bet: MutableList<Int> = mutableListOf(0,0)
    var botLastRaise: Int = 0
    var roundPot: Int = 0
    var mainPot: Int = 0

    // Bet and turn type
    var action: Int = -1
    var checkAvailable: Boolean = true
    var round: Int = PRE_FLOP

    // Count variables
    var winnerCount: MutableList<Int> = mutableListOf(0,0)
    var gameNumber: Int = 0

    // Player positions
    var player: Int = -1
    var opponent: Int = -1
    var dealer: Int = -1
    var blind: Int = -1

    // Cards
    var playerCards: SnapshotStateList<Card> = mutableStateListOf()
    var botCards: SnapshotStateList<Card> = mutableStateListOf()
    var tableCards: SnapshotStateList<Card> = mutableStateListOf()

    // Summary
    var gameSummaryMap: MutableList<List<String>> = ArrayList()
    var gameSummaryList: MutableList<String> = mutableListOf()
}