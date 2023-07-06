package com.example.poker

import java.util.Arrays

class Odds (var playerCards: ArrayList<Card>, val tableCards: ArrayList<Card>, private var deck: ArrayList<Card>) {

    private var cardCombinations = mutableListOf<ArrayList<Card>>()
    private var usedCombinations = mutableListOf<ArrayList<Card>>()
    private var player1 = 0
    private var player2 = 0
    private var draw = 0
    private var combinations = 0

    init {
        setCardCombinations()
        flopOdds()

        println()
    }

    /**
     * create function to set all possible hand combinations
     */
    private fun setCardCombinations() {

        for (card1: Card in deck) {
            for (card2: Card in deck) {

                if (card1 == card2) {
                    continue
                }

                if (usedCombinations.isNotEmpty() && usedCombinations.contains(listOf(card1, card2))) {
                   continue
                }

                cardCombinations.add(arrayListOf(card1, card2))
                usedCombinations.add(arrayListOf(card1, card2))
                usedCombinations.add(arrayListOf(card2, card1))
            }
        }
    }

    /**
     * Calculate inner
     */
    private fun flopOdds() {

        val tempDeck: ArrayList<Card> = ArrayList()
        val tempTableCards: ArrayList<Card> = ArrayList()

        tempTableCards.addAll(tableCards.toList())
        for (opponentCards: ArrayList<Card> in cardCombinations) {
            tempDeck.addAll(deck.toList())
            tempDeck.remove(opponentCards.component1())
            tempDeck.remove(opponentCards.component2())
            for (card1Index in 0 until tempDeck.size) {

                tempTableCards.add(tempDeck[card1Index])
                for (card2Index: Int in 0 until tempDeck.size) {

                    if (deck[card1Index] === deck[card2Index]) {
                        continue
                    }

                    tempTableCards.add(tempDeck[card2Index])

                    val playerHand = Hand(playerCards, tempTableCards)
                    val opponentHand = Hand(opponentCards, tempTableCards)
                    when (HandWinnerCalculator(player1Hand = playerHand, player2Hand = opponentHand).getWinner()) {
                        1 -> player1++
                        2 -> player2++
                        3 -> draw++
                        else -> {}
                    }


                    tempTableCards.removeLast()
                    combinations++
                }
                tempTableCards.removeLast()
            }
            tempDeck.clear()
        }
    }

}