package com.filipe.poker.game

import com.filipe.poker.POT
import com.filipe.poker.bot.NO_ACTION
import com.filipe.poker.cards.BOT
import com.filipe.poker.cards.Dealer
import com.filipe.poker.cards.PLAYER
import com.filipe.poker.cards.PRE_FLOP
import com.filipe.poker.game.Data.action
import com.filipe.poker.game.Data.bet
import com.filipe.poker.game.Data.betting
import com.filipe.poker.game.Data.blind
import com.filipe.poker.game.Data.botMoney
import com.filipe.poker.game.Data.cardDealer
import com.filipe.poker.game.Data.checkAvailable
import com.filipe.poker.game.Data.botCards
import com.filipe.poker.game.Data.dealer
import com.filipe.poker.game.Data.gameNumber
import com.filipe.poker.game.Data.gameSummaryList
import com.filipe.poker.game.Data.gameSummaryMap
import com.filipe.poker.game.Data.odds
import com.filipe.poker.game.Data.opponent
import com.filipe.poker.game.Data.player
import com.filipe.poker.game.Data.playerCards
import com.filipe.poker.game.Data.playerMoney
import com.filipe.poker.game.Data.pokerChips
import com.filipe.poker.game.Data.round
import com.filipe.poker.game.Data.tableCards
import com.filipe.poker.game.Data.totalPotValue
import com.filipe.poker.game.Data.uiStateFlow
import com.filipe.poker.odds.Combinations
import com.filipe.poker.odds.Odds
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
        odds.calculateFlopOdds(botCards, tableCards.subList(0,3))
        odds.calculateTurnOdds(botCards, tableCards.subList(0,4))
        odds.calculateRiverOdds(botCards, tableCards)
    }

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
            showdown = false,
            newGame = false
        )}

        dealCards()
        initOdds()
        betting.preFlop()
    }
}