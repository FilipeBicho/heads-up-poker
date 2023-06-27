package com.example.poker

import kotlin.random.Random

class Deck {

    private var card = arrayListOf<Card>();

    init {

        // init cards
        for (rank in 0 until 12) {
            for (suit in 0 until 4) {
                card.add(Card(rank, suit))
            }
        }

        for (i in card.size -1 downTo 1) {
            // Get random number
            val index = Random.nextInt(i + 1)

            // get card at random index
            val tempCard = card[index]

            // set current card in a random index
            card[index] = card[i]

            // set random card in the current index
            card[i] = tempCard;
        }
    }

    /**
     * Remove card from the top of the deck
     */
    fun dealCard() = card.removeAt(card.size - 1);
}