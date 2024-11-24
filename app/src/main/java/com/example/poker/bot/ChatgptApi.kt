package com.example.poker.bot

import android.util.Log
import com.example.poker.cards.BOT
import com.example.poker.cards.FLOP
import com.example.poker.cards.PLAYER
import com.example.poker.cards.RIVER
import com.example.poker.cards.TURN
import com.example.poker.game.Data
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import com.example.poker.game.Data.tableCards
import com.example.poker.game.Data.totalPotValue
import org.json.JSONObject

class ChatgptApi {

    private var action: String = ""
    private var bet: String = ""

    fun makeApiCall(retrofit: ChatgptApiInterface) {

        try {

            val botCard1 = botCards.first().toString()
            val botCard2 = botCards.last().toString()

            var tableCardsString = ""
            when (round) {
                FLOP -> tableCards.subList(0,3).forEach { tableCardsString += "$it " }
                TURN -> tableCards.subList(0,4).forEach { tableCardsString += "$it " }
                RIVER -> tableCards.forEach { tableCardsString += "$it " }
                else -> ""
            }

            var playerAction = when (Data.action) {
                NO_ACTION -> "No action"
                FOLD -> "Fold"
                CHECK -> "Check"
                CALL -> "Call"
                BET -> "Bet ${Data.bet[PLAYER]}"
                RAISE -> "Raise ${Data.bet[PLAYER]}"
                ALLIN -> "All in"
                else -> ""
            }

            val message =
                listOf(
                    Message(
                        role = "user",
                        content = "Game type: Heads-up Texas hold'em\n" +
                                "Your hand: $botCard1 $botCard2\n" +
                                "Table cards: $tableCardsString\n" +
                                "Initial money: 1500\n" +
                                "Your money: ${pokerChips[BOT]}\n" +
                                "Opponent money: ${pokerChips[PLAYER]}\n" +
                                "Pot: $totalPotValue\n" +
                                "Opponent action: $playerAction \n" +
                                "Output: JSON containing only the action and bet\n" +
                                "Action types: Fold, Check, Call, Bet, Raise, All in\n" +
                                "Bet: value of the bet\n" +
                                "Question: What should be my action and Bet?\n"
                    ))

            val request = ChatRequest(model = "gpt-4o", messages = message, response_format = ResponseFormat(type = "json_object"))
            val response = retrofit.getChatCompletion(request).execute()

            if (response.isSuccessful) {
                val content = response.body()?.choices?.first()?.message?.content
                if (content != null) {
                    val jsonContent = JSONObject(content)
                    action = jsonContent.get("action").toString()
                    bet = jsonContent.get("bet").toString()
                }

            }

        } catch (e: Exception) {
            Log.e("MainViewModel", "Error: ${e.message}")
        }

    }

    fun getAction(): String = action
    fun getBet(): String = bet
}