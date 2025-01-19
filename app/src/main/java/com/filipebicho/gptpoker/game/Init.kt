package com.filipebicho.gptpoker.game

import com.filipebicho.gptpoker.POT
import com.filipebicho.gptpoker.bot.NO_ACTION
import com.filipebicho.gptpoker.cards.BOT
import com.filipebicho.gptpoker.cards.Dealer
import com.filipebicho.gptpoker.cards.PLAYER
import com.filipebicho.gptpoker.cards.PRE_FLOP
import com.filipebicho.gptpoker.game.Data.action
import com.filipebicho.gptpoker.game.Data.bet
import com.filipebicho.gptpoker.game.Data.betting
import com.filipebicho.gptpoker.game.Data.blind
import com.filipebicho.gptpoker.game.Data.botMoney
import com.filipebicho.gptpoker.game.Data.cardDealer
import com.filipebicho.gptpoker.game.Data.checkAvailable
import com.filipebicho.gptpoker.game.Data.botCards
import com.filipebicho.gptpoker.game.Data.dealer
import com.filipebicho.gptpoker.game.Data.gameNumber
import com.filipebicho.gptpoker.game.Data.gameSummaryList
import com.filipebicho.gptpoker.game.Data.gameSummaryMap
import com.filipebicho.gptpoker.game.Data.odds
import com.filipebicho.gptpoker.game.Data.opponent
import com.filipebicho.gptpoker.game.Data.player
import com.filipebicho.gptpoker.game.Data.playerCards
import com.filipebicho.gptpoker.game.Data.playerMoney
import com.filipebicho.gptpoker.game.Data.pokerChips
import com.filipebicho.gptpoker.game.Data.round
import com.filipebicho.gptpoker.game.Data.tableCards
import com.filipebicho.gptpoker.game.Data.totalPotValue
import com.filipebicho.gptpoker.game.Data.uiStateFlow
import com.filipebicho.gptpoker.odds.Combinations
import com.filipebicho.gptpoker.odds.Odds
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