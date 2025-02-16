package com.filipebicho.pokerclash.odds

import android.util.Log
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.Deck
import com.filipebicho.pokerclash.hand.Hand
import com.filipebicho.pokerclash.hand.HandWinnerCalculator
import com.filipebicho.pokerclash.hand.RESULT
import kotlin.math.roundToInt
import kotlin.random.Random

const val MAX_COMBINATIONS = 1000

class Odds(private var allCombinations: MutableList<ArrayList<Card>>) {

    private var fullDeck = Deck().getDeck()
    private val flopOdds = Array(11) { _ -> 0}
    private val opponentFlopOdds = Array(11) { _ -> 0}
    private val turnOdds = Array(11) { _ -> 1}
    private val riverOdds = Array(11) { _ -> 1}
    private var showdownPlayerOdds = 0
    private var showdownOpponentOdds = 0

    /**
     * Calculate flop odds
     */
    fun calculateFlopOdds(holeCards: MutableList<Card>, tableCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val combinations = allCombinations.toMutableList()

        // remove hole and table cards from combinations
        for (card in holeCards + tableCards) {
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
                val hand = Hand(holeCards, tempTableCards)
                flopOdds[hand.resultValue]++

                // use 1st combination cards and table cards to calculate opponent hand
                val opponentHand = Hand(opponentCards, tempTableCards)
                opponentFlopOdds[opponentHand.resultValue]++

                // calculate winner
                when (HandWinnerCalculator(
                    player1Hand = hand,
                    player2Hand = opponentHand
                ).getWinner()) {
                    0 -> player1++
                    1 -> player2++
                    2 -> draw++
                    else -> {}
                }

                if (count >= MAX_COMBINATIONS) {
                    break
                }

                // remove temporarily table cards
                tempTableCards.removeLast()
                tempTableCards.removeLast()

                count++

            }
            combinations.shuffle()
        }

        // calculate odds
        for ((index, value) in flopOdds.withIndex()) {
            if (index == RESULT) {
                flopOdds[RESULT] = ((player1.toDouble()/count) * 100).roundToInt()
            } else {
                flopOdds[index] = ((value.toFloat() / count) * 100).roundToInt()
            }

            Log.d("ODDS",
                "${Hand.handRankToString(index)} - ${flopOdds[index]}"
            )
        }

        for ((index, value) in opponentFlopOdds.withIndex()) {
            if (index == RESULT) {
                opponentFlopOdds[RESULT] = ((player2.toDouble()/count) * 100).roundToInt()
            } else {
                opponentFlopOdds[index] = ((value.toFloat() / count) * 100).roundToInt()
            }
        }

        Log.d("------------------------------------", '0'.toString())
    }

    /**
     * calculate turn odds
     */
    fun calculateTurnOdds(holeCards: MutableList<Card>, tableCards: MutableList<Card>) {

        var player1 = 0
        var player2 = 0
        var draw = 0
        var count = 0
        val deck = fullDeck
        val combinations = allCombinations.toMutableList()
        val tempTableCards: ArrayList<Card> = ArrayList()

        // remove player and table cards from combinations
        for (card in holeCards + tableCards) {
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
                val hand = Hand(holeCards, tempTableCards)
                turnOdds[hand.resultValue]++

                // use combination cards and table cards to calculate opponent hand
                val opponentHand = Hand(opponentCards, tempTableCards)

                // calculate winner
                when (HandWinnerCalculator(
                    player1Hand = hand,
                    player2Hand = opponentHand
                ).getWinner()) {
                    0 -> player1++
                    1 -> player2++
                    2 -> draw++
                    else -> {}
                }

                if (count >= MAX_COMBINATIONS) {
                    break
                }

                tempTableCards.removeLast()
                count++
            }
            combinations.shuffle()
        }

        // calculate odds
        for ((index, value) in turnOdds.withIndex()) {

            if (index == RESULT) {
                turnOdds[RESULT] = ((player1.toDouble()/count) * 100).toInt()
            } else {
                turnOdds[index] = ((value.toFloat() / count) * 100).roundToInt()
            }

            Log.d("ODDS",
                "${Hand.handRankToString(index)} - ${turnOdds[index]}"
            )
        }

        Log.d("------------------------------------", '0'.toString())
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
        val hand = Hand(playerCards, tableCards)
        turnOdds[hand.resultValue] = 100

        for (opponentCards: ArrayList<Card> in combinations) {

            // use combination cards and table cards to calculate opponent hand
            val opponentHand = Hand(opponentCards, tableCards)

            // calculate winner
            when (HandWinnerCalculator(
                player1Hand = hand,
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
        riverOdds[RESULT] = ((player1.toDouble()/count) * 100).roundToInt()

        Log.d("ODDS river result", riverOdds[RESULT].toString())
        Log.d("------------------------------------", '0'.toString())
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
            val botHand = Hand(opponentCards, tempTableCards)

            // calculate winner
            when (HandWinnerCalculator(
                player1Hand = playerHand,
                player2Hand = botHand
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
            val botHand = Hand(opponentCards, tempTableCards)

            // calculate winner
            when (HandWinnerCalculator(
                player1Hand = playerHand,
                player2Hand = botHand
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

    fun getFlopOdds() = flopOdds

    fun getOpponentFlopOdds() = opponentFlopOdds

    fun getTurnOdds() = turnOdds

    fun getRiverOdds() = riverOdds

    fun getShowdownPlayerOdds() = showdownPlayerOdds

    fun getShowdownOpponentOdds() = showdownOpponentOdds
}