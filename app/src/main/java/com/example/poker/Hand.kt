package com.example.poker

class Hand(private var playerCards: ArrayList<Card>, var tableCards: ArrayList<Card>) {

    private var allCards = mutableListOf<Card>()
    private var hand = mutableListOf<Card>()
    private lateinit var suitRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private lateinit var rankRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private lateinit var flushCards: List<Card>
    var resultText: String = ""
    private var resultValue: Int = 0

    init {
        initCards()
        evaluateHand()
    }

    private fun evaluateHand() {

        if (isRoyalStraightFlush()) {
            resultValue = 10
            resultText = "Royal Straight Flush"
            return
        }

        if (isStraightFlush()) {
            resultValue = 9
            resultText = "Straight Flush"
            return
        }

        if (isFourOfAKind()) {
            resultValue = 8
            resultText = "Four of a Kind"
            return
        }

        if (isFullHouse()) {
            resultValue = 7
            resultText = "Full House"
            return
        }

        if (isFlush()) {
            resultValue = 6
            resultText = "Flush"
            return
        }

        if (isStraight()) {
            resultValue = 5
            resultText = "Straight"
            return
        }

        if (isThreeOfAKind()) {
            resultValue = 4
            resultText = "Three of a Kind"
            return
        }

        if (isTwoPair()) {
            resultValue = 3
            resultText = "Two Pair"
            return
        }

        if (isPair()) {
            resultValue = 2
            resultText = "Pair"
            return
        }

        highCards()
        resultValue = 1
        resultText = "High Card"
    }

    /**
     * Is Royal Straight Flush
     */
    private fun isRoyalStraightFlush(): Boolean {

        // is straight flush hand first card is an Ace
        if (isStraightFlush() && hand[0].rank == 0) {
            return true
        }

        hand.clear()
        return false
    }

    /**
     * Is Straight flush
     */
    private fun isStraightFlush(): Boolean {

        if (flushCards.size >= 5 && isStraight(flushCards.toMutableList())) {
            return true
        }

        hand.clear()
        return false
    }

    /**
     * Is Four of a Kind
     */
    private fun isFourOfAKind(): Boolean {

        if (rankRepeatedCards.first().value.size == 4) {
            hand.addAll(rankRepeatedCards.first().value)
            highCards()
            return true
        }

        return false
    }

    /**
     * Is Full House
     */
    private fun isFullHouse(): Boolean {

        val threeOfAKindList = rankRepeatedCards
            .filter { it.value.size == 3 }
            .sortedByDescending { it.key }
            .toMutableList()

        val pairList = rankRepeatedCards.filter { it.value.size == 2 }.sortedByDescending { it.key }
            .toMutableList()

        // there is no three of a kind
        if (threeOfAKindList.isEmpty()) {
            return false
        }

        // add highest three of a kind
        if (threeOfAKindList.any { it.key == 0 }) {
            hand.addAll(threeOfAKindList.removeLast().value)

        } else {
            hand.addAll(threeOfAKindList.removeFirst().value)
        }

        // if there is a pair of Ace
        if (pairList.isNotEmpty() && pairList.any { it.key == 0 }) {
            hand.addAll(pairList.removeLast().value)
            return true
        } else {
            // if there is another three of a kind
            if (threeOfAKindList.isNotEmpty()) {
                hand.addAll(threeOfAKindList.first().value.take(2))
                return true
            }

            // if there is 1 pair or more
            if (pairList.isNotEmpty()) {
                hand.addAll(pairList.first().value)
                return true
            }
        }

        hand.clear()
        return false
    }

    /**
     * Is Flush
     */
    private fun isFlush(): Boolean {

        // more than 5 cards with the same suit
        if (flushCards.size >= 5) {
            // has Ace
            if (flushCards.any { it.rank == 0 }) {
                hand.add(flushCards[0])
                for (index in flushCards.size - 1 downTo flushCards.size - 4) {
                    hand.add(flushCards[index])
                }
            } else {
                for (index in flushCards.size - 1 downTo flushCards.size - 5) {
                    hand.add(flushCards[index])
                }
            }
            return true
        }

        return false
    }

    /**
     * Is Straight
     */
    private fun isStraight(cards: MutableList<Card> = allCards): Boolean {

        var currentRank: Int?
        var nextCardRank: Int?
        var sequentialCardsCount: Int = 1
        var straight: Int = 1
        var lastSequentialCardRank: Int = -1
        var lastSequentialCardIndex: Int = -1

        for ((index, card) in cards.withIndex()) {
            // already checked all cards
            if (index + 1 == cards.size) {
                break
            }

            currentRank = card.rank
            nextCardRank = cards[index + 1].rank

            // next card has the same rank
            if (currentRank == nextCardRank) {
                continue;
            }

            // is a sequential card
            if (currentRank == nextCardRank - 1) {
                sequentialCardsCount++

                if (straight < sequentialCardsCount) {
                    straight = sequentialCardsCount
                    lastSequentialCardRank = nextCardRank
                    lastSequentialCardIndex = index + 1
                }


            } else {
                sequentialCardsCount = 1
            }
        }

        // has straight 10 to Ace
        if (straight >= 4 && cards.any { it.rank == 0 } && lastSequentialCardRank == 12) {
            hand.add(cards[0])
            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 3)) {
                hand.add(cards[index])
            }
            return true
        }

        // assign the biggest straight
        if (straight >= 5) {
            for (index in lastSequentialCardIndex downTo (lastSequentialCardIndex - 4)) {
                hand.add(cards[index])
            }
            return true
        }

        return false
    }

    /**
     * Is Three of a Kind
     */
    private fun isThreeOfAKind(): Boolean {

        val threeOfAKindHashMap = rankRepeatedCards
            .filter { it.value.size == 3 }
            .sortedByDescending { it.key }

        // there is no set with three of a kind
        if (threeOfAKindHashMap.isEmpty()) {
            return false
        }

        if (threeOfAKindHashMap.any { it.key == 0 }) {
            hand.addAll(threeOfAKindHashMap.last().value)
        } else {
            hand.addAll(threeOfAKindHashMap.first().value)
        }

        highCards()
        return true
    }

    /**
     * Is Two Pair
     */
    private fun isTwoPair(): Boolean {
        val pairList = rankRepeatedCards
            .filter { it.value.size == 2 }
            .sortedByDescending { it.key }
            .toMutableList()

        if (pairList.size > 1) {
            // has pair of Ace
            if (pairList.any { it.key == 0 }) {
                hand.addAll(pairList.last().value)
                hand.addAll(pairList.first().value)
            } else {
                hand.addAll(pairList.removeFirst().value)
                hand.addAll(pairList.removeFirst().value)
            }
            highCards()
            return true
        }

        return false
    }

    /**
     * Is Pair
     */
    private fun isPair(): Boolean {
        val pairList = rankRepeatedCards
            .filter { it.value.size == 2 }
            .sortedByDescending { it.key }
            .toMutableList()

        if (pairList.size == 1) {
            hand.addAll(pairList.first().value)
            highCards()
            return true
        }

        return false
    }

    /**
     * Set High cards
     */
    private fun highCards() {

        val otherCards: ArrayList<Card> = ArrayList()

        for (card: Card in allCards) {
            if (!hand.contains(card)) {
                otherCards.add(card)
            }
        }

        for (index: Int in hand.size..4) {
            if (otherCards.any { it.rank == 0 }) {
                hand.add(otherCards.removeFirst())
            } else {
                hand.add(otherCards.removeLast())
            }
        }
    }

    /**
     * Init cards
     */
    private fun initCards() {

        // join player and table cards
        allCards.addAll(0, this.playerCards)
        allCards.addAll(allCards.size, this.tableCards)

        // sort by rank
        allCards = allCards.sortedBy { it.rank }.toCollection(ArrayList())

        // group repeated cards
        suitRepeatedCards = allCards
            .groupBy { it.suit }
            .entries
            .sortedByDescending { it.value.size }
            .toMutableList()

        rankRepeatedCards = allCards
            .groupBy { it.rank }
            .entries
            .sortedByDescending { it.value.size }
            .toMutableList()

        // sort grouped suit cards by rank
        flushCards = suitRepeatedCards.first().value.sortedBy { it.rank }

    }

    /**
     * Return player hand
     */
    fun getHand() = hand;


}