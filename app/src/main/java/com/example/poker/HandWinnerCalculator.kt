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

        return when(player1Result) {
            9 -> compareStraight()
            8 -> compareFirstAndLastCard()
            7 -> compareFirstAndLastCard()
            6 -> compareFlush()
            5 -> compareStraight()
            4 -> compareThreeOfAKind()
            3 -> compareTwoPair()
            2 -> compareOnePair()
            1 -> compareHigherKicker(startIndex = 0, endIndex = 4)
            else -> {winner}
        }
    }

    /**
     * Calculate flush winner
     */
    private fun compareFlush(): Int {
        return if (player1Cards[0].rank == 0 && player2Cards[0].rank != 0) {
            1
        } else if (player1Cards[0].rank != 0 && player2Cards[0].rank == 0) {
            2
        } else {
            compareHigherKicker(startIndex = 3, endIndex = 4)
        }
    }

    /**
     * Calculate straight flush or straight winner
     */
    private fun compareStraight(): Int {
        return if (player1Cards.last().rank > player2Cards.last().rank) {
            1
        } else  if (player1Cards.last().rank < player2Cards.last().rank) {
            2
        } else {
            3
        }
    }

    /**
     * Calculate three of a kind winner
     */
    private fun compareThreeOfAKind(): Int {

        // check first if there is a three of a kind of Aces
        return if (player1Cards[0].rank == 0 && player2Cards[0].rank != 0) {
            1
        } else if (player1Cards[0].rank != 0 && player2Cards[0].rank == 0) {
            2
        } else {
            // check if highest three of a kind
            if (player1Cards[0].rank > player2Cards[0].rank) {
                1
            } else if (player1Cards[0].rank < player2Cards[0].rank) {
                2
            } else {
                compareHigherKicker(startIndex = 3, endIndex = 4)
            }
        }
    }

    /**
     * Calculate 2 pair winner
     */
    private fun compareTwoPair(): Int {
        return if (player1Cards[0].rank == 0 && player2Cards[0].rank != 0) {
            1
        } else if (player1Cards[0].rank != 0 && player2Cards[0].rank == 0) {
            2
        } else {
            if (player1Cards[0].rank > player2Cards[0].rank) {
                1
            } else if (player1Cards[0].rank < player2Cards[0].rank) {
                2
            } else {
                if (player1Cards[3].rank > player2Cards[3].rank) {
                    1
                } else if (player1Cards[3].rank < player2Cards[3].rank) {
                    2
                } else {
                    compareHigherKicker(startIndex = 4, endIndex = 4)
                }
            }
        }
    }

    private fun compareOnePair(): Int {
        return if (player1Cards[0].rank == 0 && player2Cards[0].rank != 0) {
            1
        } else if (player1Cards[0].rank != 0 && player2Cards[0].rank == 0) {
            2
        } else {
            compareHigherKicker(startIndex = 2, endIndex = 4)
        }
    }

    /**
     * Calculate four of a kind or full house winner
     */
    private fun compareFirstAndLastCard(): Int {
        // players have the same four of a kind
        if (player1Cards[0].rank == player2Cards[0].rank) {
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
                if (player1Cards.last().rank > player2Cards.last().rank) {
                    1
                } else if (player1Cards.last().rank < player2Cards.last().rank) {
                    2
                } else {
                    3
                }
            }
        }

        // calculate winner via higher four of a kind
        return if (player1Cards[0].rank == 0 && player2Cards[0].rank != 0) {
            1
        } else if (player1Cards[0].rank != 0 && player2Cards[0].rank == 0) {
            2
        } else {
            if (player1Cards[0].rank > player2Cards[0].rank) {
                1
            } else if (player1Cards[0].rank < player2Cards[0].rank) {
                2
            } else {
                3
            }
        }
    }

    /**
     * compare kickers
     */
    private fun compareHigherKicker (startIndex: Int, endIndex: Int): Int {

        // check for kicker Ace
        if (player1Cards[startIndex].rank == 0 && player2Cards[startIndex].rank != 0) {
            return 1
        } else if (player1Cards[startIndex].rank != 0 && player2Cards[startIndex].rank == 0) {
            return 2
        } else {
            // check higher kicker
            for (index: Int in startIndex .. endIndex) {
                if (player1Cards[index].rank > player2Cards[index].rank) {
                    return 1
                } else if (player1Cards[index].rank < player2Cards[index].rank) {
                    return 2
                }
            }
            return 3
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