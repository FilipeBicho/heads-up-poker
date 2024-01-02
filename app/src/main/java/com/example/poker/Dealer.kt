package com.example.poker

import android.annotation.SuppressLint

import androidx.compose.runtime.snapshots.SnapshotStateList

const val PLAYER = 0
const val COMPUTER = 1
const val PRE_FLOP = 0
const val FLOP = 1
const val TURN = 2
const val RIVER = 3

@SuppressLint("MutableCollectionMutableState")
class Dealer {

    private var deck: Deck = Deck()

    /**
     * Set player cards
     */
    fun setPlayerCards(
        playerCards: SnapshotStateList<Card>,
        computerCards: SnapshotStateList<Card>
    ) {
        playerCards.add(deck.dealCard())
        computerCards.add(Card(1, 3))
        deck.getDeck().remove(Card(1, 3))
        playerCards.add(deck.dealCard())
        computerCards.add(Card(8, 1))
        deck.getDeck().remove(Card(8, 1))
    }

    /**
     * Set flop
     */
    fun setFlopCards(tableCards: SnapshotStateList<Card>) {

        deck.dealCard();
       // for (i in 0 until 3) {
        tableCards.add(Card(7, 1))
        deck.getDeck().remove(Card(7, 1))

        tableCards.add(Card(6, 0))
        deck.getDeck().remove(Card(6, 0))

        tableCards.add(Card(10, 3))
        deck.getDeck().remove(Card(10, 3))
       // }
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