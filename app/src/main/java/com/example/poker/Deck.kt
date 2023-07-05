package com.example.poker

import kotlin.random.Random

class Deck {

    /**
     * Holds deck
     */
    private var deck = arrayListOf<Card>();

    init {
        // init cards
        for (rank in 0 until 12) {
            for (suit in 0 until 4) {
                deck.add(Card(rank, suit))
            }
        }

        // shuffle
        for (i in deck.size -1 downTo 1) {
            // Get random number
            val index = Random.nextInt(i + 1)
            // get card at random index
            val tempCard = deck[index]
            // set current card in a random index
            deck[index] = deck[i]
            // set random card in the current index
            deck[i] = tempCard;
        }
    }

    /**
     * Return deck
     */
    fun getDeck() = deck;

    /**
     * Remove card from the top of the deck
     */
    fun dealCard() = deck.removeAt(deck.size - 1);
}