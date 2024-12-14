package com.example.poker.game

import com.example.poker.POT
import com.example.poker.bot.NO_ACTION
import com.example.poker.cards.BOT
import com.example.poker.cards.Dealer
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.game.Data.action
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.betting
import com.example.poker.game.Data.blind
import com.example.poker.game.Data.botMoney
import com.example.poker.game.Data.cardDealer
import com.example.poker.game.Data.checkAvailable
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.dealer
import com.example.poker.game.Data.gameNumber
import com.example.poker.game.Data.gameSummaryList
import com.example.poker.game.Data.gameSummaryMap
import com.example.poker.game.Data.odds
import com.example.poker.game.Data.opponent
import com.example.poker.game.Data.player
import com.example.poker.game.Data.playerCards
import com.example.poker.game.Data.playerMoney
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import com.example.poker.game.Data.tableCards
import com.example.poker.game.Data.totalPotValue
import com.example.poker.game.Data.uiStateFlow
import com.example.poker.odds.Combinations
import com.example.poker.odds.Odds
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
            displayBotCards = true,
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
            showdown = false
        )}

        dealCards()
        initOdds()
        betting.preFlop()
    }
}