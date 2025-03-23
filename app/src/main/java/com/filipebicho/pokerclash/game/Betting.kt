package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.BIG_BLIND
import com.filipebicho.pokerclash.POT
import com.filipebicho.pokerclash.SMALL_BLIND
import com.filipebicho.pokerclash.bot.ALLIN
import com.filipebicho.pokerclash.bot.BET
import com.filipebicho.pokerclash.bot.CALL
import com.filipebicho.pokerclash.bot.CHECK
import com.filipebicho.pokerclash.bot.FOLD
import com.filipebicho.pokerclash.bot.NO_ACTION
import com.filipebicho.pokerclash.bot.RAISE
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.game.Data.action
import com.filipebicho.pokerclash.game.Data.bet
import com.filipebicho.pokerclash.game.Data.blind
import com.filipebicho.pokerclash.game.Data.chatGptBot
import com.filipebicho.pokerclash.game.Data.checkAvailable
import com.filipebicho.pokerclash.game.Data.dealer
import com.filipebicho.pokerclash.game.Data.gameNumber
import com.filipebicho.pokerclash.game.Data.gameSummaryList
import com.filipebicho.pokerclash.game.Data.gameSummaryMap
import com.filipebicho.pokerclash.game.Data.init
import com.filipebicho.pokerclash.game.Data.minPlayerBet
import com.filipebicho.pokerclash.game.Data.name
import com.filipebicho.pokerclash.game.Data.opponent
import com.filipebicho.pokerclash.game.Data.player
import com.filipebicho.pokerclash.game.Data.pokerChips
import com.filipebicho.pokerclash.game.Data.round
import com.filipebicho.pokerclash.game.Data.showdown
import com.filipebicho.pokerclash.game.Data.totalPotValue
import com.filipebicho.pokerclash.game.Data.uiStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timerTask

class Betting {

    /**
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    private fun isBetAvailable(): Boolean {
        return if (bet[opponent] == 0 && pokerChips[player] > BIG_BLIND) {
            minPlayerBet = BIG_BLIND
            uiStateFlow.update { currentState ->
                currentState.copy(
                    playerBetValue = BIG_BLIND
                )
            }
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
            uiStateFlow.update { currentState ->
                currentState.copy(
                    playerBetValue = bet[opponent] * 2
                )
            }
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
        uiStateFlow.update { currentState -> currentState.copy(
            displayBetButtons = player == PLAYER
        )}
    }

    private fun botAction() {
        CoroutineScope(Dispatchers.Main).launch {
            val action = chatGptBot.calculateAction()
            when (action) {
                FOLD -> fold()
                CHECK -> check()
                CALL -> call()
                BET -> {
                    bet(chatGptBot.betValue)
                }
                RAISE -> {
                    raise(chatGptBot.betValue)
                }
                ALLIN -> {
                    allIn()
                }
                else -> null
            }
        }
    }

    private fun updateStateFlowBets() {
        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerBetValue = bet[dealer],
                currentPot = pokerChips[POT],
                totalPot = totalPotValue + pokerChips[POT],
                debugTotal = pokerChips[PLAYER] + pokerChips[BOT] + pokerChips[POT] + totalPotValue,
                playerText = "${bet[PLAYER]} €",
                botText = "${bet[BOT]} €",
                gameSummary = gameSummaryMap
            )
        }
    }

    private fun foldCall() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                displayBetButtons = true,
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = false,
                displayRaiseButton = false,
                displayAllInButton = false
            )}
        } else {
            botAction()
        }
    }

    private fun foldCallBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                displayBetButtons = true,
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = isBetAvailable(),
                displayRaiseButton = isRaiseAvailable(),
                displayAllInButton = isAllInAvailable()
            )}
        } else {
            botAction()
        }
    }

    private fun checkBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState ->
                currentState.copy(
                    displayBetButtons = true,
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = false,
                    displayBetButton = isBetAvailable(),
                    displayRaiseButton = isRaiseAvailable(),
                    displayAllInButton = isAllInAvailable()
                )
            }
        } else {
            botAction()
        }
    }

    private fun nextRound() {

        if (player == dealer)
            switchPlayerTurn()

        action = NO_ACTION

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
                currentPot = 0
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

                 gameSummaryList += "${name[blind]} makes all in ${bet[blind]} €"
                 gameSummaryList += "${name[dealer]} pays all in ${bet[dealer]} €"
                 gameSummaryMap[gameNumber] = gameSummaryList.toList()

                 updateStateFlowBets()
                 showdown.showdownCards()
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

                 updateStateFlowBets()
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

             gameSummaryList += "${name[blind]} makes all in ${bet[blind]} €"
             gameSummaryList += "${name[dealer]} pays all in ${bet[dealer]} €"
             gameSummaryMap[gameNumber] = gameSummaryList.toList()

             updateStateFlowBets()
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

             updateStateFlowBets()
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
                actionText = "${name[player]} folds, ${name[opponent]} wins ${pokerChips[POT] + totalPotValue} €",
                gameSummary = gameSummaryMap,
                displayBetButtons = false
            )
        }
        Timer().schedule(timerTask {
            init.newGame()
        }, 3000)
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
            pokerChips[POT] = bet[player] + bet[opponent]

            gameSummaryList += "${name[player]} calls $callValue €"
            gameSummaryMap[gameNumber] = gameSummaryList.toList()

            updateStateFlowBets()
            showdown.showdownCards()
        } else {

            // player equals opponent bet
            bet[player] = bet[opponent]
            pokerChips[player] -= callValue

            // calculate pot
            pokerChips[POT] = bet[player] + bet[opponent]

            gameSummaryList += "${name[player]} calls $callValue €"
            gameSummaryMap[gameNumber] = gameSummaryList.toList()

            updateStateFlowBets()
            switchPlayerTurn()

            if (pokerChips[player] == 0 || pokerChips[opponent] == 0) {
                showdown.showdownCards()
            } else {
                if (checkAvailable && round == PRE_FLOP) {
                    checkBet()
                } else {
                    if (round == RIVER) {
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

        if (value >= pokerChips[player]) {
            allIn()
            return
        } else if (value > pokerChips[opponent]) {
            bet[player] = pokerChips[opponent];
        } else {
            bet[player] = value
        }

        pokerChips[player] -= bet[player]

        // calculate pot
        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${name[player]} bets ${bet[player]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        updateStateFlowBets()
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

        updateStateFlowBets()
        switchPlayerTurn()

        if (pokerChips[player] + bet[player] <= bet[opponent]) {
            foldCall()
        } else {
            foldCallBet()
        }
    }

    fun allIn() {
        checkAvailable = false

        val previousBet = bet[player]

        // bet all chips
        bet[player] = if (pokerChips[player] + bet[player] > pokerChips[opponent] + bet[opponent]) {
            pokerChips[opponent] + bet[opponent]
        } else {
            pokerChips[player] + bet[player]
        }

        pokerChips[player] += previousBet
        pokerChips[player] -= bet[player]

        pokerChips[POT] = bet[player] + bet[opponent]

        gameSummaryList += "${name[player]} makes all in with ${bet[player]} €"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        updateStateFlowBets()
        switchPlayerTurn()

        if (pokerChips[player] > 0) {
            foldCall()
        } else {
            showdown.showdownCards()
        }
    }
}