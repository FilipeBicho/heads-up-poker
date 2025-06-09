package com.filipebicho.pokerclash.bot

import android.util.Log
import com.filipebicho.pokerclash.BIG_BLIND
import com.filipebicho.pokerclash.SMALL_BLIND
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.actionHistory
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.botModel
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.roundText
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.validActions
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

class ChatgptBot {

    val retrofit = RetrofitClient.getOpenAiClient()
    var betValue: Int = 0

    suspend fun getAction(): Int = suspendCancellableCoroutine { continuation ->
        val request = ChatRequest(model = botModel, messages = getRequestMessage(), response_format = ResponseFormat(type = "json_object"))
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

            if (jsonContent.has("bet")) {
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
        var currentTableCards = ""

        currentTableCards = when (round) {
            FLOP -> tableCards.subList(0,3).joinToString(", ") { it.cardString() }
            TURN -> tableCards.subList(0,4).joinToString(", ") { it.cardString() }
            RIVER -> tableCards.joinToString(", ") { it.cardString() }
            else -> "[none]"
        }

        val prompt = PokerRequest(
            yourHand = botCards.joinToString(", ") { it.cardString() },
            tableCards = currentTableCards,
            round = roundText[round],
            dealer = if (dealer == BOT) "You" else "Opponent",
            smallBlind = SMALL_BLIND,
            bigBlind = BIG_BLIND,
            yourStack = pokerChips[BOT],
            opponentStack = pokerChips[PLAYER],
            yourBetThisRound = bet[BOT],
            opponentBetThisRound = bet[PLAYER],
            potBeforeRound = mainPot,
            currentPot = mainPot + roundPot,
            actionHistory = actionHistory,
            validActions = validActions,
        )

        Log.d("ChatgptBot", "Request: $prompt")

        return listOf(Message(
            role = "user",
            content = prompt.toPrompt()
        ))
    }
}