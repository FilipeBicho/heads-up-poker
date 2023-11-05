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
        computerCards.add(deck.dealCard())
        playerCards.add(deck.dealCard())
        computerCards.add(deck.dealCard())
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