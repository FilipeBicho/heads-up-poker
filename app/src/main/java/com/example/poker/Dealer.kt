package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.ArrayList

@SuppressLint("MutableCollectionMutableState")
class Dealer {

    private var player1Cards: ArrayList<Card> = ArrayList()
    private var player2Cards: ArrayList<Card> = ArrayList()
    private var tableCards by mutableStateOf(ArrayList<Card>())
    private var deck: Deck = Deck()

    /**
     * Deal cards to player1 and player 2
     */
    init {
        player1Cards.add(deck.dealCard())
        player2Cards.add(deck.dealCard())
        player1Cards.add(deck.dealCard())
        player2Cards.add(deck.dealCard())
    }

    fun getPlayer1Cards() = player1Cards

    fun getPlayer2Cards() = player2Cards

    /**
     * Deal flop
     */
    fun getFlop(): MutableList<Card> {
        // Burn card
        deck.dealCard();

        // deal flop
        for (i in 0 until 3) {
            tableCards.add(deck.dealCard())
        }

        return tableCards
    }

    /**
     * Deal turn
     */
    fun getTurn(): MutableList<Card> {
        // Burn card
        deck.dealCard();

        // deal turn
        tableCards.add(deck.dealCard())

        return tableCards
    }

    /**
     * Deal river
     */
    fun getRiver(): MutableList<Card> {
        // Burn card
        deck.dealCard();

        // deal river
        tableCards.add(deck.dealCard())

        return tableCards
    }
}