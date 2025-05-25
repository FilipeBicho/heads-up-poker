package com.filipebicho.pokerclash.bot

import android.util.Log
import com.filipebicho.pokerclash.POT
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.botModel
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.tableCards
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
const val RAISE = 4
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
            val actionString = jsonContent.get("action").toString()
            betValue = jsonContent.get("bet").toString().toInt()

            return when (actionString) {
                "fold" -> FOLD
                "call" -> CALL
                "check" -> CHECK
                "bet" -> BET
                "raise" -> RAISE
                "all in" -> ALLIN
                else -> -1
            }
        }

        return -1
    }

    fun getRequestMessage(): List<Message> {
        val botCard1 = botCards.first().toString()
        val botCard2 = botCards.last().toString()

        var tableCardsString = ""
        when (round) {
            FLOP -> tableCards.subList(0,3).forEach { tableCardsString += "$it, " }
            TURN -> tableCards.subList(0,4).forEach { tableCardsString += "$it, " }
            RIVER -> tableCards.forEach { tableCardsString += "$it, " }
            else -> ""
        }

        var playerAction = when (action) {
            NO_ACTION -> "No action"
            FOLD -> "Fold"
            CHECK -> "Check"
            CALL -> "Call"
            BET -> "Bet ${bet[PLAYER]}"
            RAISE -> "Raise ${bet[PLAYER]}"
            ALLIN -> "All in"
            else -> ""
        }

        var roundString = when (round) {
            PRE_FLOP -> "Pre flop"
            FLOP -> "Flop"
            TURN -> "Turn"
            RIVER -> "River"
            else -> ""
        }

        val dealer = if (dealer == BOT) "You" else "Opponent"

        Log.d("ChatgptBot", "Request: \"Game type: Heads-up Texas hold'em\\n\" +\n" +
                "                    \"Your hand: $botCard1, $botCard2\\n\" +\n" +
                "                    \"Table cards: $tableCardsString\\n\" +\n" +
                "                    \"Round: $roundString\\n\" +\n" +
                "                    \"Dealer: $dealer\\n\" +\n" +
                "                    \"Initial money: 1500\\n\" +\n" +
                "                    \"Your money: ${pokerChips[BOT]}\\n\" +\n" +
                "                    \"Opponent money: ${pokerChips[PLAYER]}\\n\" +\n" +
                "                    \"Your previous bet: ${bet[BOT]}\\n\"+\n" +
                "                    \"Opponent bet: ${bet[PLAYER]}\\n\"+\n" +
                "                    \"Current pot round: ${bet[POT]}\\n\" +\n" +
                "                    \"Total pot: ${pokerChips[POT]}\\n\" +\n" +
                "                    \"Opponent action: $playerAction \\n\" +\n" +
                "                    \"Output: JSON containing only the action and bet\\n\" +\n" +
                "                    \"Action types: Fold, Check, Call, Bet, Raise, All in\\n\" +\n" +
                "                    \"Bet: value of the bet\\n\" +\n" +
                "                    \"Question: What should be my action and Bet?\\n\"")

        return listOf(Message(
            role = "user",
            content = "Game type: Heads-up Texas hold'em\n" +
                    "Your hand: $botCard1, $botCard2\n" +
                    "Table cards: $tableCardsString\n" +
                    "Round: $roundString\n" +
                    "Dealer: $dealer\n" +
                    "Initial money: 1500\n" +
                    "Your money: ${pokerChips[BOT]}\n" +
                    "Opponent money: ${pokerChips[PLAYER]}\n" +
                    "Your previous bet: ${bet[BOT]}\n"+
                    "Opponent bet: ${bet[PLAYER]}\n"+
                    "Current pot round: ${bet[POT]}\n" +
                    "Total pot: ${pokerChips[POT]}\n" +
                    "Opponent action: $playerAction \n" +
                    "Output: JSON containing only the action and bet\n" +
                    "Action types: Fold, Check, Call, Bet, Raise, All in\n" +
                    "Bet: value of the bet\n" +
                    "Question: What should be my action and Bet?\n"
        ))
    }
}