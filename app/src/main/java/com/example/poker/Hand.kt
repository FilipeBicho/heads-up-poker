package com.example.poker

class Hand(private var playerCards: ArrayList<Card>, var tableCards: ArrayList<Card>) {

    private var hand = mutableListOf<Card>()
    private var suitRepeatedCards = LinkedHashMap<Int, List<Card>>()
    private var rankRepeatedCards = LinkedHashMap<Int, List<Card>>()

    init {
        setHand()

        // sort by rank
        hand = hand.sortedBy { it.rank }.toCollection(ArrayList())

        // group repeated cards
        this.suitRepeatedCards = hand.groupBy { it.suit } as LinkedHashMap<Int, List<Card>>
        this.rankRepeatedCards = hand.groupBy { it.rank } as LinkedHashMap<Int, List<Card>>
    }

    /**
     * Set hand by merging player and table cards
     */
    private fun setHand() {
        hand.addAll(0, this.playerCards)
        hand.addAll(hand.size, this.tableCards)
    }

    /**
     * Return player hand
     */
    fun getHand() = hand;


}