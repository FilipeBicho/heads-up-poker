package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.bot.NO_ACTION
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Dealer
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.betting
import com.filipebicho.pokerclash.data.Data.blind
import com.filipebicho.pokerclash.data.Data.botMoney
import com.filipebicho.pokerclash.data.Data.cardDealer
import com.filipebicho.pokerclash.data.Data.checkAvailable
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.odds
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.playerCards
import com.filipebicho.pokerclash.data.Data.playerMoney
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.odds.Combinations
import com.filipebicho.pokerclash.odds.Odds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class Init(coroutineScope: CoroutineScope) {

    init {
        betting = Betting(coroutineScope)
    }

    private fun dealCards() {
        round = PRE_FLOP

        playerCards.clear()
        botCards.clear()
        tableCards.clear()

        cardDealer = Dealer()
        cardDealer.shuffle()
        cardDealer.setPlayerCards(playerCards, botCards)
        cardDealer.setFlopCards(tableCards)
        cardDealer.setTurnCard(tableCards)
        cardDealer.setRiverCard(tableCards)

        odds = Odds(Combinations(tableCards.subList(0,3)).combinations)

        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = false,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            showdown = false,
            playerCards = playerCards.toList(),
            botCards = botCards.toList(),
            tableCards = tableCards.toList()
        )}
    }

    /**
     * Called at the begin of each new game iteration
     */
    private fun initValues() {
        action = NO_ACTION

        // players
        bet[PLAYER] = 0
        bet[BOT] = 0

        // pot
        roundPot = 0
        mainPot = 0

        checkAvailable = true
        gameSummaryList.clear()

        if (gameSummaryMap.isNotEmpty())
            gameNumber += 1

        gameSummaryList.add("Game ${gameNumber+1}")
        gameSummaryMap.add(gameNumber, gameSummaryList.toList())

        // init or change dealer
        dealer = if (dealer == -1) {
            (0..1).random()
        } else {
            if (dealer == 0) 1 else 0
        }
        blind = if (dealer == 0) 1 else 0
        player = dealer
        opponent = blind

        uiStateFlow.update { currentState -> currentState.copy(
            playerBet = 0,
            botBet = 0,
            playerMinRaise = 0,
            mainPot = 0,
            roundPot = 0,
            gameSummary = gameSummaryMap,
            playerHandResult = "",
            playerOdds = -1,
            botOdds = -1,
            isPlayerTurn = player == PLAYER,
            newGame = false,
            dealer = dealer,
        )}
    }

    /**
     * Called at the begin of a new game
     */
    fun initGame() {
        pokerChips[PLAYER] = playerMoney
        pokerChips[BOT] = botMoney
        newGame()
    }

    fun newGame() {
        initValues()
        dealCards()
        betting.preFlop()
    }
}