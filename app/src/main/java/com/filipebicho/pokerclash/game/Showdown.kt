package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.POT
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.init
import com.filipebicho.pokerclash.data.Data.odds
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.playerCards
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.totalPotValue
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.data.Data.winnerCount
import com.filipebicho.pokerclash.hand.Hand
import com.filipebicho.pokerclash.hand.HandWinnerCalculator
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
        totalPotValue += pokerChips[POT]

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
            displayBetButtons = false,
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

        gameSummaryList += "${uiStateFlow.value.name[PLAYER]} hand: $playerHandString - ${playerHand.resultText}"
        gameSummaryList += "${uiStateFlow.value.name[BOT]} hand: $computerHandString - ${computerHand.resultText}"

        when (winner) {
            PLAYER -> {
                pokerChips[PLAYER] += totalPotValue
                gameSummaryList += "${uiStateFlow.value.name[PLAYER]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerText = "${playerHand.resultText} 100 %",
                    botText = "${computerHand.resultText} 0 %",
                    actionText = "${uiStateFlow.value.name[PLAYER]} wins $totalPotValue €",
                    gameSummary = gameSummaryMap
                )}
            }
            BOT -> {
                pokerChips[BOT] += totalPotValue
                gameSummaryList += "${uiStateFlow.value.name[BOT]} wins $totalPotValue €"
                gameSummaryMap[gameNumber] = gameSummaryList.toList()

                uiStateFlow.update { currentState -> currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerText = "${playerHand.resultText} 0 %",
                    botText = "${computerHand.resultText} 100 %",
                    actionText = "${uiStateFlow.value.name[BOT]} wins $totalPotValue €",
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
                    actionText = "${uiStateFlow.value.name[winner]} wins the game",
                    playerText = "${uiStateFlow.value.name[PLAYER]} has ${winnerCount[PLAYER]} win(s)",
                    botText = "${uiStateFlow.value.name[BOT]} has ${winnerCount[BOT]} win(s)",
                    playerWins = winnerCount[PLAYER],
                    botWins = winnerCount[BOT]
                )}
            }, 4000)
        }
    }
}