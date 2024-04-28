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
                mutableStateFlow.update { currentState ->
                    currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = true
                    )
                }
            } else {
                computerBotValidActions[FOLD] = false
                computerBotValidActions[CHECK] = true
                computerBotValidActions[CALL] = false
                computerBotValidActions[BET] = true
                when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                    CHECK -> check()
                    BET -> {
                        betValue = computerBot.betValue
                        bet()
                    }
                }
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

        val currentPlayerBet: Int = bet[opponent]

        if (pokerChips[player] <= bet[opponent]) {

            // player makes all in
            bet[player] += pokerChips[player]
            pokerChips[player] = 0

            if (pokerChips[opponent] > 0) {
                // opponent equals player all in
                pokerChips[opponent] = pokerChips[opponent] + bet[opponent] // reset opponent poker chips
                bet[opponent] = bet[player]
                pokerChips[opponent] -= bet[opponent]
            }

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]
            totalPotValue += pokerChips[POT]

            gameSummaryList += "${playerName[player]} calls ${bet[player]} €"

            updateMutableStateValues()
            showdown()
        } else {

            val callValue = abs(currentPlayerBet - bet[blind])

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= callValue

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${playerName[player]} calls $callValue €"

            updateMutableStateValues()
            switchPlayerTurn()

            if (pokerChips[player] == 0 || pokerChips[opponent] == 0) {
                totalPotValue += pokerChips[POT]
                showdown()
            } else {
                if (checkAvailable && round == PRE_FLOP) {
                    if (isPlayerTurn()) {
                        mutableStateFlow.update { currentState ->
                            currentState.copy(
                                displayFoldButton = false,
                                displayCheckButton = true,
                                displayCallButton = false,
                                displayBetButton = true
                            )
                        }
                    } else {
                        computerBotValidActions[FOLD] = false
                        computerBotValidActions[CHECK] = true
                        computerBotValidActions[CALL] = false
                        computerBotValidActions[BET] = true
                        when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                            CHECK -> check()
                            BET -> {
                                betValue = computerBot.betValue
                                bet()
                            }
                        }
                    }
                } else {
                    if (round == RIVER) {
                        totalPotValue += pokerChips[POT]
                        showdown()
                    } else {
                        nextRound()
                    }
                }
            }
        }
    }

    /**
     * Handles bet request
     */
    override fun bet() {

        if (betValue == 0) {
            betValue = BIG_BLIND
        }

        checkAvailable = false

        val oldBet: Int = bet[player]

        // player makes a bet
        if (betValue > bet[opponent] + BIG_BLIND) {
            bet[player] = betValue
        } else {
            bet[player] = betValue + bet[opponent]
        }

        if (bet[player] >= pokerChips[player] + oldBet) {
            bet[player] = oldBet + pokerChips[player]
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
                mutableStateFlow.update { currentState ->
                    currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false
                    )
                }
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = false
                when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                    FOLD -> fold()
                    CALL -> call()
                }
            }

        } else {
            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState ->
                    currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = true
                    )
                }
            } else {
                betValue = BIG_BLIND

                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = true
                when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                    FOLD -> fold()
                    CALL -> call()
                    BET -> {
                        betValue = computerBot.betValue
                        bet()
                    }
                }
            }
        }
    }

    /**
     * update game screen
     */
    override fun updateMutableStateValues() {

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        mutableStateFlow.update { currentState ->
            currentState.copy(
                playerText = "${bet[PLAYER]} €",
                computerText = "${bet[BOT]} €",
                playerMoney = pokerChips[PLAYER],
                computerMoney = pokerChips[BOT],
                playerBetValue = BIG_BLIND,
                currentPot = pokerChips[POT],
                totalPot = totalPotValue,
                gameSummary = gameSummaryMap
            )
        }
    }

    /**
     * Switch player turns
     */
    override fun switchPlayerTurn() {
        player = if (player == PLAYER) BOT else PLAYER
        opponent = if (player == BOT) PLAYER else BOT
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

        mutableStateFlow.update { currentState ->
            currentState.copy(
                playerBetValue = betValue
            )
        }
    }
}
