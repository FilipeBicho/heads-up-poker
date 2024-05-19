package com.example.poker.game

import com.example.poker.BIG_BLIND
import com.example.poker.POT
import com.example.poker.cards.BOT
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.gameNumber
import com.example.poker.game.Data.gameSummaryList
import com.example.poker.game.Data.gameSummaryMap
import com.example.poker.game.Data.init
import com.example.poker.game.Data.name
import com.example.poker.game.Data.odds
import com.example.poker.game.Data.opponent
import com.example.poker.game.Data.player
import com.example.poker.game.Data.playerCards
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import com.example.poker.game.Data.tableCards
import com.example.poker.game.Data.totalPotValue
import com.example.poker.game.Data.uiStateFlow
import com.example.poker.hand.Hand
import com.example.poker.hand.HandWinnerCalculator
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
            Timer().schedule(timerTask {
                showdownCards()
            }, 2000)

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

            Timer().schedule(timerTask {
                showdownCards()
            }, 2000)
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

        uiStateFlow.update { currentState -> currentState.copy(
            displayRiver = true,
            gameSummary = gameSummaryMap
        )}

        Timer().schedule(timerTask {
            showdownCards()
        }, 2000)
    }

    fun showdownCards() {
        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = true,
            showdown = true,
            totalPot = totalPotValue
        )}

        when (round) {
            PRE_FLOP -> flop(true)
            FLOP -> turn(true)
            TURN -> river(true)
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
            displayBotCards = true,
            showdown = true
        )}

        val playerHand = Hand(playerCards = playerCards, tableCards = tableCards)
        val computerHand = Hand(playerCards = botCards, tableCards = tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)

        var playerHandString = ""
        playerHand.getHand().forEach {
            playerHandString += it.cardString()+" "
        }

        var computerHandString = ""
        computerHand.getHand().forEach {
            computerHandString += it.cardString()+" "
        }

        gameSummaryList += "${name[PLAYER]} hand:  $playerHandString - ${playerHand.resultText}"
        gameSummaryList += "${name[BOT]} hand:  $computerHandString - ${computerHand.resultText}"

        when (winnerCalculator.getWinner()) {
            PLAYER -> {
                pokerChips[PLAYER] += totalPotValue
                gameSummaryList += "${name[PLAYER]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 100 %",
                    botText = "${computerHand.resultText} 0 %",
                    winnerText = "Player wins $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
            BOT -> {
                pokerChips[BOT] += totalPotValue
                gameSummaryList += "${name[BOT]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    botText = "${computerHand.resultText} 100 %",
                    winnerText = "Computer wins $totalPotValue €",
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
                    winnerText = "Draw, split $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
        }

        if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
            Timer().schedule(timerTask {
                init.newGame()
            }, 2000)
        }
    }
}