package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.POT
import com.filipebicho.pokerclash.bot.NO_ACTION
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Dealer
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.game.Data.action
import com.filipebicho.pokerclash.game.Data.bet
import com.filipebicho.pokerclash.game.Data.betting
import com.filipebicho.pokerclash.game.Data.blind
import com.filipebicho.pokerclash.game.Data.botMoney
import com.filipebicho.pokerclash.game.Data.cardDealer
import com.filipebicho.pokerclash.game.Data.checkAvailable
import com.filipebicho.pokerclash.game.Data.botCards
import com.filipebicho.pokerclash.game.Data.dealer
import com.filipebicho.pokerclash.game.Data.gameNumber
import com.filipebicho.pokerclash.game.Data.gameSummaryList
import com.filipebicho.pokerclash.game.Data.gameSummaryMap
import com.filipebicho.pokerclash.game.Data.odds
import com.filipebicho.pokerclash.game.Data.opponent
import com.filipebicho.pokerclash.game.Data.player
import com.filipebicho.pokerclash.game.Data.playerCards
import com.filipebicho.pokerclash.game.Data.playerMoney
import com.filipebicho.pokerclash.game.Data.pokerChips
import com.filipebicho.pokerclash.game.Data.round
import com.filipebicho.pokerclash.game.Data.tableCards
import com.filipebicho.pokerclash.game.Data.totalPotValue
import com.filipebicho.pokerclash.game.Data.uiStateFlow
import com.filipebicho.pokerclash.odds.Combinations
import com.filipebicho.pokerclash.odds.Odds
import kotlinx.coroutines.flow.update

class Init {

    private fun dealCards() {
        cardDealer = Dealer()
        cardDealer.shuffle()
        cardDealer.setPlayerCards(playerCards, botCards)
        cardDealer.setFlopCards(tableCards)
        cardDealer.setTurnCard(tableCards)
        cardDealer.setRiverCard(tableCards)
    }

    private fun initOdds() {
        odds = Odds(Combinations(tableCards.subList(0,3)).combinations)
    }

    /**
     * Called at the begin of each new game iteration
     */
    private fun initValues() {
        round = PRE_FLOP

        action = NO_ACTION

        // players
        bet[PLAYER] = 0
        bet[BOT] = 0

        // pot
        pokerChips[POT] = 0
        bet[POT] = 0
        totalPotValue = 0

        // cards
        playerCards.clear()
        botCards.clear()
        tableCards.clear()

        checkAvailable = true
        gameSummaryList.clear()

        if (gameSummaryMap.isNotEmpty()) {
            gameNumber += 1
        }
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

        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = false,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            playerBetValue = 0,
            botBetValue = 0,
            totalPot = 0,
            currentPot = 0,
            actionText = "",
            playerText = "0 €",
            botText = "0 €",
            gameSummary = gameSummaryMap,
            displayBetButtons = player == PLAYER,
            showdown = false,
            newGame = false
        )}

        dealCards()
        initOdds()
        betting.preFlop()
    }
}