package com.example.poker

class HandWinnerCalculator(player1Hand: Hand, player2Hand: Hand) {

    private var winner: Int = 3
    val player1Cards = player1Hand.getHand()
    val player2Cards = player2Hand.getHand()
    private val player1Result = player1Hand.resultValue
    val player2Result = player2Hand.resultValue

    init {
        winner = if (player1Result > player2Result) {
            1
        } else if (player1Result< player2Result) {
            2
        } else {
            calculateWinner()
        }
    }

    private fun calculateWinner(): Int {

        if (player1Result == 9) {
            return straightFlush()
        }
        else if (player1Result == 8) {
            return fourOfAKind()
        }

        return winner
    }

    /**
     * Calculate straight flush winner
     */
    private fun straightFlush() = if (player1Cards.first().rank > player2Cards.first().rank) 1 else 2

    /**
     * Calculate four of a kind winner
     */
    private fun fourOfAKind(): Int {
        // players have the same four of a kind
        if (player1Cards.first().rank == player2Cards.first().rank) {

            // players have the same kicker
            if (player1Cards.last().rank == player2Cards.last().rank ) {
                return 3
            }

            // calculate winner via kicker
            return if (player1Cards.last().rank == 0 && player2Cards.last().rank != 0) {
                1
            } else if (player1Cards.last().rank != 0 && player2Cards.last().rank == 0) {
                2
            } else {
                if (player1Cards.last().rank > player2Cards.last().rank) 1 else 2
            }
        }

        // calculate winner via higher four of a kind
        return if (player1Cards.first().rank == 0 && player2Cards.first().rank != 0) {
            1
        } else if (player1Cards.first().rank != 0 && player2Cards.first().rank == 0) {
            2
        } else {
            if (player1Cards.first().rank > player2Cards.first().rank) 1 else 2
        }
    }

    /**
     * Get result
     */
    fun getResult(): String {

        return when (winner) {
            1 -> "player 1 wins"
            2 -> "player 2 wins"
            3 -> "draw"
            else -> {"draw"}
        }
    }

}