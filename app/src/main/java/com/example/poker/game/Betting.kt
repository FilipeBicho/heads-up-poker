package com.example.poker.game

import com.example.poker.BIG_BLIND
import com.example.poker.POT
import com.example.poker.SMALL_BLIND
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.Bot
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
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.blind
import com.example.poker.game.Data.botValidActions
import com.example.poker.game.Data.checkAvailable
import com.example.poker.game.Data.dealer
import com.example.poker.game.Data.gameNumber
import com.example.poker.game.Data.gameSummaryList
import com.example.poker.game.Data.gameSummaryMap
import com.example.poker.game.Data.init
import com.example.poker.game.Data.minPlayerBet
import com.example.poker.game.Data.name
import com.example.poker.game.Data.opponent
import com.example.poker.game.Data.player
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import com.example.poker.game.Data.showdown
import com.example.poker.game.Data.totalPotValue
import com.example.poker.game.Data.uiStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.timerTask

class Betting {

    private var computerBot: Bot = Bot()

    /**
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    private fun isBetAvailable(): Boolean {
        return if (bet[opponent] == 0 && pokerChips[player] > BIG_BLIND) {
            minPlayerBet = BIG_BLIND
            true
        } else {
            false
        }
    }

    /**
     * raise is available if:
     *  - there is a previous bet
     *  - player chips and player bet (if any) is bigger than 2 times opponent bet
     */
    private fun isRaiseAvailable(): Boolean {

        return if (bet[opponent] > 0 && pokerChips[player] > bet[opponent] * 2) {
            minPlayerBet = bet[opponent] * 2
            true
        } else {
            false
        }
    }

    /**
     * all in is available if:
     *  - there is a previous bet:
     *      - player chips and player bet is equal or small than 2 times opponent bet
     *  - there is no previous bet:
     *      - player chips are smaller or equal to big bling
     */
    private fun isAllInAvailable(): Boolean {
        return if (bet[opponent] > 0 && pokerChips[player] <= bet[opponent] * 2) {
            true
        } else {
            pokerChips[player] <= BIG_BLIND
        }
    }
    
    private fun switchPlayerTurn() {
        player = if (player == PLAYER) BOT else PLAYER
        opponent = if (player == BOT) PLAYER else BOT
    }

    private fun calculateBotAction() {
        when (computerBot.calculateAction()) {
            FOLD -> fold()
            CHECK -> check()
            CALL -> call()
            BET -> {
                bet(computerBot.betValue)
            }
            RAISE -> {
                raise(computerBot.betValue)
            }
            ALLIN -> {
                allIn()
            }
        }
    }

    private fun foldCall() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = false,
                displayRaiseButton = false,
                displayAllInButton = false
            )}
        } else {
            botValidActions[FOLD] = true
            botValidActions[CHECK] = false
            botValidActions[CALL] = true
            botValidActions[BET] = false
            botValidActions[RAISE] = false
            botValidActions[ALLIN] = false

            calculateBotAction()
        }
    }

    private fun foldCallBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = isBetAvailable(),
                displayRaiseButton = isRaiseAvailable(),
                displayAllInButton = isAllInAvailable()
            )}
        } else {
            botValidActions[FOLD] = true
            botValidActions[CHECK] = false
            botValidActions[CALL] = true
            botValidActions[BET] = isBetAvailable()
            botValidActions[RAISE] = isRaiseAvailable()
            botValidActions[ALLIN] = isAllInAvailable()

           calculateBotAction()
        }
    }

    private fun checkBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState ->
                currentState.copy(
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = false,
                    displayBetButton = isBetAvailable(),
                    displayRaiseButton = isRaiseAvailable(),
                    displayAllInButton = isAllInAvailable()
                )
            }
        } else {
            botValidActions[FOLD] = false
            botValidActions[CHECK] = true
            botValidActions[CALL] = false
            botValidActions[BET] = isBetAvailable()
            botValidActions[RAISE] = isRaiseAvailable()
            botValidActions[ALLIN] = isAllInAvailable()

            calculateBotAction()
        }
    }

    private fun nextRound() {

        if (player == dealer) {
            switchPlayerTurn()
        }

        totalPotValue += pokerChips[POT]
        bet[PLAYER] = 0
        bet[BOT] = 0
        pokerChips[POT] = 0
        checkAvailable = true
        minPlayerBet = BIG_BLIND

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerBetValue = bet[PLAYER],
                botBetValue = bet[BOT],
                totalPot = totalPotValue,
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
            )
        }

        when (round) {
            PRE_FLOP -> {
                round = FLOP
                showdown.flop()
                checkBet()
            }

            FLOP -> {
                round = TURN
                showdown.turn()
                checkBet()
            }

            TURN -> {
                round = RIVER
                showdown.river()
                checkBet()
            }

            RIVER -> {
                showdown.calculateWinner()
            }
        }
    }

    fun preFlop() {
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
                 totalPotValue += pokerChips[POT]

                 gameSummaryList += "${name[blind]} makes all in ${bet[blind]} €"
                 gameSummaryList += "${name[dealer]} pays all in ${bet[dealer]} €"
                 gameSummaryMap[gameNumber] = gameSummaryList.toList()

                 uiStateFlow.update { currentState ->
                     currentState.copy(
                         playerMoney = pokerChips[PLAYER],
                         botMoney = pokerChips[BOT],
                         playerBetValue = bet[dealer],
                         currentPot = pokerChips[POT],
                         totalPot = totalPotValue,
                         playerText = "${bet[PLAYER]} €",
                         botText = "${bet[BOT]} €",
                         gameSummary = gameSummaryMap
                     )
                 }

                showdown.flop()
             } else {
                 // blind makes all in
                 bet[blind] = pokerChips[blind]
                 pokerChips[blind] = 0

                 // dealer pay small blind
                 bet[dealer] = SMALL_BLIND
                 pokerChips[dealer] -= bet[dealer]

                 // calculate pot
                 pokerChips[POT] = bet[blind] + bet[dealer]

                 gameSummaryList += "${name[blind]} makes all in ${bet[blind]} €"
                 gameSummaryList += "${name[dealer]} pays small blind ${bet[dealer]} €"
                 gameSummaryMap[gameNumber] = gameSummaryList.toList()

                 player = dealer

                 uiStateFlow.update { currentState ->
                     currentState.copy(
                         playerMoney = pokerChips[PLAYER],
                         botMoney = pokerChips[BOT],
                         playerBetValue = bet[PLAYER],
                         botBetValue = bet[BOT],
                         currentPot = pokerChips[POT],
                         totalPot = totalPotValue,
                         playerText = "${bet[PLAYER]} €",
                         botText = "${bet[BOT]} €",
                         gameSummary = gameSummaryMap
                     )
                 }

                 foldCall()
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

             gameSummaryList += "${name[blind]} makes all in ${bet[blind]} €"
             gameSummaryList += "${name[dealer]} pays all in ${bet[dealer]} €"
             gameSummaryMap[gameNumber] = gameSummaryList.toList()

             uiStateFlow.update { currentState ->
                 currentState.copy(
                     playerMoney = pokerChips[PLAYER],
                     botMoney = pokerChips[BOT],
                     playerBetValue = bet[PLAYER],
                     botBetValue = bet[BOT],
                     currentPot = pokerChips[POT],
                     totalPot = totalPotValue,
                     playerText = "${bet[PLAYER]} €",
                     botText = "${bet[BOT]} €",
                     gameSummary = gameSummaryMap
                 )
             }

             showdown.showdownCards()
         } else {

             // dealer pay small blind
             bet[dealer] = SMALL_BLIND
             pokerChips[dealer] -= bet[dealer]

             // blind pay big blind
             bet[blind] = BIG_BLIND
             pokerChips[blind] -= bet[blind]

             // calculate pot
             pokerChips[POT] = bet[blind] + bet[dealer]

             gameSummaryList += "${name[dealer]} pays small blind ${bet[dealer]} €"
             gameSummaryList += "${name[blind]} pays big blind ${bet[blind]} €"
             gameSummaryMap[gameNumber] = gameSummaryList.toList()

             uiStateFlow.update { currentState ->
                 currentState.copy(
                     playerMoney = pokerChips[PLAYER],
                     botMoney = pokerChips[BOT],
                     playerBetValue = bet[PLAYER],
                     botBetValue = bet[BOT],
                     currentPot = pokerChips[POT],
                     totalPot = totalPotValue,
                     playerText = "${bet[PLAYER]} €",
                     botText = "${bet[BOT]} €",
                     gameSummary = gameSummaryMap
                 )
             }

             player = dealer

             foldCallBet()
         }
    }

    fun fold() {
        // opponent wins the pot
        pokerChips[opponent] += pokerChips[POT] + totalPotValue

        gameSummaryList += "${name[player]} folds"
        gameSummaryList += "${name[opponent]} wins ${pokerChips[POT]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
                gameSummary = gameSummaryMap,
            )
        }
        Timer().schedule(timerTask {
            init.newGame()
        }, 2000)
    }

    fun check() {

        gameSummaryList += "${name[player]} checks"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                gameSummary = gameSummaryMap
            )
        }

        if (checkAvailable && round != PRE_FLOP) {
            checkAvailable = false
            switchPlayerTurn()
            checkBet()
        } else {
            nextRound()
        }
    }

    fun call() {

        val callValue = bet[opponent] - bet[player]

        if (pokerChips[player] <= callValue) {

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

            gameSummaryList += "${name[player]} calls $callValue €"
            gameSummaryMap[gameNumber] = gameSummaryList.toList()

            uiStateFlow.update { currentState ->
                currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerText = "${bet[PLAYER]} €",
                    botText = "${bet[BOT]} €",
                    playerBetValue = bet[PLAYER],
                    botBetValue = bet[BOT],
                    currentPot = pokerChips[POT],
                    totalPot = totalPotValue,
                    gameSummary = gameSummaryMap
                )
            }

            showdown.showdownCards()
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= callValue

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${name[player]} calls $callValue €"
            gameSummaryMap[gameNumber] = gameSummaryList.toList()

            uiStateFlow.update { currentState ->
                currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    botMoney = pokerChips[BOT],
                    playerBetValue = bet[PLAYER],
                    botBetValue = bet[BOT],
                    playerText = "${bet[PLAYER]} €",
                    botText = "${bet[BOT]} €",
                    currentPot = pokerChips[POT],
                    totalPot = totalPotValue,
                    gameSummary = gameSummaryMap
                )
            }

            switchPlayerTurn()

            if (pokerChips[player] == 0 || pokerChips[opponent] == 0) {
                totalPotValue += pokerChips[POT]
                showdown.showdownCards()
            } else {
                if (checkAvailable && round == PRE_FLOP) {
                    checkBet()
                } else {
                    if (round == RIVER) {
                        totalPotValue += pokerChips[POT]

                        uiStateFlow.update { currentState ->
                            currentState.copy(
                                playerMoney = pokerChips[PLAYER],
                                botMoney = pokerChips[BOT],
                                playerBetValue = bet[PLAYER],
                                botBetValue = bet[BOT],
                                currentPot = pokerChips[POT],
                                totalPot = totalPotValue,
                                playerText = "0 €",
                                botText = "0 €",
                                gameSummary = gameSummaryMap
                            )
                        }

                        showdown.showdownCards()
                    } else {
                        nextRound()
                    }
                }
            }
        }
    }

    fun bet(value: Int) {

        checkAvailable = false

        bet[player] = value

        pokerChips[player] -= bet[player]

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${name[player]} bets ${bet[player]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerBetValue = bet[dealer],

                currentPot = pokerChips[POT],
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
                totalPot = totalPotValue,
                gameSummary = gameSummaryMap
            )
        }
        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {
           foldCall()
        } else {
            foldCallBet()
        }
    }

    fun raise(value: Int) {

        checkAvailable = false

        // Add old bet to chips and subtract the new bet
        pokerChips[player] += bet[player] - value
        bet[player] = value
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${name[player]} raises to ${bet[player]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
                playerBetValue = bet[dealer],
                currentPot = pokerChips[POT],
                totalPot = totalPotValue,
                gameSummary = gameSummaryMap
            )
        }

        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {
            foldCall()
        } else {
            foldCallBet()
        }
    }

    fun allIn() {
        checkAvailable = false

        // bet all chips
        bet[player] = pokerChips[player] + bet[player]
        pokerChips[player] = 0
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${name[player]} makes all in with ${bet[player]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
                playerBetValue = bet[dealer],
                currentPot = pokerChips[POT],
                totalPot = totalPotValue,
                gameSummary = gameSummaryMap
            )
        }

        switchPlayerTurn()

        if (pokerChips[player] > 0) {
            foldCall()
        } else {
            showdown.showdownCards()
        }
    }
}