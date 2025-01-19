package com.filipebicho.gptpoker

import androidx.lifecycle.ViewModel
import com.filipebicho.gptpoker.bot.ALLIN
import com.filipebicho.gptpoker.bot.BET
import com.filipebicho.gptpoker.bot.CALL
import com.filipebicho.gptpoker.bot.CHECK
import com.filipebicho.gptpoker.bot.FOLD
import com.filipebicho.gptpoker.bot.RAISE
import com.filipebicho.gptpoker.cards.PLAYER
import com.filipebicho.gptpoker.game.Data.action
import com.filipebicho.gptpoker.game.Data.betting
import com.filipebicho.gptpoker.game.Data.init
import com.filipebicho.gptpoker.game.Data.pokerChips
import com.filipebicho.gptpoker.game.Data.uiStateFlow
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
                playerBetValue = value,
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

    fun newGame() {
        init.initGame()
    }
}
