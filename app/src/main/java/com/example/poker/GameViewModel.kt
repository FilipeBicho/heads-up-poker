package com.example.poker


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    var dealer: Dealer = Dealer()

    init {
        uiState.value.player1Cards = dealer.getPlayer1Cards()
        uiState.value.player2Cards = dealer.getPlayer2Cards()
    }

    fun dealFlop() {

        _uiState.update { currentState ->
            currentState.copy(
                pot = 50
            )
        }
    }

    fun dealTurn() {
        _uiState.update { currentState -> currentState.copy(pot = 60) }
    }

    fun dealRiver() {
        _uiState.update { currentState ->
            currentState.copy(
                tableCards = dealer.getRiver()
            )
        }
    }

}