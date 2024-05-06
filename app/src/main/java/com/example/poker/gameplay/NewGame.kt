package com.example.poker.gameplay

import com.example.poker.GameUiState
import com.example.poker.GameViewModel
import com.example.poker.POT
import com.example.poker.bot.Bot
import com.example.poker.cards.BOT
import com.example.poker.cards.Dealer
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.odds.Combinations
import com.example.poker.odds.Odds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressWarnings("LeakingThisInConstructor")
class NewGame(private var game: GameViewModel) {

    private val mutableStateFlow = MutableStateFlow(GameUiState())
    private val uiState: StateFlow<GameUiState> = mutableStateFlow.asStateFlow()

    private fun initValues() {

        game.gameNumber++

        // init poker chips
        game.pokerChips[PLAYER] = uiState.value.playerMoney
        game.pokerChips[BOT] = uiState.value.computerMoney
        game.pokerChips[POT] = 0

        // init or change dealer
        game.dealer = BOT

        // init blind turn
        game.blind = if (game.dealer == 0) 1 else 0

        // set turns
        game.player = game.dealer
        game.opponent = game.blind
    }

    private fun resetValues() {
        game.round = PRE_FLOP

        // reset values
        game.bet[PLAYER] = 0
        game.bet[BOT] = 0
        game.totalPotValue = 0
        game.checkAvailable = true

        // clear cards
        game.playerCards.clear()
        game.computerCards.clear()
        game.tableCards.clear()

        game.gameSummaryList.clear()

    }

    /**
     * Deal players and table cards
     */
    private fun dealCards() {
        game.cardDealer = Dealer()

        // set player and computer cards
        game.cardDealer.setPlayerCards(game.playerCards, game.computerCards)

        // set flop
        game.cardDealer.setFlopCards(game.tableCards)

        // set turn
        game.cardDealer.setTurnCard(game.tableCards)

        // set river
        game.cardDealer.setRiverCard(game.tableCards)
    }

    /**
     * Init and calculate odds
     */
    private fun initOdds() {

        game.odds = Odds(Combinations(game.tableCards.subList(0,3)).combinations)

        // calculate flop odds
        game.odds.calculateFlopOdds(game.computerCards, game.tableCards.subList(0,3))

        // calculate turn odds
        game.odds.calculateTurnOdds(game.computerCards, game.tableCards.subList(0,4))

        // calculate river odds
        game.odds.calculateRiverOdds(game.computerCards, game.tableCards)
    }

    fun start() {
        resetValues()
        initValues()
        dealCards()
        initOdds()
        game.computerBot = Bot(game.computerCards.toList(), !game.isPlayerDealer())

        game.gameSummaryList.add("Game ${game.gameNumber+1}")
        game.gameSummaryMap.add(game.gameNumber, game.gameSummaryList.toList())

        mutableStateFlow.update { currentState -> currentState.copy(
            displayComputerCards = true,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            playerBetValue = 0,
            computerBetValue = 0,
            totalPot = 0,
            currentPot = 0,
            winnerText = "",
            gameSummary = game.gameSummaryMap,
            showdown = false
        )}

        game.preFlopBets()
    }


}