package com.example.poker

import androidx.lifecycle.ViewModel
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.betValue
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
        init.newGame()
    }

    /**
     * Update player bet via button interaction
     */
    fun updatePlayerBet(value: Int) {

        betValue = if (value > pokerChips[player]) {
            pokerChips[player]
        } else {
            value
        }

        uiStateFlow.update { currentState ->
            currentState.copy(
                playerBetValue = betValue
            )
        }
    }

    fun foldAction() {
        betting.fold()
    }

    fun checkAction() {
        betting.check()
    }

    fun callAction() {
        betting.call()
    }

    fun betAction() {
        betting.bet()
    }

    fun raiseAction() {
        betting.raise()
    }

    fun allInAction() {
        betting.allIn()
    }
}
