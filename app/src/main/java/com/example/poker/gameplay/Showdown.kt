package com.example.poker.gameplay

import androidx.lifecycle.viewModelScope
import com.example.poker.BIG_BLIND
import com.example.poker.GameUiState
import com.example.poker.GameViewModel
import com.example.poker.POT
import com.example.poker.cards.BOT
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import com.example.poker.hand.Hand
import com.example.poker.hand.HandWinnerCalculator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Showdown(private var game: Betting) {

    private val mutableStateFlow = MutableStateFlow(GameUiState())

    private fun showdownFlop() {
        game.round = FLOP

        game.odds.calculateShowdownFlopOdds(
            playerCards = game.playerCards,
            opponentCards = game.computerCards,
            tableCards = game.tableCards.subList(0, 3)
        )

        var flopString = ""
        game.tableCards.subList(0,3).forEach { flopString += it.cardString()+" " }
        game.gameSummaryList.add("---- $flopString ----")
        game.gameSummaryMap[game.gameNumber] = game.gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayFlop = true,
            playerText = "${game.odds.getShowdownPlayerOdds()} %",
            computerText = "${game.odds.getShowdownOpponentOdds()} %",
            gameSummary = game.gameSummaryMap
        )}
    }

    private fun showdownTurn() {
        game.round = TURN

        game.odds.calculateShowdownTurnOdds(
            playerCards = game.playerCards,
            opponentCards = game.computerCards,
            tableCards = game.tableCards.subList(0, 4)
        )

        var turnString = ""
        game.tableCards.subList(0,4).forEach { turnString += it.cardString()+" " }
        game.gameSummaryList.add("---- $turnString ----")
        game.gameSummaryMap[game.gameNumber] = game.gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayTurn = true,
            playerText = "${game.odds.getShowdownPlayerOdds()} %",
            computerText = "${game.odds.getShowdownOpponentOdds()} %",
            gameSummary = game.gameSummaryMap
        )}
    }

    private fun showdownRiver() {
        game.round = RIVER

        var riverString = ""
        game.tableCards.forEach { riverString += it.cardString()+" " }
        game.gameSummaryList.add("---- $riverString ----")
        game.gameSummaryMap[game.gameNumber] = game.gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            displayRiver = true,
            gameSummary = game.gameSummaryMap
        )}
    }

    /**
     * Calculate winner
     */
    fun calculateWinner() {
        mutableStateFlow.update { currentState -> currentState.copy(
            displayFoldButton = false,
            displayCheckButton = false,
            displayCallButton = false,
            displayBetButton = false,
            displayComputerCards = true,
            showdown = true
        )}

        val playerHand = Hand(playerCards = game.playerCards, tableCards = game.tableCards)
        val computerHand = Hand(playerCards = game.computerCards, tableCards = game.tableCards)
        val winnerCalculator = HandWinnerCalculator(player1Hand = playerHand, player2Hand = computerHand)

        var playerHandString = ""
        playerHand.getHand().forEach {
            playerHandString += it.cardString()+" "
        }

        var computerHandString = ""
        computerHand.getHand().forEach {
            computerHandString += it.cardString()+" "
        }

        game.gameSummaryList += "${game.playerName[PLAYER]} hand:  $playerHandString - ${playerHand.resultText}"
        game.gameSummaryList += "${game.playerName[BOT]} hand:  $computerHandString - ${computerHand.resultText}"

        when (winnerCalculator.getWinner()) {
            PLAYER -> {
                game.pokerChips[PLAYER] += game.totalPotValue
                game.gameSummaryList += "${game.playerName[PLAYER]} wins $game.totalPotValue €"

                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 100 %",
                    computerText = "${computerHand.resultText} 0 %",
                    winnerText = "Player wins $game.totalPotValue €"
                )}
            }
            BOT -> {
                game.pokerChips[BOT] += game.totalPotValue
                game.gameSummaryList += "${game.playerName[BOT]} wins $game.totalPotValue €"
                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    computerText = "${computerHand.resultText} 100 %",
                    winnerText = "Computer wins $game.totalPotValue €"
                )}
            }
            else -> {
                game.pokerChips[PLAYER] += game.totalPotValue / 2
                game.pokerChips[BOT] += game.totalPotValue / 2
                game.gameSummaryList += "Split pot with value $game.totalPotValue €"

                mutableStateFlow.update { currentState -> currentState.copy(
                    playerText = "${playerHand.resultText} 0 %",
                    computerText = "${computerHand.resultText} 0 %",
                    winnerText = "Draw, split $game.totalPotValue €"
                )}
            }
        }

        game.gameSummaryMap[game.gameNumber] = game.gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            playerMoney = game.pokerChips[PLAYER],
            computerMoney = game.pokerChips[BOT],
            currentPot = game.pokerChips[POT],
            playerBetValue = BIG_BLIND,
            totalPot = game.totalPotValue,
            gameSummary = game.gameSummaryMap
        )}

        game.viewModelScope.launch {
            delay(2000)
            if (game.pokerChips[game.player] > 0 && game.pokerChips[game.opponent] > 0) {
                game.game.newGame()
            }
        }
    }

    /**
     * Show all cards and calculate winner
     */
    fun showdown() {
        mutableStateFlow.update { currentState -> currentState.copy(
            displayComputerCards = true,
            showdown = true,
            totalPot = game.totalPotValue
        )}

        when (game.round) {
            PRE_FLOP -> {
                showdownFlop()
                showdown()
            }

            FLOP -> {
                game.viewModelScope.launch {
                    delay(2000)
                    showdownTurn()
                    showdown()
                }
            }

            TURN -> {
                game.viewModelScope.launch {
                    delay(1000)
                    showdownRiver()
                    showdown()
                }
            }

            RIVER -> {
                game.viewModelScope.launch {
                    calculateWinner()
                }
            }
        }
    }
}