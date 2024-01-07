package com.example.poker

data class Combinations(private var usedCards: MutableList<Card>) {
    var combinations = getCardCombinations(usedCards)

    /**
     * get card combinations without given used cards
     */
    private fun getCardCombinations(usedCards: List<Card>): MutableList<ArrayList<Card>> {

        val usedCombinations = mutableMapOf<String, ArrayList<Card>>()
        val cardCombinations = mutableListOf<ArrayList<Card>>()
        val deck = Deck().getDeck()

        if (usedCards.isNotEmpty()) {

            // remove used cards
            for (card in usedCards) {
                deck.removeIf { card.toString() == it.toString() }
            }

            for (card1: Card in deck) {
                for (card2: Card in deck) {
                    if (card1 == card2) {
                        continue
                    }

                    val combination1 = "$card1-$card2"
                    val combination2 = "$card2-$card1"

                    // prevent repetitions
                    if (usedCombinations.isNotEmpty()
                        && (usedCombinations.contains(combination1)
                                || usedCombinations.contains(combination2))
                    ) {
                        continue
                    }

                    // add combination
                    cardCombinations.add(arrayListOf(card1, card2))

                    // used to prevent repetitions
                    usedCombinations[combination1] = arrayListOf(card1, card2)
                    usedCombinations[combination2] = arrayListOf(card2, card1)
                }
            }
        }

        return cardCombinations
    }
}
