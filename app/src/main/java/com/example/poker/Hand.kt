package com.example.poker

const val ROYAL_STRAIGHT_FLUSH: Int = 10
const val STRAIGHT_FLUSH: Int = 9
const val FOUR_OF_A_KIND: Int = 8
const val FULL_HOUSE: Int = 7
const val FLUSH: Int = 6
const val STRAIGHT: Int = 5
const val THREE_OF_A_KIND: Int = 4
const val TWO_PAIR: Int = 3
const val PAIR: Int = 2
const val HIGH_CARD: Int = 1
const val RESULT = 0

class Hand(private var playerCards: List<Card>, private var tableCards: List<Card>) {

    private var allCards = mutableListOf<Card>()
    private var hand = mutableListOf<Card>()
    private lateinit var suitRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private lateinit var rankRepeatedCards: MutableList<Map.Entry<Int, List<Card>>>
    private lateinit var flushCards: List<Card>
    var resultText: String = ""
    var resultValue: Int = 0

    init {
        initCards()
        evaluateHand()
    }

    private fun evaluateHand() {
        if (isRoyalStraightFlush()) {
            resultValue = ROYAL_STRAIGHT_FLUSH
            resultText = "Royal Straight Flush"
            return
        }

        if (isStraightFlush()) {
            resultValue = STRAIGHT_FLUSH
            resultText = "Straight Flush"
            return
        }

        if (isFourOfAKind()) {
            resultValue = FOUR_OF_A_KIND
            resultText = "Four of a Kind"
            return
        }

        if (isFullHouse()) {
            resultValue = FULL_HOUSE
            resultText = "Full House"
            return
        }

        if (isFlush()) {
            resultValue = FLUSH
            resultText = "Flush"
            return
        }

        if (isStraight()) {
            resultValue = STRAIGHT
            resultText = "Straight"
            return
        }

        if (isThreeOfAKind()) {
            resultValue = THREE_OF_A_KIND
            resultText = "Three of a Kind"
            return
        }

        if (isTwoPair()) {
            resultValue = TWO_PAIR
            resultText = "Two Pair"
            return
        }

        if (isPair()) {
            resultValue = PAIR
            resultText = "Pair"
            return
        }

        highCards()
        resultValue = HIGH_CARD
        resultText = "High Card"
    }

    /**
     * Is Royal Straight Flush
     */
    private fun isRoyalStraightFlush(): Boolean {
        // is straight flush hand first card is an Ace
        if (isStraightFlush() && hand[0].rank == ACE) {
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
        if (rankRepeatedCards[0].value.size == 4) {
            hand.addAll(rankRepeatedCards[0].value)
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
                hand.addAll(threeOfAKindList[0].value.take(2))
                return true
            }

            // if there is 1 pair or more
            if (pairList.isNotEmpty()) {
                hand.addAll(pairList[0].value)
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
            if (flushCards.any { it.rank == ACE }) {
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
    private fun isStraight(cards: MutableList<Card> = allCards.distinctBy { it.rank }.toMutableList()): Boolean {
        var currentRank: Int?
        var nextCardRank: Int?
        var sequentialCardsCount: Int = 0
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
            if (currentRank == (nextCardRank - 1)) {
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
        if (straight >= 4 && cards.any { it.rank == ACE } && lastSequentialCardRank == KING) {
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
            hand.addAll(threeOfAKindHashMap[0].value)
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
            if (pairList.any { it.key == ACE }) {
                hand.addAll(pairList.last().value)
                hand.addAll(pairList[0].value)
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
            hand.addAll(pairList[0].value)
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
            if (otherCards.any { it.rank == ACE }) {
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
        flushCards = suitRepeatedCards[0].value.sortedBy { it.rank }
    }

    /**
     * Return player hand
     */
    fun getHand() = hand;
}