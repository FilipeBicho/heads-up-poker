package com.example.poker

import com.example.poker.cards.PLAYER
import com.example.poker.gameplay.Betting
import com.example.poker.gameplay.NewGame
import com.example.poker.gameplay.Showdown
import kotlinx.coroutines.flow.update

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

class GameViewModel : Betting() {

    override val newGame: NewGame = NewGame(this)
    override val showdown: Showdown = Showdown(this)

    init {
        newGame.start()
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

        mutableStateFlow.update { currentState ->
            currentState.copy(
                playerBetValue = betValue
            )
        }
    }
}
