package com.example.poker

class HandWinnerCalculator(player1Hand: Hand, player2Hand: Hand) {

    private var winner: Int = 3
    val player1Cards = player1Hand.getHand()
    val player2Cards = player2Hand.getHand()
    private val player1Result = player1Hand.resultValue
    val player2Result = player2Hand.resultValue

    init {
        if (player1Result > player2Result) {
            winner = 1
        } else if (player1Result< player2Result) {
            winner = 2
        } else {
            winner = calculateWinner()
        }
    }

    private fun calculateWinner(): Int {

        if (player1Result == 9) {
            return straightFlush()
        }

        return winner
    }

    /**
     * calculate straight flush winner
     */
    private fun straightFlush() = if (player1Cards.first().rank > player2Cards.first().rank) 1 else 2

}