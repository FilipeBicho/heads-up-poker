package com.example.poker

import android.util.Log
import kotlin.math.roundToInt

class Odds(private var playerCards: List<Card>, private var tableCards: MutableList<Card>) {

    private var cardCombinations = mutableListOf<ArrayList<Card>>()
    private var deck = Deck().getDeck()

    /**
     * set all possible hand combinations
     */
    init {
        val usedCombinations = mutableListOf<ArrayList<Card>>()

        // remove player cards
        for (playerCard in playerCards) {
            deck.removeIf { playerCard.toString() == it.toString() }
        }

        // remove table cards
        for (tableCard in tableCards) {
            deck.removeIf { tableCard.toString() == it.toString() }
        }

        for (card1: Card in deck) {
            for (card2: Card in deck) {
                if (card1 == card2) {
                    continue
                }

                // prevent repetitions
                if (usedCombinations.isNotEmpty()
                    && (usedCombinations.contains(listOf(card1, card2))
                            || usedCombinations.contains(listOf(card2, card1)))
                ) {
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
    fun getFlopOdds(): Array<Int> {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0
        val odds = Array(11) { _ -> 0}

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        for (opponentCards: ArrayList<Card> in cardCombinations) {
            for (tableCards: ArrayList<Card> in cardCombinations) {
                // continue if 2nd combination contains any card from the 1st combination
                if (opponentCards.contains(tableCards.component1())
                    || opponentCards.contains(tableCards.component2())
                ) {
                    continue
                }

                // add 2nd combination cards to table cards
                tempTableCards.addAll(tableCards)

                // use table cards to calculate player hand
                val playerHand = Hand(playerCards, tempTableCards)
                odds[playerHand.resultValue]++

                // use 1st combination cards and table cards to calculate opponent hand
                val opponentHand = Hand(opponentCards, tempTableCards)

                // calculate winner
                when (HandWinnerCalculator(
                    player1Hand = playerHand,
                    player2Hand = opponentHand
                ).getWinner()) {
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

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        // calculate odds
        odds[RESULT] = ((player1.toFloat() / combinations) * 100).roundToInt()

        Log.d("ODDS", odds.joinToString("\n"))
        return odds
    }

    /**
     * calculate turn odds
     */
    fun getTurnOdds(): Array<Int> {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0
        val odds = Array(11) { _ -> 0}

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
                odds[playerHand.resultValue]++

                // use combination cards and table cards to calculate opponent hand
                val opponentHand = Hand(opponentCards, tempTableCards)

                // calculate winner
                when (HandWinnerCalculator(
                    player1Hand = playerHand,
                    player2Hand = opponentHand
                ).getWinner()) {
                    1 -> player1++
                    2 -> player2++
                    3 -> draw++
                    else -> {}
                }

                tempTableCards.removeLast()
                combinations++
            }
        }

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        // calculate odds
        odds[RESULT] = ((player1.toFloat() / combinations) * 100).roundToInt()
        Log.d("ODDS", odds.joinToString("\n"))
        return odds
    }

    /**
     * calculate river odds
     */
    fun getRiverOdds(): Array<Int> {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0
        val odds = Array(11) { _ -> 0}

        // use table cards to calculate player hand
        val playerHand = Hand(playerCards, tableCards)
        odds[playerHand.resultValue] = 100

        for (opponentCards: ArrayList<Card> in cardCombinations) {

            // use combination cards and table cards to calculate opponent hand
            val opponentHand = Hand(opponentCards, tableCards)

            // calculate winner
            when (HandWinnerCalculator(
                player1Hand = playerHand,
                player2Hand = opponentHand
            ).getWinner()) {
                1 -> player1++
                2 -> player2++
                3 -> draw++
                else -> {}
            }

            combinations++
        }

        // calculate odds
        odds[RESULT] = ((player1.toFloat() / combinations) * 100).roundToInt()
        Log.d("ODDS", odds.joinToString("\n"))
        return odds
    }

    /**
     * Update table cards
     * Remove card from deck and card combinations
     */
    fun updateCombinationCards(card: Card): Unit {
        // add card to table cards
        tableCards.add(card)

        // remove added card from card combinations
        cardCombinations.removeIf { it.any { it.toString() == card.toString() } }

        // remove added card from deck
        deck.removeIf { card.toString() == it.toString() }
    }
}