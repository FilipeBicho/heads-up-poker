package com.example.poker

import androidx.lifecycle.ViewModel
import com.example.poker.game.Bet
import com.example.poker.game.Dealer
import com.example.poker.game.GameplayState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

const val POT = 2
const val SMALL_BLIND = 20
const val BIG_BLIND = 40

class GameViewModel : ViewModel() {

    var dealer: Dealer = Dealer()
    lateinit var bet: Bet

    init {
        dealer.newGame()
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
