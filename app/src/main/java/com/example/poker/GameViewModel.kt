package com.example.poker

import androidx.lifecycle.ViewModel
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.CALL
import com.example.poker.bot.CHECK
import com.example.poker.bot.FOLD
import com.example.poker.bot.RAISE
import com.example.poker.cards.PLAYER
import com.example.poker.game.Data.action
import com.example.poker.game.Data.betting
import com.example.poker.game.Data.init
import com.example.poker.game.Data.player
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.uiStateFlow
import kotlinx.coroutines.flow.update

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

class GameViewModel : ViewModel() {

    init {
        init.initGame()
    }

    /**
     * Update player bet via button interaction
     */
    fun updatePlayerBet(value: Int) {
        uiStateFlow.update { currentState ->
            currentState.copy(
                playerBetValue = value
            )
        }
    }

    fun foldAction() {
        action = FOLD
        betting.fold()
    }

    fun checkAction() {
        action = CHECK
        betting.check()
    }

    fun callAction() {
        action = CALL
        betting.call()
    }

    fun betAction(value: Int) {
        action = BET
        if (pokerChips[PLAYER] - value == 0) {
            betting.allIn()
        } else {
            betting.bet(value)
        }
    }

    fun raiseAction(value: Int) {
        action = RAISE
        if (pokerChips[PLAYER] - value == 0) {
            betting.allIn()
        } else {
            betting.raise(value)
        }
    }

    fun allInAction() {
        action = ALLIN
        betting.allIn()
    }
}
