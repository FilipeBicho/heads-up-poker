package com.example.poker

import androidx.lifecycle.ViewModel
import com.example.poker.game.Data.init

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

class GameViewModel : ViewModel() {

    init {
        init.newGame()
    }

//    /**
//     * Update player bet via button interaction
//     */
//    fun updatePlayerBet(value: Int) {
//
//        betValue = if (value > pokerChips[player]) {
//            pokerChips[player]
//        } else {
//            value
//        }
//
//        mutableStateFlow.update { currentState ->
//            currentState.copy(
//                playerBetValue = betValue
//            )
//        }
//    }
}
