package com.filipebicho.pokerclash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipebicho.pokerclash.bot.BET
import com.filipebicho.pokerclash.bot.CALL
import com.filipebicho.pokerclash.bot.CHECK
import com.filipebicho.pokerclash.bot.FOLD
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.betting
import com.filipebicho.pokerclash.data.Data.currentBot
import com.filipebicho.pokerclash.data.Data.init
import com.filipebicho.pokerclash.data.Data.playerWins
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.simulatedPlayer
import com.filipebicho.pokerclash.data.Data.stats
import com.filipebicho.pokerclash.data.Data.statsRepository
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.data.PokerStatsRepository
import com.filipebicho.pokerclash.game.Init
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


const val SMALL_BLIND = 20
const val BIG_BLIND = 40

class GameViewModel : ViewModel() {

    init {
        loadStats()
        init = Init(viewModelScope)
    }

    fun setPlayerName(playerName: String) {
        uiStateFlow.update { currentState ->
            currentState.copy(
                playerName = playerName,
                name = listOf(playerName, "")
            )
        }
    }

    fun setBot(bot: Pair<String, String>, index: Int) {
        uiStateFlow.update { currentState ->
            currentState.copy(
                simulatedPlayerName = bot.first,
                name = listOf(uiStateFlow.value.playerName, bot.first)
            )
        }

        simulatedPlayer = bot.second
        currentBot = index
    }

    fun startGame() {
        init.initGame()
    }

    /**
     * Update player bet via button interaction
     */
    fun updatePlayerBet(value: Int) {
        uiStateFlow.update { currentState ->
            currentState.copy(
                playerCurrentRaise = value,
            )
        }
    }

    fun fold() {
        action = FOLD
        betting.fold()
    }

    fun check() {
        action = CHECK
        betting.check()
    }

    fun call() {
        action = CALL
        betting.call()
    }

    fun bet(value: Int) {
        action = BET
        if (pokerChips[PLAYER] - value == 0) {
            betting.allIn()
        } else {
            betting.bet(value)
        }
    }

    fun toggleGameSummary() {
        uiStateFlow.update { currentState ->
            currentState.copy(
                displaySummary = !currentState.displaySummary,
            )
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            val loadedData = statsRepository.getAllStats()

            stats.handsPlayed = loadedData.handsPlayed
            stats.voluntarilyPutMoneyInPot = loadedData.voluntarilyPutMoneyInPot
            stats.preFlopRaises = loadedData.preFlopRaises
            stats.continuationBet = loadedData.continuationBet
            stats.continuationBetFaced = loadedData.continuationBetFaced
            stats.foldsToContinuationBet = loadedData.foldsToContinuationBet
            stats.riverBets = loadedData.riverBets
            stats.riverBluffsDetected = loadedData.riverBluffsDetected
            playerWins.addAll(loadedData.playerWins)
            playerWins.addAll(loadedData.botWins)
        }
    }
}
