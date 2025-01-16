package com.filipe.poker.game

import com.filipe.poker.BIG_BLIND
import com.filipe.poker.POT
import com.filipe.poker.cards.BOT
import com.filipe.poker.cards.FLOP
import com.filipe.poker.cards.PLAYER
import com.filipe.poker.cards.PRE_FLOP
import com.filipe.poker.cards.RIVER
import com.filipe.poker.cards.TURN
import com.filipe.poker.game.Data.botCards
import com.filipe.poker.game.Data.gameNumber
import com.filipe.poker.game.Data.gameSummaryList
import com.filipe.poker.game.Data.gameSummaryMap
import com.filipe.poker.game.Data.init
import com.filipe.poker.game.Data.name
import com.filipe.poker.game.Data.odds
import com.filipe.poker.game.Data.opponent
import com.filipe.poker.game.Data.player
import com.filipe.poker.game.Data.playerCards
import com.filipe.poker.game.Data.pokerChips
import com.filipe.poker.game.Data.round
import com.filipe.poker.game.Data.tableCards
import com.filipe.poker.game.Data.totalPotValue
import com.filipe.poker.game.Data.uiStateFlow
import com.filipe.poker.game.Data.winnerCount
import com.filipe.poker.hand.Hand
import com.filipe.poker.hand.HandWinnerCalculator
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.timerTask

class Showdown {

    fun flop(showdownCards: Boolean = false) {
        round = FLOP

        var flopString = ""
        tableCards.subList(0,3).forEach { flopString += it.cardString()+" " }
        gameSummaryList.add("---- $flopString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        if (showdownCards) {
            odds.calculateShowdownFlopOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = tableCards.subList(0, 3)
            )

            uiStateFlow.update { currentState -> currentState.copy(
                displayFlop = true,
                playerText = "${odds.getShowdownPlayerOdds()} %",
                botText = "${odds.getShowdownOpponentOdds()} %",
                gameSummary = gameSummaryMap
            )}
            showdownCards()
        } else {
            uiStateFlow.update { currentState -> currentState.copy(
                displayFlop = true,
                gameSummary = gameSummaryMap
            )}
        }
    }

    fun turn(showdownCards: Boolean = false) {
        round = TURN

        var turnString = ""
        tableCards.subList(0,4).forEach { turnString += it.cardString()+" " }
        gameSummaryList.add("---- $turnString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        if (showdownCards) {
            odds.calculateShowdownTurnOdds(
                playerCards = playerCards,
                opponentCards = botCards,
                tableCards = tableCards.subList(0, 4)
            )

            uiStateFlow.update { currentState -> currentState.copy(
                displayTurn = true,
                playerText = "${odds.getShowdownPlayerOdds()} %",
                botText = "${odds.getShowdownOpponentOdds()} %",
                gameSummary = gameSummaryMap
            )}

            showdownCards()
        } else {
            uiStateFlow.update { currentState -> currentState.copy(
                displayTurn = true,
                gameSummary = gameSummaryMap
            )}
        }
    }

    fun river(showdownCards: Boolean = false) {
        round = RIVER

        var riverString = ""
        tableCards.forEach { riverString += it.cardString()+" " }
        gameSummaryList.add("---- $riverString ----")
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        if (showdownCards) {
            uiStateFlow.update { currentState -> currentState.copy(
                displayRiver = true,
                playerText = "${odds.getShowdownPlayerOdds()} %",
                botText = "${odds.getShowdownOpponentOdds()} %",
                gameSummary = gameSummaryMap
            )}

            showdownCards()
        } else {
            uiStateFlow.update { currentState -> currentState.copy(
                displayRiver = true,
                gameSummary = gameSummaryMap
            )}

        }
    }

    fun showdownCards() {
        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = true,
            showdown = true,
            totalPot = totalPotValue
        )}

        when (round) {
            PRE_FLOP -> Timer().schedule(timerTask {flop(true)}, 2000)
            FLOP -> Timer().schedule(timerTask {turn(true)}, 2000)
            TURN -> Timer().schedule(timerTask {river(true)}, 2000)
            RIVER -> calculateWinner()
        }
    }

    /**
     * Calculate winner
     */
    fun calculateWinner() {
        uiStateFlow.update { currentState -> currentState.copy(
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false,
            displayAllInButton = false,
            displayBotCards = true,
            showdown = true
        )}

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        val computerHand = Hand(playerCards = botCards, tableCards = tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)
        val winner = winnerCalculator.getWinner()

        var playerHandString = ""
        playerHand.getHand().forEach {
            playerHandString += it.cardString()+" "
        }

        var computerHandString = ""
        computerHand.getHand().forEach {
            computerHandString += it.cardString()+" "
        }

        gameSummaryList += "${name[PLAYER]} hand: $playerHandString - ${playerHand.resultText}"
        gameSummaryList += "${name[BOT]} hand: $computerHandString - ${computerHand.resultText}"

        when (winner) {
            PLAYER -> {
                pokerChips[PLAYER] += totalPotValue
                gameSummaryList += "${name[PLAYER]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerText = "${playerHand.resultText} 100 %",
                    botText = "${computerHand.resultText} 0 %",
                    actionText = "${name[PLAYER]} wins $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
            BOT -> {
                pokerChips[BOT] += totalPotValue
                gameSummaryList += "${name[BOT]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerText = "${playerHand.resultText} 0 %",
                    botText = "${computerHand.resultText} 100 %",
                    actionText = "${name[BOT]} wins $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
            else -> {
                pokerChips[PLAYER] += totalPotValue / 2
                pokerChips[BOT] += totalPotValue / 2
                gameSummaryList += "Split pot with value $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    botText = "${computerHand.resultText} 0 %",
                    actionText = "Draw, split $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
        }

        if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
            Timer().schedule(timerTask {
                init.newGame()
            }, 2000)
        } else {
            winnerCount[winner]++
            Timer().schedule(timerTask {
                uiStateFlow.update { currentState -> currentState.copy(
                    newGame = true,
                    actionText = "${name[winner]} wins the game",
                    playerText = "${name[PLAYER]} has ${winnerCount[PLAYER]} win(s)",
                    botText = "${name[BOT]} has ${winnerCount[BOT]} win(s)",
                    playerWins = winnerCount[PLAYER],
                    botWins = winnerCount[BOT]
                )}
            }, 4000)
        }
    }
}