package com.example.poker.helper

import androidx.compose.animation.core.tween
import com.example.poker.ACE
import com.example.poker.Card
import com.example.poker.EIGHT
import com.example.poker.FIVE
import com.example.poker.FOUR
import com.example.poker.JACK
import com.example.poker.KING
import com.example.poker.NINE
import com.example.poker.QUEEN
import com.example.poker.SEVEN
import com.example.poker.SIX
import com.example.poker.TEN
import com.example.poker.THREE
import com.example.poker.TWO

/**
 * Slansky hand groups
 * @see https://en.wikipedia.org/wiki/Texas_hold_%27em_starting_hands
 *
 */
data class HandGroup(val playerCards: List<Card>) {
    var group: Int = getHandGroup(playerCards)

    private fun getHandGroup(playerCards: List<Card>): Int {
        
        val cards = playerCards.sortedByDescending { it.rank }.toList()
        val card1Rank = cards.first().rank
        val card1Suit = cards.first().suit
        val card2Rank = cards.last().rank
        val card2Suit = cards.last().suit


        val suited = card1Suit == card2Suit
        val pair = card1Rank == card2Rank

        if (pair) {
            // group 1: AA, KK or JJ
            if (card1Rank == ACE || card1Rank == KING || card1Rank == QUEEN) {
                return 1
            }

            // group 2: 1010
            if (card1Rank == TEN) {
                return 2
            }

            // group 3: 99
            if (card1Rank == NINE) {
                return 3
            }

            // group 4: 88
            if (card1Rank == EIGHT) {
                return 4
            }

            // group 5: 77
            if (card1Rank == SEVEN) {
                return 5
            }

            // group 6: 66 or 55
            if (card1Rank == SIX || card1Rank == FIVE) {
                return 6
            }

            // group 7: 44, 33, 22
            if (card1Rank == FOUR || card1Rank == THREE || card1Rank == TWO) {
                return 7
            }

        } else {
            if (suited) {
                // group 1: AKs
                if (card2Rank == ACE && card1Rank == KING) {
                    return 1
                }

                // group 2: AQs, AJs or KQs
                if ((card2Rank == ACE && (card1Rank == QUEEN || card1Rank == JACK)) ||
                    (card1Rank == KING && card2Rank == QUEEN)) {
                    return 2
                }

                // group 3: A10s, KJs, QJs or J10s
                if ((card2Rank == ACE && card1Rank == TEN) ||
                    (card2Rank == JACK && (card1Rank == KING || card1Rank == QUEEN)) ||
                    (card1Rank == JACK && card2Rank == TEN)) {
                    return 3
                }

                // group 4: K10s, Q10s, J10s, J9s or 98s
                if ((card2Rank == TEN && (card1Rank == KING || card1Rank == QUEEN)) ||
                    (card2Rank == NINE && (card1Rank == JACK || card1Rank == TEN)) ||
                    (card1Rank == NINE && card2Rank == EIGHT)) {
                    return 4
                }

                // group 5: A9s to A2s, Q9s, 108s, 97s, 87s or 76s
                if ((card2Rank == ACE && (card1Rank in TWO..NINE)) ||
                    (card1Rank == QUEEN && card2Rank == NINE) ||
                    (card1Rank == TEN && card2Rank == EIGHT) ||
                    (card2Rank == SEVEN && (card1Rank == NINE || card1Rank == EIGHT)) ||
                    (card1Rank == SEVEN && card2Rank == SIX)) {
                    return 5
                }

                // group 6: K9s, J8s, 86s, 75s or 54s
                if ((card1Rank == KING && card2Rank == NINE) ||
                    (card1Rank == JACK && card2Rank == EIGHT) ||
                    (card1Rank == EIGHT && card2Rank == SIX) ||
                    (card1Rank == SEVEN && card2Rank == FIVE) ||
                    (card1Rank == FIVE && card2Rank == FOUR)) {
                    return 6
                }

                // group 7: K8s to K2s, Q8s, 107s, 64s, 53s or 43s
                if ((card1Rank == KING && (card2Rank in TWO..EIGHT)) ||
                    (card1Rank == QUEEN && card2Rank == EIGHT) ||
                    (card1Rank == TEN && card2Rank == SEVEN) ||
                    (card1Rank == SIX && card2Rank == FOUR) ||
                    (card2Rank == THREE && (card1Rank == FIVE || card1Rank == FOUR))) {
                    return 7
                }

                // group 8: J7s, 96s, 85s, 74s, 42s or 32s
                if ((card1Rank == JACK && card2Rank == SEVEN) ||
                    (card1Rank == NINE && card2Rank == SIX) ||
                    (card1Rank == EIGHT && card2Rank == FIVE) ||
                    (card1Rank == SEVEN && card2Rank == FOUR) ||
                    (card2Rank == TWO && (card1Rank == FOUR || card1Rank == THREE))) {
                    return 8
                }

            } else {
                // group 2: AKo
                if (card2Rank == ACE && card1Rank == KING) {
                    return 2
                }

                // group 3: AQo
                if (card2Rank == ACE && card1Rank == QUEEN) {
                    return 3
                }

                // group 4: AJo or KQo
                if ((card2Rank == ACE && card1Rank == JACK) ||
                    (card1Rank == KING && card2Rank == QUEEN)) {
                    return 4
                }

                // group 5: KJo, QJo or J10o
                if ((card2Rank == JACK && (card1Rank == KING || card1Rank == QUEEN)) ||
                    (card1Rank == JACK && card2Rank == TEN)) {
                    return 5
                }

                // group 6: A10o, K10o or Q10o
                if ((card2Rank == ACE && card1Rank == TEN) ||
                    (card2Rank == TEN && (card1Rank == KING || card1Rank == QUEEN))) {
                    return 6
                }

                // group 7: J9o, 109o or 98o
                if ((card2Rank == NINE && (card1Rank == JACK || card1Rank == TEN)) ||
                    (card1Rank == NINE && card2Rank == EIGHT)) {
                    return 7
                }

                // group 8: A9o, K9o, Q9o, J8o, 108o, 87o, 76o, 65o or 54o
                if ((card2Rank == ACE && card1Rank == NINE) ||
                    (card2Rank == NINE && (card1Rank == KING || card1Rank == QUEEN)) ||
                    (card2Rank == EIGHT && (card1Rank == JACK || card1Rank == TEN)) ||
                    (card1Rank == EIGHT && card2Rank == SEVEN) ||
                    (card1Rank == SEVEN && card2Rank == SIX) ||
                    (card1Rank == SIX && card2Rank == FIVE) ||
                    (card1Rank == FIVE && card2Rank == FOUR)) {
                    return 8
                }
            }
        }

        return 9

    }
}
