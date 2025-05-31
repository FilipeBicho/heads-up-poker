package com.filipebicho.pokerclash.cards

import android.annotation.SuppressLint
import androidx.compose.material3.Card

import androidx.compose.runtime.snapshots.SnapshotStateList

const val PLAYER = 0
const val BOT = 1
const val PRE_FLOP = 0
const val FLOP = 1
const val TURN = 2
const val RIVER = 3

@SuppressLint("MutableCollectionMutableState")
class Dealer {

    private lateinit var deck: Deck

   fun shuffle() {
       deck = Deck()
   }

    /**
     * Set player cards
     */
    fun setPlayerCards(
        playerCards: SnapshotStateList<Card>,
        botCards: SnapshotStateList<Card>
    ) {
        playerCards.add(deck.dealCard())
        botCards.add(deck.dealCard())
        playerCards.add(deck.dealCard())
        botCards.add(deck.dealCard())

//        playerCards.add(Card(TWO, HEARTS))
//        playerCards.add(Card(TEN, CLUBS))
//        botCards.add(Card(ACE, SPADES))
//        botCards.add(Card(JACK, CLUBS))
    }

    /**
     * Set flop
     */
    fun setFlopCards(tableCards: SnapshotStateList<Card>) {

        deck.dealCard()
        for (i in 0 until 3) {
            tableCards.add(deck.dealCard())
        }
//
//        tableCards.add(Card(SIX, HEARTS))
//        tableCards.add(Card(JACK, DIAMONDS))
//        tableCards.add(Card(FIVE, DIAMONDS))
    }

    /**
     * Set turn
     */
    fun setTurnCard(tableCards: SnapshotStateList<Card>) {
        deck.dealCard()
        tableCards.add(deck.dealCard())

//        tableCards.add(Card(ACE, HEARTS))
    }

    /**
     * Set river
     */
    fun setRiverCard(tableCards: SnapshotStateList<Card>) {
        deck.dealCard()
        tableCards.add(deck.dealCard())
//        tableCards.add(Card(KING, HEARTS))
    }
}