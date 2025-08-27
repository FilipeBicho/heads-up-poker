package com.filipebicho.pokerclash.bot

import android.util.Log
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.actionHistory
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.roundText
import com.filipebicho.pokerclash.data.Data.simulatedPlayer
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.validActions
import com.filipebicho.pokerclash.game.Stats
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val NO_ACTION = -1
const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3
const val ALLIN = 5

class ChatgptBot(private val stats: Stats) {

    val retrofit = RetrofitClient.getOpenAiClient()
    var betValue: Int = 0

    suspend fun getAction(): Int = suspendCancellableCoroutine { continuation ->
        val request = ChatRequest(model = "gpt-4o", messages = getRequestMessage(), response_format = ResponseFormat(type = "json_object"))
        retrofit.getChatCompletion(request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    // Resume coroutine with the result
                    continuation.resume(getActionsFromResponse(response))
                } else {
                    // Resume with exception if response is unsuccessful
                    continuation.resumeWithException(Exception("API error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                // Resume with exception if call fails
                continuation.resumeWithException(t)
            }
        })
    }

    suspend fun calculateAction(): Int {
        try {
           return getAction()
        } catch (e: Exception) {
           throw e
        }
    }

    fun getActionsFromResponse(response: Response<ChatResponse>): Int {
        val content = response.body()?.choices?.first()?.message?.content.toString().lowercase()
        if (content.isNotEmpty()) {
            val jsonContent = JSONObject(content)
            Log.d("ChatgptBot", "Response: $jsonContent")
            var actionString = ""
            if (jsonContent.has("action")) {
                actionString = jsonContent.get("action").toString()
            }

            if (jsonContent.has("bet") && jsonContent.get("bet") != null) {
                betValue = jsonContent.get("bet").toString().toInt()
            }

            return when (actionString) {
                "fold" -> FOLD
                "call" -> CALL
                "check" -> CHECK
                "bet" -> BET
                "all in" -> ALLIN
                else -> -1
            }
        }

        return -1
    }

    fun getRequestMessage(): List<Message> {
        var currentTableCards = emptyList<Card>()

        currentTableCards = when (round) {
            FLOP -> tableCards.subList(0,3)
            TURN -> tableCards.subList(0,4)
            RIVER -> tableCards
            else -> emptyList()
        }

        val opponentStatsPayload = mapOf(
            "handsPlayed" to stats.handsPlayed,
            "voluntarilyPutMoneyInPot" to stats.safePercentage(stats.voluntarilyPutMoneyInPot, stats.handsPlayed),
            "preFlopRaises" to stats.safePercentage(stats.preFlopRaises, stats.handsPlayed),
            "continuationBet" to stats.safePercentage(stats.continuationBet, stats.preFlopRaises),
            "continuationBetFaced" to stats.continuationBetFaced,
            "foldsToContinuationBet" to stats.safePercentage(stats.foldsToContinuationBet, stats.continuationBetFaced),
            "riverBets" to stats.riverBets,
            "riverBluffsDetected" to stats.riverBluffsDetected
        )

        val prompt = buildChatPrompt(opponentStatsPayload, currentTableCards)
        return listOf(Message(
            role = "user",
            content = prompt
        ))
    }

    fun buildChatPrompt(
        opponentStatsPayload: Map<String, Int>,
        tableCards: List<Card>,
    ): String = buildString {
        appendLine("You are playing Heads-up Texas Hold'em Poker.")
        appendLine()
        appendLine("Opponent Stats after ${opponentStatsPayload["handsPlayed"]} hands:")
        appendLine("- VPIP (Voluntarily Put Money In Pot): ${opponentStatsPayload["voluntarilyPutMoneyInPot"]}%")
        appendLine("- PFR (Pre-Flop Raise): ${opponentStatsPayload["preFlopRaises"]}%")
        appendLine("- Continuation Bet Frequency: ${opponentStatsPayload["continuationBet"]}%")
        appendLine("- Fold to C-Bet: ${opponentStatsPayload["foldsToContinuationBet"]}%")
        appendLine("- River Bets: ${opponentStatsPayload["riverBets"]}")
        appendLine("- River Bluffs Detected: ${opponentStatsPayload["riverBluffsDetected"]}")
        appendLine()
        appendLine("Current Hand State:")
        appendLine("- Round: ${roundText[round]}")
        appendLine("- Your Hand: ${botCards.joinToString(", ") { it.cardString() }}")
        appendLine("- Board: ${if (tableCards.isEmpty()) "No board yet" else tableCards.joinToString(", ") { it.cardString() }}")
        appendLine("- Dealer: ${if (dealer == BOT) "You" else "Opponent"}")
        appendLine("- Pot: ${roundPot + mainPot}, Pot Before Round: ${mainPot}")
        appendLine("- Your Stack: ${pokerChips[BOT]}, Opponent Stack: ${pokerChips[PLAYER]}")
        appendLine("- Your Bet This Round: ${bet[BOT]}, Opponent Bet This Round: ${bet[PLAYER]}")
        appendLine()
        appendLine("Valid Actions: ${validActions.joinToString(", ")}")
        appendLine()
        appendLine("Action History:")
        actionHistory.forEach { appendLine("- $it") }
        appendLine()
        appendLine("Play style: Play as $simulatedPlayer")
        appendLine("Output: JSON with keys \"action\" and \"bet\"")
        appendLine("Question: What should be my action and bet?")
    }

}