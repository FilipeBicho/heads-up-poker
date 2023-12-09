package com.example.poker

import android.util.Log
import kotlin.math.roundToInt

const val MAX_OPPONENT_COMBINATIONS = 100
const val MAX_TABLE_COMBINATIONS = 50

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
        var opponentCombinationsCount = 0
        var tableCombinationsCount: Int

        setCardCombinations(playerCards + tableCards)

        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        for (opponentCards: ArrayList<Card> in cardCombinations) {
            cardCombinations.shuffle()
            tableCombinationsCount = 0
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

                tableCombinationsCount++
                if (tableCombinationsCount >= MAX_TABLE_COMBINATIONS) {
                    break
                }
            }

            opponentCombinationsCount++
            if (opponentCombinationsCount >= MAX_OPPONENT_COMBINATIONS) {
                break
            }
        }

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        Log.d("ODDS", odds.joinToString("\n"))
        Log.d("ODDS player1", player1.toString())
        Log.d("ODDS player2", player2.toString())
        Log.d("ODDS draw", draw.toString())
        Log.d("ODDS combinations", combinations.toString())
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

            if (combinations >= MAX_TABLE_COMBINATIONS * MAX_OPPONENT_COMBINATIONS) {
                break
            }
        }

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / combinations) * 100).roundToInt()
        }

        // calculate odds
        odds[RESULT] = ((player1.toDouble()/combinations) * 100).toInt()

        Log.d("ODDS", odds.joinToString("\n"))
        Log.d("ODDS player1", player1.toString())
        Log.d("ODDS player2", player2.toString())
        Log.d("ODDS draw", draw.toString())
        Log.d("ODDS combinations", combinations.toString())

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
                1 -> player1++
                2 -> player2++
                3 -> draw++
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
            val usedCombinations = mutableListOf<ArrayList<Card>>()

            // remove player cards
            for (card in removeCards) {
                deck.removeIf { card.toString() == it.toString() }
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
    }
}