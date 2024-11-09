package com.example.poker.bot

import android.util.Log
import org.json.JSONObject

class ChatgptApi {

    private var action: String = ""
    private var bet: String = ""

    fun makeApiCall(retrofit: ChatgptApiInterface) {

        try {
            val message =
                listOf(
                    Message(
                        role = "user",
                        content = "Game type: Heads-up Texas hold'em\n" +
                                "Your hand: 8 clubs and 9 clubs\n" +
                                "Table cards: 7 hearts, 6 hearts, 5 clubs\n" +
                                "Initial money: 1500\n" +
                                "Your money: 1700\n" +
                                "Opponent money: 1300\n" +
                                "Pot: 100\n" +
                                "Opponent action: Bet 50\n" +
                                "Output: JSON containing only the action and bet\n" +
                                "Action types: Fold, Check, Call, Bet, Raise, All in\n" +
                                "Bet: value of the bet\n" +
                                "Question: What should be my action and Bet?\n"
                    ))

            val request = ChatRequest(model = "gpt-4o-mini", messages = message, response_format = ResponseFormat(type = "json_object"))
            val response = retrofit.getChatCompletion(request).execute()

            if (response.isSuccessful) {
                val content = response.body()?.choices?.first()?.message?.content
                if (content != null) {
                    val jsonContent = JSONObject(content)
                    action = jsonContent.get("action").toString()
                    bet = jsonContent.get("bet").toString()
                }

            }

        }catch (e: Exception) {
            Log.e("MainViewModel", "Error: ${e.message}")
        }

    }

    fun getAction(): String = action
    fun getBet(): String = bet
}