package com.example.poker.bot

class ChatgptBot: Bot() {

    override suspend fun calculateAction(): Int {

        try {
            val chatgptApi: ChatgptApi = ChatgptApi()
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