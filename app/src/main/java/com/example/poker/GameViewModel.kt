package com.example.poker

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

open class GameViewModel : Game() {

    init {
        newGame()
    }

    /**
     * Handles fold request
     */
    override fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT] + totalPotValue

        gameSummaryList += "${playerName[player]} folds"
        gameSummaryList += "${playerName[opponent]} wins ${pokerChips[POT]} €"

        // update mutable state values
        updateMutableStateValues()

        // new game
        newGame()
    }

    /**
     * Handles check request
     */
    override fun check() {

        if (checkAvailable && round != PRE_FLOP) {

            checkAvailable = false
            switchPlayerTurn()

            gameSummaryList += "${playerName[player]} checks"

            updateMutableStateValues()

            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = false,
                    displayBetButton = true
                )}
            } else {
                computerBotValidActions[FOLD] = false
                computerBotValidActions[CHECK] = true
                computerBotValidActions[CALL] = false
                computerBotValidActions[BET] = true
                computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
            }
        } else {
            viewModelScope.launch {
                delay(1000)
                nextRound()
            }
        }
    }

    /**
     * Handles call request
     */
    override fun call() {

        val currentPlayerBet: Int = bet[player]

        if (pokerChips[player] <= bet[opponent]) {

            // player makes all in
            bet[player] += pokerChips[player]
            pokerChips[player] = 0

            // opponent equals player all in
            bet[opponent] = bet[player]
            pokerChips[opponent] = pokerChips[opponent] + bet[opponent] - bet[player]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${playerName[player]} calls ${bet[player]} €"

            updateMutableStateValues()
            showdown()
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= abs(currentPlayerBet - bet[player])

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${playerName[player]} calls ${bet[player]} €"

            updateMutableStateValues()
            switchPlayerTurn()

            if (checkAvailable && round == PRE_FLOP) {
                if (isPlayerTurn()) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = true
                    computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
                }

            } else {
                if (pokerChips[player] > 0 && pokerChips[opponent] > 0) {
                    nextRound()
                } else {
                    showdown()
                }
            }
        }
    }

    /**
     * Handles bet request
     */
    override fun bet() {

        checkAvailable = false

        val oldBet: Int = bet[player]

        // player makes a bet
        if (betValue > bet[opponent] + BIG_BLIND) {
            bet[player] = betValue
        } else {
            bet[player] = betValue + bet[opponent]
        }

        if (bet[player] > pokerChips[player]) {
            bet[player] = pokerChips[player] + oldBet
        }

        // if all in add old bet
        if (pokerChips[player] == bet[player]) {
            bet[player] += oldBet
        }

        pokerChips[player] = pokerChips[player] - bet[player] + oldBet

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${playerName[player]} bets ${bet[player]} €"

        updateMutableStateValues()
        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {

            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = false
                )}
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = false
                computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
            }

        } else {
            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = true
                )}
            } else {
                betValue = BIG_BLIND

                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = true
                computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)
            }
        }
    }

    /**
     * update game screen
     */
    override fun updateMutableStateValues() {

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState -> currentState.copy(
            playerText = "${bet[PLAYER]} €",
            computerText = "${bet[COMPUTER]} €",
            playerMoney = pokerChips[PLAYER],
            computerMoney = pokerChips[COMPUTER],
            currentPot = pokerChips[POT],
            playerBetValue = BIG_BLIND,
            totalPot = totalPotValue,
            gameSummary = gameSummaryMap
        )}
    }

    /**
     * Switch player turns
     */
    override fun switchPlayerTurn() {
        player = if (player == PLAYER) COMPUTER else PLAYER
        opponent = if (player == COMPUTER) PLAYER else COMPUTER
    }

    /**
     * check if is player turn
     */
    override fun isPlayerTurn() = player == PLAYER

    /**
     * check if player is dealer
     */
    override fun isPlayerDealer() = dealer == PLAYER

    /**
     * Update player bet via button interaction
     */
    override fun updatePlayerBet(value: Int) {

        betValue = if (value > pokerChips[player]) {
            pokerChips[player]
        } else {
            value
        }

        mutableStateFlow.update { currentState -> currentState.copy(
            playerBetValue = betValue
        ) }
    }
}
