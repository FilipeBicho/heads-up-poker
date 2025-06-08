package com.filipebicho.pokerclash.odds

import android.util.Log
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.Deck
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.hand.Hand
import com.filipebicho.pokerclash.hand.HandWinnerCalculator
import com.filipebicho.pokerclash.hand.RESULT
import kotlin.math.roundToInt
import kotlin.random.Random

const val MAX_COMBINATIONS = 1000

class Odds(private var allCombinations: MutableList<ArrayList<Card>>) {

    private var fullDeck = Deck().getDeck()
    private var showdownPlayerOdds = 0
    private var showdownOpponentOdds = 0

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
            tempTableCards.removeAt(tempTableCards.lastIndex)
            tempTableCards.removeAt(tempTableCards.lastIndex)
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
            tempTableCards.removeAt(tempTableCards.lastIndex)
        }

        showdownPlayerOdds = ((player1.toDouble()/count) * 100).roundToInt()
        showdownOpponentOdds = ((player2.toDouble()/count) * 100).roundToInt()
    }

    fun calculateShowdownRiverOdds(playerCards: MutableList<Card>, opponentCards: MutableList<Card>, tableCards: MutableList<Card>)
    {
        val playerHand = Hand(playerCards, tableCards)
        val botHand = Hand(opponentCards, tableCards)
        val winner = HandWinnerCalculator(player1Hand = playerHand, player2Hand = botHand).getWinner()

        if (winner == PLAYER) {
            showdownPlayerOdds = 100
            showdownOpponentOdds = 0
        } else if (winner == BOT) {
            showdownPlayerOdds = 0
            showdownOpponentOdds = 100
        } else {
            showdownPlayerOdds = 50
            showdownOpponentOdds = 50
        }
    }

    fun getShowdownPlayerOdds() = showdownPlayerOdds

    fun getShowdownBotOdds() = showdownOpponentOdds
}