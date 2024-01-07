package com.example.poker

import android.util.Log
import kotlin.math.roundToInt
import kotlin.random.Random

const val MAX_COMBINATIONS = 1000

class Odds(private var allCombinations: MutableList<ArrayList<Card>>) {

    private var fullDeck = Deck().getDeck()
    private val odds = Array(11) { _ -> 0}
    private var flopOdds = 0
    private var turnOdds = 0
    private var riverOdds = 0
    private var showdownPlayerOdds = 0
    private var showdownOpponentOdds = 0

    /**
     * Calculate flop odds
     */
    fun calculateFlopOdds(playerCards: MutableList<Card>, tableCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val combinations = allCombinations.toMutableList()

        // remove player and table cards from combinations
        for (card in playerCards + tableCards) {
            combinations.removeIf { it.any { it.toString() == card.toString() } }
        }

        // init table cards
        val tempTableCards: ArrayList<Card> = ArrayList()
        tempTableCards.addAll(tableCards.toList())

        while (count < MAX_COMBINATIONS) {
            for (i in 0 until combinations.size - 2) {

                // add 1st combination cards to opponent
                val opponentCards = combinations[i]

                // continue if 2nd combination contains any card from the 1st combination
                if (opponentCards.contains(combinations[i+1].component1())
                    || opponentCards.contains(combinations[i+1].component2())
                ) {
                    continue
                }

                // add 2nd combination cards to table cards
                tempTableCards.addAll(combinations[i+1])

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

                if (count >= MAX_COMBINATIONS) {
                    break;
                }

                // remove temporarily table cards
                tempTableCards.removeLast()
                tempTableCards.removeLast()

                count++

            }
            combinations.shuffle()
        }

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / count) * 100).roundToInt()
        }

        Log.d("ODDS", odds.joinToString("\n"))
        Log.d("ODDS flop combination", count.toString())
        Log.d("ODDS result", ((player1.toDouble()/count) * 100).roundToInt().toString())

        // calculate odds
        flopOdds = ((player1.toDouble()/count) * 100).roundToInt()
    }

    /**
     * calculate turn odds
     */
    fun calculateTurnOdds(playerCards: MutableList<Card>, tableCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val deck = fullDeck
        val combinations = allCombinations.toMutableList()
        val tempTableCards: ArrayList<Card> = ArrayList()

        // remove player and table cards from combinations
        for (card in playerCards + tableCards) {
            combinations.removeIf { it.any { it.toString() == card.toString() } }
            deck.removeIf { card.toString() == it.toString() }
        }

        tempTableCards.addAll(tableCards.toList())

        while (count < MAX_COMBINATIONS) {
            for (i in 0 until combinations.size - 1) {

                val turnCard = deck[Random.nextInt((deck.size-1) + 1)]
                val opponentCards = combinations[i]

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

                if (count >= MAX_COMBINATIONS) {
                    break;
                }

                tempTableCards.removeLast()
                count++
            }
            combinations.shuffle()
        }

        Log.d("ODDS turn combinations", count.toString())

        // calculate odds per hand ranking
        for ((index, value) in odds.withIndex()) {
            odds[index] = ((value.toFloat() / count) * 100).roundToInt()
        }

        // calculate odds
        odds[RESULT] = ((player1.toDouble()/count) * 100).toInt()

        // calculate odds
        turnOdds = ((player1.toDouble()/count) * 100).roundToInt()
    }

    /**
     * calculate river odds
     */
    fun calculateRiverOdds(playerCards: MutableList<Card>, tableCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val combinations = allCombinations.toMutableList()

        // remove player and table cards from combinations
        for (card in playerCards + tableCards) {
            combinations.removeIf { it.any { it.toString() == card.toString() } }
        }

        // use table cards to calculate player hand
        val playerHand = Hand(playerCards, tableCards)
        odds[playerHand.resultValue] = 100

        for (opponentCards: ArrayList<Card> in combinations) {

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

            count++
        }

        // calculate odds
        riverOdds = ((player1.toDouble()/count) * 100).roundToInt()
    }

    /**
     * calculate showdown flop odds
     */
    fun calculateShowdownFlopOdds(playerCards: MutableList<Card>, opponentCards: MutableList<Card>, tableCards: MutableList<Card>)
    {
        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val combinations = allCombinations.toMutableList()
        val tempTableCards: ArrayList<Card> = ArrayList()

        // remove player and table cards from combinations
        for (card in playerCards + opponentCards + tableCards) {
            combinations.removeIf { it.any { it.toString() == card.toString() } }
        }

        tempTableCards.addAll(tableCards)

        for (cards: ArrayList<Card> in combinations) {
            // add table cards
            tempTableCards.addAll(cards)

            // use table cards to calculate player hand
            val playerHand = Hand(playerCards, tempTableCards)
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

            count++

            // remove temporarily table cards
            tempTableCards.removeLast()
            tempTableCards.removeLast()
        }

        showdownPlayerOdds = ((player1.toDouble()/count) * 100).roundToInt()
        showdownOpponentOdds = ((player2.toDouble()/count) * 100).roundToInt()
    }

    /**
     * calculate showdown turn odds
     */
    fun calculateShowdownTurnOdds(playerCards: MutableList<Card>, opponentCards: MutableList<Card>, tableCards: MutableList<Card>)
    {
        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val deck = fullDeck
        val tempTableCards: ArrayList<Card> = ArrayList()

        // remove player and table cards from combinations
        for (card in playerCards + opponentCards + tableCards) {
            deck.removeIf { card.toString() == it.toString() }
        }

        tempTableCards.addAll(tableCards)

        for (card: Card in deck) {
            // add table cards
            tempTableCards.add(card)

            // use table cards to calculate player hand
            val playerHand = Hand(playerCards, tempTableCards)
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

            count++

            // remove temporarily river card
            tempTableCards.removeLast()
        }

        showdownPlayerOdds = ((player1.toDouble()/count) * 100).roundToInt()
        showdownOpponentOdds = ((player2.toDouble()/count) * 100).roundToInt()
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
     * get showdown player odds
     */
    fun getShowdownPlayerOdds() = showdownPlayerOdds

    /**
     * get showdown opponent odds
     */
    fun getShowdownOpponentOdds() = showdownOpponentOdds
}