package com.example.poker.gameplay

import androidx.lifecycle.viewModelScope
import com.example.poker.BIG_BLIND
import com.example.poker.GameViewModel
import com.example.poker.POT
import com.example.poker.SMALL_BLIND
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.CALL
import com.example.poker.bot.CHECK
import com.example.poker.bot.FOLD
import com.example.poker.bot.RAISE
import com.example.poker.cards.BOT
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

abstract class Betting: Main() {

    /**
     * go to next round
     */
    private fun nextRound() {

        if (player == dealer) {
            switchPlayerTurn()
        }

        totalPotValue += pokerChips[POT]
        bet[PLAYER] = 0
        bet[BOT] = 0
        checkAvailable = true

        updateMutableStateValues()

        when (round) {
            PRE_FLOP -> {
                round = FLOP

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFlop = true,
                    turnDelayTime = 0,
                    riverDelayTime = 1000,
                ) }

                var flopString = ""
                tableCards.subList(0,3).forEach { flopString += it.cardString()+" " }

                gameSummaryList.add("---- $flopString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = isBetAvailable(),
                        displayRaiseButton = isRaiseAvailable(),
                        displayAllInButton = isAllInAvailable()
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
                }
            }

            FLOP -> {
                round = TURN

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayTurn = true,
                    turnDelayTime = 0,
                    riverDelayTime = 0,
                )}
                var turnString = ""
                tableCards.subList(0,4).forEach { turnString += it.cardString()+" " }

                gameSummaryList.add("---- $turnString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = pokerChips[player] + bet[player] >= BIG_BLIND
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
                }
            }

            TURN -> {

                round = RIVER

                mutableStateFlow.update { currentState -> currentState.copy(
                    displayRiver = true,
                )}

                var riverString = ""
                tableCards.forEach { riverString += it.cardString()+" " }

                gameSummaryList.add("---- $riverString ----")

                if (player == PLAYER) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = false,
                        displayCheckButton = true,
                        displayCallButton = false,
                        displayBetButton = pokerChips[player] + bet[player] >= BIG_BLIND
                    )}
                } else {
                    computerBotValidActions[FOLD] = false
                    computerBotValidActions[CHECK] = true
                    computerBotValidActions[CALL] = false
                    computerBotValidActions[BET] = pokerChips[player] + bet[player] >= BIG_BLIND
                    when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                        CHECK -> check()
                        BET -> {
                            betValue = computerBot.betValue
                            bet()
                        }
                    }
                }
            }

            RIVER -> {
                showdown.calculateWinner()
            }
        }
    }

    fun preFlopBets() {

        if (pokerChips[blind] <= BIG_BLIND) {
            if (pokerChips[blind] <= SMALL_BLIND) {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pays all in
                bet[dealer] = bet[blind]
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

                totalPotValue += pokerChips[POT]
                updateMutableStateValues()
                showdown.showdown()
            } else {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pay small blind
                bet[dealer] = SMALL_BLIND
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"

                updateMutableStateValues()
                player = dealer

                if (isPlayerTurn()) {
                    mutableStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false,
                        displayRaiseButton = false,
                        displayAllInButton = false
                    )}
                } else {
                    computerBotValidActions[FOLD] = true
                    computerBotValidActions[CHECK] = false
                    computerBotValidActions[CALL] = true
                    computerBotValidActions[BET] = false
                    computerBotValidActions[RAISE] = false
                    computerBotValidActions[ALLIN] = false

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
        } else if (pokerChips[dealer] <= SMALL_BLIND) {

            // dealer makes all in
            bet[dealer] = pokerChips[dealer]
            pokerChips[dealer] = 0

            // blind pays all in
            bet[blind] = bet[player]
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]
            totalPotValue += pokerChips[POT]

            gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
            gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

            updateMutableStateValues()
            showdown.showdown()
        } else {

            // dealer pay small blind
            bet[dealer] = SMALL_BLIND
            pokerChips[dealer] -= bet[dealer]

            // blind pay big blind
            bet[blind] = BIG_BLIND
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"
            gameSummaryList += "${playerName[blind]} pays big blind ${bet[blind]} €"

            updateMutableStateValues()
            player = dealer

            if (isPlayerTurn()) {
                mutableStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = isBetAvailable(),
                    displayRaiseButton = isRaiseAvailable(),
                    displayAllInButton = isAllInAvailable()
                )}
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = isBetAvailable()
                computerBotValidActions[RAISE] = isRaiseAvailable()
                computerBotValidActions[ALLIN] = isAllInAvailable()

                when (computerBot.botAction(pokerChips, bet, totalPotValue, round, computerBotValidActions)) {
                    FOLD -> fold()
                    CALL -> call()
                    BET -> {
                        betValue = computerBot.betValue
                        bet()
                    }
                    RAISE -> {
                        betValue = computerBot.betValue
                        raise()
                    }
                }
            }
        }
    }


    /**
     * Handles fold request
     */
    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT] + totalPotValue

        gameSummaryList += "${playerName[player]} folds"
        gameSummaryList += "${playerName[opponent]} wins ${pokerChips[POT]} €"

        // update mutable state values
        updateMutableStateValues()

        // new game
        game.newGame()
    }

    /**
     * Handles check request
     */
    fun check() {

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
    fun call() {

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
            showdown.showdown()
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
                showdown.showdown()
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
                        showdown.showdown()
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
    fun bet() {

        checkAvailable = false

        bet[player] = betValue

        pokerChips[player] -= bet[player]

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
                        displayBetButton = isBetAvailable(),
                        displayRaiseButton = isRaiseAvailable(),
                        displayAllInButton = isAllInAvailable()
                    )
                }
            } else {
                betValue = BIG_BLIND

                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = isBetAvailable()
                computerBotValidActions[RAISE] = isRaiseAvailable()
                computerBotValidActions[ALLIN] = isAllInAvailable()
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

    fun raise() {

        checkAvailable = false

        // Add old bet to chips and subtract the new bet
        pokerChips[player] += bet[player] - betValue

        // Update bet
        bet[player] = betValue

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${playerName[player]} raises to ${bet[player]} €"

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
                        displayBetButton = isBetAvailable(),
                        displayRaiseButton = isRaiseAvailable(),
                        displayAllInButton = isAllInAvailable()
                    )
                }
            } else {
                betValue = BIG_BLIND

                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = isBetAvailable()
                computerBotValidActions[RAISE] = isRaiseAvailable()
                computerBotValidActions[ALLIN] = isAllInAvailable()
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

    fun allIn() {
        TODO("Not yet implemented")
    }



}