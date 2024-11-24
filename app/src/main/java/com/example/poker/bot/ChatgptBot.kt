package com.example.poker.bot

class ChatgptBot: Bot() {

    override suspend fun calculateAction(): Int {

        try {
            val chatgptApi: ChatgptApi = ChatgptApi()
            chatgptApi.makeApiCall(retrofit)
            action = when(chatgptApi.getAction()) {
                "Fold" -> FOLD
                "Call" -> CALL
                "Check" -> CHECK
                "Bet" -> BET
                "Raise" -> RAISE
                "All in" -> ALLIN
                else -> -1
            }

            if (action == BET || action == RAISE) {
                betValue = chatgptApi.getBet().toInt()
            }
        } catch (e: Exception) {
            println("Error111: ${e.message}")
        }


        return action
    }
}