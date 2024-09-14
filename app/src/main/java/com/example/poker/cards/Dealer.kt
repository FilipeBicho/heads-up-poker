package com.example.poker.cards

import android.annotation.SuppressLint

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.poker.bot.FOLD
import com.example.poker.hand.FOUR_OF_A_KIND
import com.example.poker.hand.THREE_OF_A_KIND
import java.nio.channels.FileChannel

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
//        playerCards.add(deck.dealCard())
//        botCards.add(deck.dealCard())
//        playerCards.add(deck.dealCard())
//        botCards.add(deck.dealCard())

        // debug specific game
        playerCards.add(Card(SIX, CLUBS))
        playerCards.add(Card(TWO, CLUBS))
        botCards.add(Card(SEVEN, HEARTS))
        botCards.add(Card(EIGHT, CLUBS))
    }

    /**
     * Set flop
     */
    fun setFlopCards(tableCards: SnapshotStateList<Card>) {

        deck.dealCard()
//        for (i in 0 until 3) {
//            tableCards.add(deck.dealCard())
//        }

        // debug specific game
        tableCards.add(Card(SEVEN, SPADES))
        tableCards.add(Card(EIGHT, HEARTS))
        tableCards.add(Card(EIGHT, SPADES))
    }

    /**
     * Set turn
     */
    fun setTurnCard(tableCards: SnapshotStateList<Card>) {

        deck.dealCard()
     //   tableCards.add(deck.dealCard())

        // debug specific game
        tableCards.add(Card(TWO, SPADES))
    }

    /**
     * Set river
     */
    fun setRiverCard(tableCards: SnapshotStateList<Card>) {

        deck.dealCard()
        //tableCards.add(deck.dealCard())

        // debug specific game
        tableCards.add(Card(ACE, SPADES))
    }
}