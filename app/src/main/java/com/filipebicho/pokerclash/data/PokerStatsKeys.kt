package com.filipebicho.pokerclash.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.pokerStatsDataStore: DataStore<Preferences> by preferencesDataStore(name = "poker_game_stats")

object PokerStatsKeys {
    val HANDS_PLAYED = intPreferencesKey("hands_played")
    val VOLUNTARY_PUT_IN_POT = intPreferencesKey("vol_put_in_pot")
    val PRE_FLOP_RAISES = intPreferencesKey("pre_flop_raises")
    val CONTINUATION_BET = intPreferencesKey("cont_bet")
    val CONTINUATION_BET_FACED = intPreferencesKey("cont_bet_faced")
    val FOLDS_TO_CONTINUATION_BET = intPreferencesKey("folds_to_cont_bet")
    val RIVER_BETS = intPreferencesKey("river_bets")
    val RIVER_BLUFFS_DETECTED = intPreferencesKey("river_bluffs_detected")
    val PLAYER_WINS = stringPreferencesKey("player_wins")
    val BOT_WINS = stringPreferencesKey("bot_wins")
}

class PokerStatsRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    // Read Data
    val handsPlayed: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.HANDS_PLAYED] ?: 0}

    val voluntaryPutInPot: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.VOLUNTARY_PUT_IN_POT] ?: 0}

    val preFlopRaises: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.PRE_FLOP_RAISES] ?: 0}

    val continuationBet: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.CONTINUATION_BET] ?: 0}

    val continuationBetFaced: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.CONTINUATION_BET_FACED] ?: 0}

    val foldsToContinuationBet: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.FOLDS_TO_CONTINUATION_BET] ?: 0}

    val riverBets: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.RIVER_BETS] ?: 0}

    val riverBluffsDetected: Flow<Int> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.RIVER_BLUFFS_DETECTED] ?: 0}

    private val playerWinsJson: Flow<String?> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.PLAYER_WINS] }

    private val botWinsJson: Flow<String?> = context.pokerStatsDataStore.data
        .map { preferences -> preferences[PokerStatsKeys.BOT_WINS] }

    suspend fun getAllStats(): StoredPokerStats {
        val preferences = context.pokerStatsDataStore.data.first()

        val handsPlayed = preferences[PokerStatsKeys.HANDS_PLAYED] ?: 0
        val voluntaryPutInPot = preferences[PokerStatsKeys.VOLUNTARY_PUT_IN_POT] ?: 0
        val preFlopRaises = preferences[PokerStatsKeys.PRE_FLOP_RAISES] ?: 0
        val continuationBet = preferences[PokerStatsKeys.CONTINUATION_BET] ?: 0
        val continuationBetFaced = preferences[PokerStatsKeys.CONTINUATION_BET_FACED] ?: 0
        val foldsToContinuationBet = preferences[PokerStatsKeys.FOLDS_TO_CONTINUATION_BET] ?: 0
        val riverBets = preferences[PokerStatsKeys.RIVER_BETS] ?: 0
        val riverBluffsDetected = preferences[PokerStatsKeys.RIVER_BLUFFS_DETECTED] ?: 0

        val playerWins = json.decodeFromString<MutableList<Int>>(preferences[PokerStatsKeys.PLAYER_WINS] ?: "[]")
        val botWins = json.decodeFromString<MutableList<Int>>(preferences[PokerStatsKeys.BOT_WINS] ?: "[]")

        return StoredPokerStats(
            handsPlayed,
            voluntaryPutInPot,
            preFlopRaises,
            continuationBet,
            continuationBetFaced,
            foldsToContinuationBet,
            riverBets,
            riverBluffsDetected,
            playerWins,
            botWins
        )
    }

    suspend fun saveAllStats(stats: StoredPokerStats) {
        context.pokerStatsDataStore.edit { settings ->
            settings[PokerStatsKeys.HANDS_PLAYED] = stats.handsPlayed
            settings[PokerStatsKeys.VOLUNTARY_PUT_IN_POT] = stats.voluntarilyPutMoneyInPot
            settings[PokerStatsKeys.PRE_FLOP_RAISES] = stats.preFlopRaises
            settings[PokerStatsKeys.CONTINUATION_BET] = stats.continuationBet
            settings[PokerStatsKeys.CONTINUATION_BET_FACED] = stats.continuationBetFaced
            settings[PokerStatsKeys.FOLDS_TO_CONTINUATION_BET] = stats.foldsToContinuationBet
            settings[PokerStatsKeys.RIVER_BETS] = stats.riverBets
            settings[PokerStatsKeys.RIVER_BLUFFS_DETECTED] = stats.riverBluffsDetected
            settings[PokerStatsKeys.PLAYER_WINS] = json.encodeToString(stats.playerWins)
            settings[PokerStatsKeys.BOT_WINS] = json.encodeToString(stats.botWins)
        }
    }
}

data class StoredPokerStats(
    val handsPlayed: Int,
    val voluntarilyPutMoneyInPot: Int,
    val preFlopRaises: Int,
    val continuationBet: Int,
    val continuationBetFaced: Int,
    val foldsToContinuationBet: Int,
    val riverBets: Int,
    val riverBluffsDetected: Int,
    val playerWins: MutableList<Int>,
    val botWins: MutableList<Int>
)

