package com.filipebicho.pokerclash.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.filipebicho.pokerclash.data.Data.botOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

val Context.pokerDataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store_poker")

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

class PokerDataStore(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val preferencesFlow: Flow<Preferences> =  context.pokerDataStore.data

    private fun Preferences.getIntOrDefault(key: Preferences.Key<Int>, defaultValue: Int = 0): Int {
        return this[key] ?: defaultValue
    }

    private fun decodeWinsList(jsonString: String?, defaultSize: Int): MutableList<Int> {
        return if (!jsonString.isNullOrEmpty()) {
            json.decodeFromString<MutableList<Int>>(jsonString)
        } else {
            MutableList(defaultSize) { 0 }
        }
    }

    suspend fun getAllStats(): StoredPokerStats {
        return preferencesFlow.first().let { preferences ->
            val handsPlayed = preferences.getIntOrDefault(PokerStatsKeys.HANDS_PLAYED)
            val voluntaryPutInPot = preferences.getIntOrDefault(PokerStatsKeys.VOLUNTARY_PUT_IN_POT)
            val preFlopRaises = preferences.getIntOrDefault(PokerStatsKeys.PRE_FLOP_RAISES)
            val continuationBet = preferences.getIntOrDefault(PokerStatsKeys.CONTINUATION_BET)
            val continuationBetFaced =
                preferences.getIntOrDefault(PokerStatsKeys.CONTINUATION_BET_FACED)
            val foldsToContinuationBet =
                preferences.getIntOrDefault(PokerStatsKeys.FOLDS_TO_CONTINUATION_BET)
            val riverBets = preferences.getIntOrDefault(PokerStatsKeys.RIVER_BETS)
            val riverBluffsDetected =
                preferences.getIntOrDefault(PokerStatsKeys.RIVER_BLUFFS_DETECTED)

            val playerWinsString = preferences[PokerStatsKeys.PLAYER_WINS]
            val playerWins = try {
                decodeWinsList(playerWinsString, botOptions.size)
            } catch (e: Exception) {
                Log.e("Stats", "Error decoding player wins: ${e.message}")
                MutableList(botOptions.size) { 0 }
            }

            val botWinsString = preferences[PokerStatsKeys.BOT_WINS]
            val botWins = try {
                decodeWinsList(botWinsString, botOptions.size)
            } catch (e: Exception) {
                Log.e("Stats", "Error decoding bot wins: ${e.message}")
                MutableList(botOptions.size) { 0 }
            }

             StoredPokerStats(
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

