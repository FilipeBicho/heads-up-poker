package com.example.poker.gameplay

import androidx.lifecycle.viewModelScope
import com.example.poker.GameUiState
import com.example.poker.GameViewModel
import com.example.poker.cards.FLOP
import com.example.poker.cards.PRE_FLOP
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Showdown(private var game: GameViewModel) {

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
                    game.calculateWinner()
                }
            }
        }
    }
}