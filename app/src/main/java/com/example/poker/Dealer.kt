package com.example.poker

import java.util.ArrayList

class Dealer {

    /**
     * Deal cards to player1 and player 2
     */
    fun dealCards(deck: Deck, player1: ArrayList<Card>, player2: ArrayList<Card>) {
        player1.add(deck.dealCard())
        player2.add(deck.dealCard())
        player1.add(deck.dealCard())
        player2.add(deck.dealCard())
    }

    /**
     * Deal flop
     */
    fun flop(deck: Deck, table: ArrayList<Card>) {
        // Burn card
        deck.dealCard();

        // deal flop
        for (i in 0 until 3) {
            table.add(deck.dealCard())
        }
    }

    /**
     * Deal turn
     */
    fun turn(deck: Deck, table: ArrayList<Card>) {
        // Burn card
        deck.dealCard();

        // deal turn
        table.add(deck.dealCard())
    }

    /**
     * Deal river
     */
    fun river(deck: Deck, table: ArrayList<Card>) {
        // Burn card
        deck.dealCard();

        // deal river
        table.add(deck.dealCard())
    }
}