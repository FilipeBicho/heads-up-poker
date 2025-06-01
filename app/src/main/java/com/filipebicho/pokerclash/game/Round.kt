package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.actionText
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.init
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.odds
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.playerCards
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.data.Data.winnerCount
import com.filipebicho.pokerclash.hand.Hand
import com.filipebicho.pokerclash.hand.HandWinnerCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlin.concurrent.timerTask
import kotlinx.coroutines.launch

class Round(var coroutineScope: CoroutineScope) {

    fun flop(showdownCards: Boolean = false) {
        round = FLOP

        var flopCards = tableCards.subList(0,3)
        if (showdownCards) {
            odds.calculateShowdownFlopOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = flopCards
            )
            showdownCards()
        }

        updateStateFlowRound(tableCards = flopCards, round = round)
    }

    fun turn(showdownCards: Boolean = false) {
        round = TURN

        var turnCards = tableCards.subList(0,4)
        if (showdownCards) {
            odds.calculateShowdownTurnOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = turnCards
            )
            showdownCards()
        }
        updateStateFlowRound(tableCards = turnCards, round = round)
    }

    fun river(showdownCards: Boolean = false) {
        round = RIVER
        if (showdownCards)
            showdownCards()

        updateStateFlowRound(tableCards = tableCards, round = round)
    }

    fun showdownCards() {
        mainPot += roundPot
        roundPot = 0

        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = true,
            showdown = true,
            mainPot = mainPot,
            roundPot = roundPot
        )}

        coroutineScope.launch {
            delay(2000)
            when (round) {
                PRE_FLOP -> flop(true)
                FLOP -> turn(true)
                TURN -> river(true)
                RIVER -> calculateWinner()
            }
        }
    }

    fun calculateWinner() {
        uiStateFlow.update { currentState -> currentState.copy(
            isPlayerTurn = false,
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false,
            displayBotCards = true,
            showdown = true
        )}

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        val botHand = Hand(playerCards = botCards, tableCards = tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = botHand)
        val winner = winnerCalculator.getWinner()
        val playerName = uiStateFlow.value.name[PLAYER]
        val botName = uiStateFlow.value.name[BOT]

        var playerHandString = playerHand.getHand().joinToString(" ") { it.cardString() }
        var botHandString = botHand.getHand().joinToString(" ") { it.cardString() }

        gameSummaryList += "$playerName hand: $playerHandString - ${playerHand.resultText}"
        gameSummaryList += "$botName hand: $botHandString - ${botHand.resultText}"

        when (winner) {
            PLAYER -> {
                pokerChips[PLAYER] += mainPot
                actionText[PLAYER] = "wins $mainPot"
                gameSummaryList += "$playerName wins $mainPot"
            }
            BOT -> {
                pokerChips[BOT] += mainPot
                actionText[BOT] = "wins $mainPot"
                gameSummaryList += "$botName wins $mainPot"
            }
            else -> {
                pokerChips[PLAYER] += mainPot / 2
                pokerChips[BOT] += mainPot / 2
                actionText[PLAYER] = "wins ${mainPot / 2}"
                actionText[BOT] = "wins ${mainPot / 2}"
                gameSummaryList += "Split pot with value $mainPot"
            }
        }

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState -> currentState.copy(
            playerMoney = pokerChips[PLAYER],
            botMoney = pokerChips[BOT],
            actions = actionText,
            gameSummary = gameSummaryMap
        )}

        if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
            coroutineScope.launch {
                delay(2000)
                init.newGame()
            }
        } else {
            winnerCount[winner]++
            coroutineScope.launch {
                uiStateFlow.update { currentState -> currentState.copy(
                    newGame = true,
                    playerWins = winnerCount[PLAYER],
                    botWins = winnerCount[BOT]
                )}
                delay(4000)
            }
        }
    }

    private fun updateStateFlowRound(tableCards: List<Card>, round: Int)
    {
        var cardsString = tableCards.joinToString(" ") { it.cardString() }
        gameSummaryList.add("---- $cardsString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        uiStateFlow.update { currentState -> currentState.copy(
            displayFlop = round >= FLOP,
            displayTurn = round >= TURN,
            displayRiver = round == RIVER,
            gameSummary = gameSummaryMap,
            playerHandResult = playerHand.resultText
        )}
    }
}