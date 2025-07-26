package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.actionHistory
import com.filipebicho.pokerclash.data.Data.actionPlayer
import com.filipebicho.pokerclash.data.Data.actionText
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.botWins
import com.filipebicho.pokerclash.data.Data.currentBot
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.init
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.odds
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.playerCards
import com.filipebicho.pokerclash.data.Data.playerWins
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.stats
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.hand.Hand
import com.filipebicho.pokerclash.hand.HandWinnerCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Round(var coroutineScope: CoroutineScope) {

    fun flop(showdownCards: Boolean = false) {
        round = FLOP

        mainPot += roundPot
        roundPot = 0

        var flopCards = tableCards.subList(0,3)
        if (showdownCards) {
            odds.calculateShowdownFlopOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = flopCards
            )

            uiStateFlow.update { currentState -> currentState.copy(
                playerOdds = odds.getShowdownPlayerOdds(),
                botOdds = odds.getShowdownBotOdds()
            )}
            showdownCards()
        }

        updateStateFlowRound(tableCards = flopCards, round = round)
    }

    fun turn(showdownCards: Boolean = false) {
        round = TURN

        mainPot += roundPot
        roundPot = 0

        var turnCards = tableCards.subList(0,4)
        if (showdownCards) {
            odds.calculateShowdownTurnOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = turnCards
            )

            uiStateFlow.update { currentState -> currentState.copy(
                playerOdds = odds.getShowdownPlayerOdds(),
                botOdds = odds.getShowdownBotOdds()
            )}

            showdownCards()
        }
        updateStateFlowRound(tableCards = turnCards, round = round)
    }

    fun river(showdownCards: Boolean = false) {
        round = RIVER

        mainPot += roundPot
        roundPot = 0

        if (showdownCards) {
            odds.calculateShowdownRiverOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = tableCards,
            )

            uiStateFlow.update { currentState -> currentState.copy(
                playerOdds = odds.getShowdownPlayerOdds(),
                botOdds = odds.getShowdownBotOdds(),
            )}

            showdownCards()
        }

        updateStateFlowRound(tableCards = tableCards, round = round)
    }

    fun showdownCards() {
        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = true,
            isPlayerTurn = false,
            showdown = true,
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

        mainPot += roundPot
        roundPot = 0

        uiStateFlow.update { currentState -> currentState.copy(
            isPlayerTurn = false,
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false,
            displayBotCards = true,
            showdown = true,
            roundPot = roundPot,
            mainPot = mainPot,
            playerBet = 0,
            botBet = 0,
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

        actionHistory += if (winner == PLAYER)
            "Showdown: ${actionPlayer[BOT]} lost"
        else
            "Showdown: ${actionPlayer[BOT]} won"


        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState -> currentState.copy(
            playerMoney = pokerChips[PLAYER],
            botMoney = pokerChips[BOT],
            actions = actionText,
            gameSummary = gameSummaryMap,
            displayGameResult = true,
            winner = winner,
            winningHand = if (winner == PLAYER) playerHand else botHand,
            displayPot = false
        )}

        if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
            coroutineScope.launch {
                delay(4000)
                stats.updateStatsAfterHand()
                init.newGame()
            }
        } else {

            if (winner == PLAYER) {
                playerWins++
            } else {
                botWins[currentBot]++
            }

            coroutineScope.launch {
                delay(4000)
                uiStateFlow.update { currentState -> currentState.copy(
                    newGame = true,
                    playerWins = playerWins,
                    botWins = botWins[currentBot],
                    showdown = false,
                    displayFlop = false,
                    displayTurn = false,
                    displayRiver = false,
                    displayBotCards = false,
                    displayPlayerCards = false,
                    displayPot = false,
                    displayGameResult = false
                )}
            }
        }
    }

    private fun updateStateFlowRound(tableCards: List<Card>, round: Int)
    {
        val cardsString = tableCards.joinToString(" ") { it.cardString() }
        gameSummaryList.add("---- $cardsString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        uiStateFlow.update { currentState -> currentState.copy(
            displayFlop = round >= FLOP,
            displayTurn = round >= TURN,
            displayRiver = round == RIVER,
            gameSummary = gameSummaryMap,
            playerHandResult = playerHand.resultText,
            mainPot = mainPot,
            roundPot = roundPot
        )}
    }
}