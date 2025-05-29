package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.BIG_BLIND
import com.filipebicho.pokerclash.SMALL_BLIND
import com.filipebicho.pokerclash.bot.ALLIN
import com.filipebicho.pokerclash.bot.BET
import com.filipebicho.pokerclash.bot.CALL
import com.filipebicho.pokerclash.bot.CHECK
import com.filipebicho.pokerclash.bot.FOLD
import com.filipebicho.pokerclash.bot.NO_ACTION
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.actionText
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.blind
import com.filipebicho.pokerclash.data.Data.botLastRaise
import com.filipebicho.pokerclash.data.Data.chatGptBot
import com.filipebicho.pokerclash.data.Data.checkAvailable
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.init
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.showdown
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timerTask

class Betting {

    fun preFlop() {

        val blindName = uiStateFlow.value.name[player]
        val dealerName = uiStateFlow.value.name[opponent]

         if (pokerChips[blind] <= BIG_BLIND) {
             if (pokerChips[blind] <= SMALL_BLIND) {

                 // blind makes all in
                 bet[blind] = pokerChips[blind]
                 pokerChips[blind] = 0

                 // dealer pays all in
                 bet[dealer] = bet[blind]
                 pokerChips[dealer] -= bet[dealer]

                 // calculate pot
                 roundPot = bet[blind] + bet[dealer]

                 gameSummaryList += "$blindName makes all in ${bet[blind]}"
                 gameSummaryList += "$dealerName pays all in ${bet[dealer]}"

                 actionText[blind] = "All in ${bet[blind]}"
                 actionText[dealer] = "Call ${bet[dealer]}"

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
                 roundPot = bet[blind] + bet[dealer]

                 gameSummaryList += "$blindName makes all in ${bet[blind]}"
                 gameSummaryList += "$dealerName pays all in ${bet[dealer]}"

                 actionText[blind] = "All in ${bet[blind]}"
                 actionText[dealer] = "Call ${bet[dealer]}"

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
             roundPot = bet[blind] + bet[dealer]

             gameSummaryList += "$blindName makes all in ${bet[dealer]}"
             gameSummaryList += "$dealerName pays all in ${bet[blind]}"

             actionText[dealer] = "All in ${bet[dealer]}"
             actionText[blind] = "Call ${bet[blind]}"

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
             roundPot = bet[blind] + bet[dealer]

             gameSummaryList += "$dealerName pays small blind ${bet[dealer]}"
             gameSummaryList += "$blindName pays big blind ${bet[blind]}"

             actionText[dealer] = "SB"
             actionText[blind] = "BB"

             updateStateFlowBets()
             player = dealer
             foldCallBet()
         }
    }

    fun fold() {
        val playerName = uiStateFlow.value.name[player]
        val opponentName = uiStateFlow.value.name[opponent]

        // opponent wins the pot
        val totalPotWonByOpponent = mainPot + roundPot
        pokerChips[opponent] += totalPotWonByOpponent

        gameSummaryList += "$playerName folds"
        gameSummaryList += "$opponentName wins $totalPotWonByOpponent"

        actionText[player] = "Fold"
        actionText[opponent] = "Win $totalPotWonByOpponent"

        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                gameSummary = gameSummaryMap,
                actions = actionText,
                isPlayerTurn = false
            )
        }
        Timer().schedule(timerTask {
            init.newGame()
        }, 3000L)
    }

    fun check() {
        val playerName = uiStateFlow.value.name[player]
        gameSummaryList += "$playerName checks"
        gameSummaryMap[gameNumber] = gameSummaryList.toList()

        actionText[player] = "Check"

        uiStateFlow.update { currentState ->
            currentState.copy(
                actions = actionText,
                gameSummary = gameSummaryMap
            )
        }

        val canPerformCheckAction = checkAvailable && round != PRE_FLOP
        if (canPerformCheckAction) {
            checkAvailable = false
            switchPlayerTurn()
            checkBet()
        } else {
            nextRound()
        }
    }

    fun call() {
        val playerName = uiStateFlow.value.name[player]
        val amountToCall = bet[opponent] - bet[player]
        gameSummaryList += "$playerName calls $amountToCall"

        if (pokerChips[player] <= amountToCall) {
            // Player is all-in by calling
            val allInAmount = pokerChips[player]
            bet[player] += allInAmount
            pokerChips[player] = 0

            // Opponent's bet might need to be adjusted if player's all-in is less than opponent's bet
            if (bet[opponent] > bet[player]) {
                val excessBet = bet[opponent] - bet[player]
                pokerChips[opponent] += excessBet // Return excess to opponent's chips
                bet[opponent] = bet[player]     // Opponent's bet now matches player's all-in bet
            }

            roundPot = bet[player] + bet[opponent]
            actionText[player] = "All in $allInAmount"
            
            updateStateFlowBets()
            showdown.showdownCards()
        } else {

            // Player has enough chips to call normally
            pokerChips[player] -= amountToCall
            bet[player] = bet[opponent]

            roundPot = bet[player] + bet[opponent]
            actionText[player] = "Call $amountToCall"
            
            updateStateFlowBets()

            if (round == RIVER || (pokerChips[player] == 0 || pokerChips[opponent] == 0)) {
                showdown.showdownCards()
            } else {
                switchPlayerTurn()
                if (checkAvailable && round == PRE_FLOP)
                    checkBet()
                else
                    nextRound()
            }
        }
    }

    fun bet(newBetAmount: Int) {
        val playerName = uiStateFlow.value.name[player]
        checkAvailable = false

        val previousBetAmount = bet[player]

        if (player == BOT)
            botLastRaise = newBetAmount - bet[BOT]

        val playerTotalStake = pokerChips[player] + previousBetAmount
        if (newBetAmount > playerTotalStake) {
            allIn()
        } else {
            val opponentTotalStake = pokerChips[opponent] + bet[opponent]
            if (newBetAmount >= opponentTotalStake) {
                // Player bets more than or equal to opponent's total stack, opponent will be all-in if they call
                bet[player] = opponentTotalStake
            } else {
                bet[player] = newBetAmount
            }

            // Adjust player's chips: subtract the additional amount committed in this action
            pokerChips[player] = playerTotalStake - bet[player]

            // calculate pot
            roundPot = bet[player] + bet[opponent]

            gameSummaryList += "$playerName bets ${bet[player]}"
            actionText[player] = "Bet ${bet[player]}"
            
            updateStateFlowBets()
            switchPlayerTurn()

            if (pokerChips[player] + bet[player] <= bet[opponent]) {
                foldCall()
            } else {
                foldCallBet()
            }
        }
    }

    fun allIn() {
        val playerName = uiStateFlow.value.name[player]
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

        // calculate pot
        roundPot = bet[player] + bet[opponent]

        gameSummaryList += "$playerName makes all in with ${bet[player]}"
        actionText[player] = "All in ${bet[player]}"
        
        updateStateFlowBets()
        switchPlayerTurn()

        if (pokerChips[player] > 0) {
            foldCall()
        } else {
            showdown.showdownCards()
        }
    }

    private fun nextRound() {

        if (player == dealer)
            switchPlayerTurn()

        action = NO_ACTION

        mainPot += roundPot
        roundPot = 0

        bet[PLAYER] = 0
        bet[BOT] = 0
        checkAvailable = true

        actionText[player] = ""

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerBet = bet[PLAYER],
                botBet = bet[BOT],
                playerMinRaise = getMinRaiseForPlayer(),
                playerCurrentRaise = getMinRaiseForPlayer(),
                mainPot = mainPot,
                roundPot = roundPot,
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

    private fun getMinRaiseForPlayer(): Int {
        val botBet = bet[BOT]
        val playerBet = bet[PLAYER]

        // Pre-flop: if both bets are zero (new hand)
        if (botBet == 0 && playerBet == 0)
            return BIG_BLIND

        // No previous raise (bot just called BB)
        if (botBet == BIG_BLIND)
            return BIG_BLIND * 2

        // There was a previous raise — use lastRaiseAmount
        return botBet + botLastRaise
    }

    private fun isBetAvailable(): Boolean {
        val playerTotalStake = pokerChips[PLAYER] + bet[PLAYER]

        if (playerTotalStake < bet[opponent])
            return false

        if (bet[opponent] == 0 && pokerChips[PLAYER] > 0)
            return true

        return playerTotalStake >= bet[opponent]
    }

    private fun isMinBetAvailable(): Boolean {
        return getMinRaiseForPlayer() < pokerChips[PLAYER]
    }

    private fun is3BBBetAvailable(): Boolean {
        return getMinRaiseForPlayer() <= BIG_BLIND * 3
    }

    private fun isPotBetAvailable(): Boolean {
        return getMinRaiseForPlayer() <= roundPot + mainPot && roundPot + mainPot < pokerChips[PLAYER]
    }

    /**
     * all in is available if:
     *  - there is a previous bet:
     *      - player chips and player bet is equal or small than 2 times opponent bet
     *  - there is no previous bet:
     *      - player chips are smaller or equal to big bling
     */
    private fun isAllInAvailable(): Boolean {
        return if (bet[BOT] > 0 && pokerChips[PLAYER] <= bet[BOT] * 2) {
            true
        } else {
            pokerChips[PLAYER] >= BIG_BLIND
        }
    }

    private fun switchPlayerTurn() {
        player = if (player == PLAYER) BOT else PLAYER
        opponent = if (player == BOT) PLAYER else BOT
        uiStateFlow.update { currentState -> currentState.copy(
            isPlayerTurn = player == PLAYER
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
                ALLIN -> {
                    allIn()
                }
                else -> null
            }
        }
    }

    private fun updateStateFlowBets() {
        gameSummaryMap[gameNumber] = gameSummaryList.toList()
        uiStateFlow.update { currentState ->
            currentState.copy(
                playerMoney = pokerChips[PLAYER],
                botMoney = pokerChips[BOT],
                playerBet = bet[PLAYER],
                botBet = bet[BOT],
                playerMinRaise = getMinRaiseForPlayer(),
                playerCurrentRaise = getMinRaiseForPlayer(),
                actions = actionText,
                roundPot = roundPot,
                mainPot = mainPot + roundPot,
                gameSummary = gameSummaryMap
            )
        }
    }

    private fun foldCall() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                isPlayerTurn = true,
                playerCall = bet[BOT] - bet[PLAYER],
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = false,
                displayMinSmallButton = false,
                display3BBSmallButton = false,
                displayPotSmallButton = false,
                displayAllInSmallButton = false
            )}
        } else {
            botAction()
        }
    }

    private fun foldCallBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState -> currentState.copy(
                isPlayerTurn = true,
                playerCall = bet[BOT] - bet[PLAYER],
                playerMinRaise = getMinRaiseForPlayer(),
                playerCurrentRaise = getMinRaiseForPlayer(),
                displayFoldButton = true,
                displayCheckButton = false,
                displayCallButton = true,
                displayBetButton = isBetAvailable(),
                displayMinSmallButton = isMinBetAvailable(),
                display3BBSmallButton = is3BBBetAvailable(),
                displayPotSmallButton = isPotBetAvailable(),
                displayAllInSmallButton = isAllInAvailable()
            )}
        } else {
            botAction()
        }
    }

    private fun checkBet() {
        if (player == PLAYER) {
            uiStateFlow.update { currentState ->
                currentState.copy(
                    isPlayerTurn = true,
                    playerMinRaise = getMinRaiseForPlayer(),
                    displayFoldButton = false,
                    displayCheckButton = true,
                    displayCallButton = false,
                    displayBetButton = isBetAvailable(),
                    displayMinSmallButton = isMinBetAvailable(),
                    display3BBSmallButton = is3BBBetAvailable(),
                    displayPotSmallButton = isPotBetAvailable(),
                    displayAllInSmallButton = isAllInAvailable()
                )
            }
        } else {
            botAction()
        }
    }
}