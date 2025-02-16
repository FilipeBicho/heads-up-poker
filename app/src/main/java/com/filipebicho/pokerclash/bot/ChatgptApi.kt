package com.filipebicho.pokerclash.bot

import android.util.Log
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.FLOP
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.cards.RIVER
import com.filipebicho.pokerclash.cards.TURN
import com.filipebicho.pokerclash.game.Data
import com.filipebicho.pokerclash.game.Data.action
import com.filipebicho.pokerclash.game.Data.bet
import com.filipebicho.pokerclash.game.Data.botCards
import com.filipebicho.pokerclash.game.Data.dealer
import com.filipebicho.pokerclash.game.Data.pokerChips
import com.filipebicho.pokerclash.game.Data.round
import com.filipebicho.pokerclash.game.Data.tableCards
import com.filipebicho.pokerclash.game.Data.totalPotValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ChatgptApi {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun makeApiCall(retrofit: ChatgptApiInterface): Pair<Int, String?> {

        return suspendCoroutine { continuation ->
            val request = ChatRequest(model = "gpt-4o", messages = getRequestMessage(), response_format = ResponseFormat(type = "json_object"))
            Log.d("Message: ", request.messages.toString())
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
    }

    fun getActionsFromResponse(response: Response<ChatResponse>): Pair<Int, String> {
        val content = response.body()?.choices?.first()?.message?.content
        if (content != null) {
            val jsonContent = JSONObject(content)
            val actionString = jsonContent.get("action").toString()
            val bet = jsonContent.get("bet").toString()

            val action = when (actionString) {
                "Fold" -> FOLD
                "Call" -> CALL
                "Check" -> CHECK
                "Bet" -> BET
                "Raise" -> RAISE
                "All in" -> ALLIN
                else -> -1
            }
            return Pair(action, bet)
        }

        return Pair(-1, "")
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
            BET -> "Bet ${Data.bet[PLAYER]}"
            RAISE -> "Raise ${Data.bet[PLAYER]}"
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

        val currentPot = bet[PLAYER] + bet[BOT]
        val totalPot = totalPotValue + currentPot

        val dealer = if (dealer == BOT) "You" else "Opponent"

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
                    "Current pot round: $currentPot\n" +
                    "Total pot: $totalPot\n" +
                    "Opponent action: $playerAction \n" +
                    "Output: JSON containing only the action and bet\n" +
                    "Action types: Fold, Check, Call, Bet, Raise, All in\n" +
                    "Bet: value of the bet\n" +
                    "Question: What should be my action and Bet?\n"
        ))
    }
}