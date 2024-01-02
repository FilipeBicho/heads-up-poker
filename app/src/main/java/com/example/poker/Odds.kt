package com.example.poker

import android.util.Log
import java.util.Collections
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val MAX_COMBINATIONS = 1000

class Odds(private var tableCards: MutableList<Card>) {

    private var cardCombinations = mutableListOf<ArrayList<Card>>()
    private var deck = Deck().getDeck()
    private val odds = Array(11) { _ -> 0}
    private var flopOdds = 0
    private var turnOdds = 0
    private var riverOdds = 0

    /**
     * Calculate flop odds
     */
    fun calculateFlopOdds(playerCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0

        setCardCombinations(playerCards + tableCards)

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        while (combinations < MAX_COMBINATIONS) {
            for (i in 0 until cardCombinations.size - 2) {

                val opponentCards = cardCombinations[i]
                val tableCards = cardCombinations[i+1]

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
                    0 -> player1++
                    1 -> player2++
                    2 -> draw++
                    else -> {}
                }

                combinations++

                // remove temporarily table cards
                tempTableCards.removeLast()
                tempTableCards.removeLast()

            }
            cardCombinations.shuffle()
        }


        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        Log.d("ODDS", odds.joinToString("\n"))
        Log.d("ODDS comparisons", combinations.toString())
        Log.d("ODDS result", ((player1.toDouble()/combinations) * 100).roundToInt().toString())

        // calculate odds
        flopOdds = ((player1.toDouble()/combinations) * 100).roundToInt()
    }

    /**
     * calculate turn odds
     */
    fun calculateTurnOdds(playerCards: MutableList<Card>, turnCard: Card) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0

        updateCombinationCards(turnCard)

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        while (combinations < MAX_COMBINATIONS) {
            for (i in 0 until cardCombinations.size - 1) {

                val turnCard = deck[Random.nextInt((deck.size-1) + 1)]
                val opponentCards = cardCombinations[i]

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
                    0 -> player1++
                    1 -> player2++
                    2 -> draw++
                    else -> {}
                }

                tempTableCards.removeLast()
                combinations++
            }
            cardCombinations.shuffle()
        }

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        // calculate odds
        odds[RESULT] = ((player1.toDouble()/combinations) * 100).toInt()

        // calculate odds
        turnOdds = ((player1.toDouble()/combinations) * 100).roundToInt()
    }

    /**
     * calculate river odds
     */
    fun calculateRiverOdds(playerCards: MutableList<Card>, riverCard: Card) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var combinations = 0

        updateCombinationCards(riverCard)

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
                0 -> player1++
                1 -> player2++
                2 -> draw++
                else -> {}
            }

            combinations++
        }

        // calculate odds
        riverOdds = ((player1.toDouble()/combinations) * 100).roundToInt()
    }

    /**
     * get flop odds
     */
    fun getFlopOdds() = flopOdds

    /**
     * get turn odds
     */
    fun getTurnOdds() = turnOdds

    /**
     * get river odds
     */
    fun getRiverOdds() = riverOdds

    /**
     * Update table cards
     * Remove card from deck and card combinations
     */
    private fun updateCombinationCards(card: Card): Unit {

        // remove added card from card combinations
        cardCombinations.removeIf { it.any { it.toString() == card.toString() } }

        // remove added card from deck
        deck.removeIf { card.toString() == it.toString() }
    }

    /**
     * set card combinations
     */
    private fun setCardCombinations(removeCards: List<Card>)
    {
        if (removeCards.isNotEmpty()) {
            val usedCombinations = mutableMapOf<String, ArrayList<Card>>()

            // remove player cards
            for (card in removeCards) {
                deck.removeIf { card.toString() == it.toString() }
            }

            for (card1: Card in deck) {
                for (card2: Card in deck) {
                    if (card1 == card2) {
                        continue
                    }

                    val combination1 = "$card1-$card2"
                    val combination2 = "$card2-$card1"

                    // prevent repetitions
                    if (usedCombinations.isNotEmpty()
                        && (usedCombinations.contains(combination1)
                                || usedCombinations.contains(combination2))
                    ) {
                        continue
                    }

                    // add combination
                    cardCombinations.add(arrayListOf(card1, card2))

                    // used to prevent repetitions
                    usedCombinations[combination1] = arrayListOf(card1, card2)
                    usedCombinations[combination2] = arrayListOf(card2, card1)
                }
            }
        }
    }
}