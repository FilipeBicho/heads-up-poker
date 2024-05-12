package com.example.poker.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.poker.GameUiState
import com.example.poker.POT
import com.example.poker.cards.BOT
import com.example.poker.cards.Card
import com.example.poker.cards.Dealer
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.gameplay.Game
import com.example.poker.odds.Combinations
import com.example.poker.odds.Odds
import kotlinx.coroutines.flow.MutableStateFlow      //  preFlopBets()
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Dealer {


    // gameplay state
    private val gameplayStateFlow = MutableStateFlow(GameplayState())
    private val gameplayState: StateFlow<GameplayState> = gameplayStateFlow.asStateFlow()

    // ui state
    private val uiStateFlow = MutableStateFlow(GameUiState())
    private val uiState: StateFlow<GameUiState> = uiStateFlow.asStateFlow()

    //
    private var dealer: Int = GameData.dealer
    private var blind: Int = GameData.blind
    private var gameNumber: Int = GameData.gameNumber

    private var pokerChips: MutableList<Int> = GameData.pokerChips

    private var gameSummaryList: MutableList<String> = GameData.gameSummaryList
    private var gameSummaryMap: MutableList<List<String>> = GameData.gameSummaryMap

    private var playerCards: SnapshotStateList<Card> = GameData.playerCards
    private var computerCards: SnapshotStateList<Card> = GameData.computerCards
    private var tableCards: SnapshotStateList<Card> = GameData.tableCards

    private var cardDealer: Dealer = Dealer()
    private var bet: Bet = GameData.Bet
    private lateinit var odds: Odds

    init {
        gameSummaryMap = uiState.value.gameSummary.toMutableList()
        gameNumber = gameplayState.value.gameNumber
        gameNumber++
    }

    private fun dealCards() {

        // set player and computer cards
        cardDealer.setPlayerCards(playerCards, computerCards)

        // set flop
        cardDealer.setFlopCards(tableCards)

        // set turn
        cardDealer.setTurnCard(tableCards)

        // set river
        cardDealer.setRiverCard(tableCards)

        GameData.playerCards = playerCards
        GameData.computerCards = computerCards
        GameData.tableCards = tableCards
    }

    private fun initOdds() {

        odds = Odds(Combinations(tableCards.subList(0,3)).combinations)

        // calculate flop odds
        odds.calculateFlopOdds(computerCards, tableCards.subList(0,3))

        // calculate turn odds
        odds.calculateTurnOdds(computerCards, tableCards.subList(0,4))

        // calculate river odds
        odds.calculateRiverOdds(computerCards, tableCards)

        GameData.odds = odds
    }

    private fun resetValues() {

        GameData.round = PRE_FLOP
        GameData.bet = mutableListOf(0,0,0)
        GameData.totalPotValue = 0
        GameData.checkAvailable = true
        GameData.gameSummaryList = gameSummaryList
    }

    private fun initValues() {

        // init poker chips
        pokerChips[PLAYER] = uiState.value.playerMoney
        pokerChips[BOT] = uiState.value.computerMoney
        pokerChips[POT] = 0

        // init or change dealer
        dealer = BOT
        blind = if (dealer == 0) 1 else 0

        GameData.pokerChips = pokerChips
        GameData.dealer = dealer
        GameData.player = dealer
        GameData.blind = blind
        GameData.opponent = blind
    }

    fun newGame() {
        resetValues()
        initValues()
        dealCards()
        initOdds()

        gameSummaryList.add("Game ${gameNumber+1}")
        gameSummaryMap.add(gameNumber, gameSummaryList.toList())

        uiStateFlow.update { currentState -> currentState.copy(
            displayComputerCards = true,
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

      bet.preFlop()
    }


}