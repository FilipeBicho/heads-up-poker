package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.runtime.snapshots.SnapshotStateList

@SuppressLint("MutableCollectionMutableState")
class Dealer(player1Cards: SnapshotStateList<Card>, player2Cards: SnapshotStateList<Card>) {

    private var deck: Deck = Deck()

    /**
     * Set player cards
     */
    fun setPlayerCards(
        player1Cards: SnapshotStateList<Card>,
        player2Cards: SnapshotStateList<Card>
    ) {
        player1Cards.add(deck.dealCard())
        player2Cards.add(deck.dealCard())
        player1Cards.add(deck.dealCard())
        player2Cards.add(deck.dealCard())
    }

    /**
     * Set flop
     */
    fun setFlopCards(tableCards: SnapshotStateList<Card>) {

        deck.dealCard();
        for (i in 0 until 3) {
            tableCards.add(deck.dealCard())
        }
    }

    /**
     * Set turn
     */
    fun setTurnCard(tableCards: SnapshotStateList<Card>) {

        deck.dealCard();
        tableCards.add(deck.dealCard())
    }

    /**
     * Set river
     */
    fun setRiverCard(tableCards: SnapshotStateList<Card>) {

        deck.dealCard();
        tableCards.add(deck.dealCard())
    }
}