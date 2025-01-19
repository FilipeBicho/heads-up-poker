package com.filipebicho.gptpoker.bot

const val NO_ACTION = -1
const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3
const val RAISE = 4
const val ALLIN = 5

class ChatgptBot {

    val retrofit = RetrofitClient.getOpenAiClient()
    val chatgptApi: ChatgptApi = ChatgptApi()

    var action: Int = 0
    var betValue: Int = 0

    suspend fun calculateAction(): Int {

        action = 0
        betValue = 0

        try {
            var (actionValue, bet) = chatgptApi.makeApiCall(retrofit)
            action = actionValue
            if (action == BET || action == RAISE) {
                betValue = bet?.toInt() ?: 0
            }
        } catch (e: Exception) {
            println("Error111: ${e.message}")
        }

        return action
    }
}