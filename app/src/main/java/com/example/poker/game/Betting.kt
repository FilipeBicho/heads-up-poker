package com.example.poker.game

import com.example.poker.BIG_BLIND
import com.example.poker.POT
import com.example.poker.SMALL_BLIND
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.CALL
import com.example.poker.bot.CHECK
import com.example.poker.bot.FOLD
import com.example.poker.bot.RAISE
import com.example.poker.cards.BOT
import com.example.poker.cards.PLAYER
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.blind
import com.example.poker.game.Data.botValidActions
import com.example.poker.game.Data.dealer
import com.example.poker.game.Data.gameSummaryList
import com.example.poker.game.Data.gameSummaryMap
import com.example.poker.game.Data.name
import com.example.poker.game.Data.opponent
import com.example.poker.game.Data.player
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.totalPotValue
import com.example.poker.game.Data.uiStateFlow
import kotlinx.coroutines.flow.update

class Betting {

    /**
     * all in is available if:
     *  - there is a previous bet:
     *      - player chips and player bet is equal or small than 2 times opponent bet
     *  - there is no previous bet:
     *      - player chips are smaller or equal to big bling
     */
    private fun isAllInAvailable(): Boolean {
        return if (bet[opponent] > 0) {
            pokerChips[player] + bet[player] <= bet[opponent] * 2
        } else {
            pokerChips[player] <= BIG_BLIND
        }
    }

    /**
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    private fun isBetAvailable(): Boolean {
        return bet[opponent] == 0 && pokerChips[player] > BIG_BLIND
    }

    /**
     * raise is available if:
     *  - there is a previous bet
     *  - player chips and player bet (if any) is bigger than 2 times opponent bet
     */
    private fun isRaiseAvailable(): Boolean {
        return bet[opponent] > 0 && pokerChips[player] + bet[player] > bet[opponent] * 2
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

            //TODO bot
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

                 gameSummaryMap[Data.gameNumber] = gameSummaryList.toList()

                 uiStateFlow.update { currentState ->
                     currentState.copy(
                         playerMoney = pokerChips[PLAYER],
                         computerMoney = pokerChips[BOT],
                         playerBetValue = bet[dealer],
                         currentPot = pokerChips[POT],
                         totalPot = totalPotValue,
                         gameSummary = gameSummaryMap
                     )
                 }

                 // TODO : showdown
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

                 player = dealer

                 uiStateFlow.update { currentState ->
                     currentState.copy(
                         playerMoney = pokerChips[PLAYER],
                         computerMoney = pokerChips[BOT],
                         playerBetValue = bet[dealer],
                         currentPot = pokerChips[POT],
                         totalPot = totalPotValue,
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

             uiStateFlow.update { currentState ->
                 currentState.copy(
                     playerMoney = pokerChips[PLAYER],
                     computerMoney = pokerChips[BOT],
                     playerBetValue = bet[dealer],
                     currentPot = pokerChips[POT],
                     totalPot = totalPotValue,
                     gameSummary = gameSummaryMap
                 )
             }

             //TODO showdown
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

             player = dealer

             foldCallBet()
         }
    }
}