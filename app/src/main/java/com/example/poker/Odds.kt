package com.example.poker

import kotlin.math.roundToInt

class Odds (private var playerCards: ArrayList<Card>, private val tableCards: ArrayList<Card>, private var deck: ArrayList<Card>) {

    private var cardCombinations = mutableListOf<ArrayList<Card>>()

    private var player1 = 0
    private var player2 = 0
    private var draw = 0
    private var combinations = 0
    var winningOdds: Float = 0.0F

    init {

    }

    /**
     * create function to set all possible hand combinations
     */
    private fun setCardCombinations() {

        val usedCombinations = mutableListOf<ArrayList<Card>>()

        for (card1: Card in deck) {
            for (card2: Card in deck) {
                if (card1 == card2) {
                    continue
                }

                // prevent repetitions
                if (usedCombinations.isNotEmpty() && usedCombinations.contains(listOf(card1, card2))) {
                   continue
                }

                // add combination
                cardCombinations.add(arrayListOf(card1, card2))

                // used to prevent repetitions
                usedCombinations.add(arrayListOf(card1, card2))
                usedCombinations.add(arrayListOf(card2, card1))
            }
        }
    }

    /**
     * Calculate flop odds
     */
    fun flopOdds() {

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        // only iterate half of the combinations
        val size = (cardCombinations.size.toFloat()/2).roundToInt()

        for (index1: Int in 0  until size step 10) {
           for (index2: Int in size - 10 downTo 10 step 10) {

               // continue if 2nd combination contains any card from the 1st combination
               if (cardCombinations[index2].contains(cardCombinations[index1].component1())
                   || cardCombinations[index2].contains(cardCombinations[index1].component2())) {
                   continue
               }

               // add 2nd combination cards to table cards
               tempTableCards.addAll(cardCombinations[index2].toList())

               // use table cards to calculate player hand
               val playerHand = Hand(playerCards, tempTableCards)

               // use 1st combination cards and table cards to calculate opponent hand
               val opponentHand = Hand(cardCombinations[index1], tempTableCards)

               // calculate winner
               when (HandWinnerCalculator(player1Hand = playerHand, player2Hand = opponentHand).getWinner()) {
                   1 -> player1++
                   2 -> player2++
                   3 -> draw++
                   else -> {}
               }

               // remove temporarily table cards
               tempTableCards.removeLast()
               tempTableCards.removeLast()
               combinations++
           }
        }

        // calculate winning odds
        winningOdds = ((player1.toFloat() / combinations) * 100)
    }

    /**
     * calculate turn odds
     */
    fun turnOdds() {

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        for (opponentCards: ArrayList<Card> in cardCombinations) {
            for (turnCard: Card in deck) {

                // continue if turn card is in card combination
                if (opponentCards.contains(turnCard)) {
                    continue
                }

                // add turn card to table
                tempTableCards.add(turnCard)

                // use table cards to calculate player hand
                val playerHand = Hand(playerCards, tempTableCards)

                // use combination cards and table cards to calculate opponent hand
                val opponentHand = Hand(opponentCards, tempTableCards)

                // calculate winner
                when (HandWinnerCalculator(player1Hand = playerHand, player2Hand = opponentHand).getWinner()) {
                    1 -> player1++
                    2 -> player2++
                    3 -> draw++
                    else -> {}
                }

                tempTableCards.removeLast()
                combinations++
            }
        }

        // calculate winning odds
        winningOdds = ((player1.toFloat() / combinations) * 100)
    }

    /**
     * calculate river odds
     */
    fun riverOdds() {

        // use table cards to calculate player hand
        val playerHand = Hand(playerCards, tableCards)

        for (opponentCards: ArrayList<Card> in cardCombinations) {

            // use combination cards and table cards to calculate opponent hand
            val opponentHand = Hand(opponentCards, tableCards)

            // calculate winner
            when (HandWinnerCalculator(player1Hand = playerHand, player2Hand = opponentHand).getWinner()) {
                1 -> player1++
                2 -> player2++
                3 -> draw++
                else -> {}
            }

            combinations++
        }

        // calculate winning odds
        winningOdds = ((player1.toFloat() / combinations) * 100)
    }

}