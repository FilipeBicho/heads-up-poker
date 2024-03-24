package com.example.poker.bot

import com.example.poker.BIG_BLIND
import com.example.poker.Bot
import com.example.poker.CALL
import com.example.poker.CHECK
import com.example.poker.Card
import com.example.poker.FOLD
import com.example.poker.PLAYER
import com.example.poker.helper.HandGroup

class PreFlopBot(cards: List<Card>, isDealer: Boolean):
    Bot(null, cards, null, isDealer) {

    private var handRank: Int = 0

    init {
        handRank = HandGroup(cards).group
        this.isDealer = isDealer
    }

    private fun calculateAction(pokerChips: IntArray, bet: IntArray, validActions: BooleanArray): Int {

        // Dealer: player called - check and bet available
        // Blind: player didn't play yet - fold, call and bet available
        if (bet[PLAYER] == BIG_BLIND) {
            // less than 160 chips
            if (botStack < 4) {
                if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(2) }
                        in 4..6 -> if (hasHandPair) { betBlinds(2) } else { CALL }
                        7 -> if (hasHandPair) { CALL } else { FOLD }
                        else -> FOLD
                    }
                } else {
                    // player called - check and bet available
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> betBlinds(2)
                        in 4..5 -> if (hasHandPair) { betBlinds(2) } else { CHECK }
                        in 6..7 -> if (hasHandPair) { CHECK } else { FOLD }
                        else -> FOLD
                    }
                }
            }

            // between 160 and 320 chips
            if (botStack in 4..8) {
                if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(4) }
                        4 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(1) }
                        5 -> if (hasHandPair) { betBlinds(4) } else { CALL }
                        6 -> if (hasHandPair) { betBlinds(2) } else { FOLD }
                        else -> if (hasHandPair) { CALL } else { FOLD }
                    }
                } else {
                    // player called - check and bet available
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(3) }
                        4 -> if (hasHandPair) { betBlinds(3) } else { betBlinds(1) }
                        5 -> if (hasHandPair) { betBlinds(3) } else { CHECK }
                        6 -> if (hasHandPair) { betBlinds(1) } else { CHECK }
                        else -> CHECK
                    }
                }
            }

            // between 320 and 600 chips
            if (botStack in 8..15) {
                if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    return when (handRank) {
                        1 -> allIn()
                        2 -> if (hasHandPair) { betBlinds(8) } else { betBlinds(4) }
                        3 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(3) }
                        4 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(1) }
                        in 5..6 -> if (hasHandPair) { betBlinds(3) } else { CALL }
                        else -> if (hasHandPair) { CALL } else { FOLD }
                    }
                } else {
                    // player called - check and bet available
                    return when (handRank) {
                        1 -> allIn()
                        2 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(4) }
                        3 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(2) }
                        4 -> if (hasHandPair) { betBlinds(2) } else { betBlinds(1) }
                        in 5..7 -> if (hasHandPair) { betBlinds(1) } else { CHECK }
                        else -> CHECK
                    }
                }
            }

            // more than 600 chips

            if (isDealer) {
                // player didn't play yet - fold, call and bet available
                return when (handRank) {
                    1 -> betBlinds(12)
                    2 -> if (hasHandPair) { betBlinds(8) } else { betBlinds(6) }
                    3 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(3) }
                    4 -> if (hasHandPair) { betBlinds(3) } else { betBlinds(1) }
                    5 -> if (hasHandPair) { betBlinds(2) } else { CALL }
                    6 -> if (hasHandPair) { betBlinds(1) } else { CALL }
                    else -> if (hasHandPair) { CALL } else { FOLD }
                }
            } else {
                // player called - check and bet available
                return when (handRank) {
                    1 -> betBlinds(13)
                    2 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(4) }
                    3 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(2) }
                    4 -> if (hasHandPair) { betBlinds(3) } else { betBlinds(1) }
                    5 -> if (hasHandPair) { betBlinds(2) } else { betBlinds(1) }
                    in 5..7 -> if (hasHandPair) { betBlinds(1) } else { CHECK }
                    else -> CHECK
                }
            }
        }
        else if (bet[PLAYER] > BIG_BLIND) {
            // less than 160 chips
            if (botStack < 4) {
                if (isDealer) {
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(1) }
                        in 4..6 -> if (hasHandPair) { allIn() } else { CALL }
                        7-> if (hasHandPair) { CALL } else { FOLD }
                        else -> FOLD
                    }
                } else {
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(2) }
                        in 4..6 -> if (hasHandPair) { allIn() } else { CALL }
                        7-> if (hasHandPair) { CALL } else { FOLD }
                        else -> FOLD
                    }
                }
            }

            // between 160 and 320 chips
            if (botStack in 4..8) {
                if (isDealer) {
                    when (handRank) {
                        in 1..2 -> return allIn()
                        3 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(6) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 5 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        6 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 2 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        7 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        8 -> return when {
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        else -> return FOLD
                    }
                } else {
                    when (handRank) {
                        in 1..2 -> return allIn()
                        3 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 5 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        in 6..7 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        8 -> return when {
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        else -> return FOLD
                    }
                }
            }

            // between 320 and 600 chips
            if (botStack in 8..15) {
                if (isDealer) {
                    when (handRank) {
                        1 -> return allIn()
                        2 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(10) else allIn()
                            callValue <= 4 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(6) else CALL
                            callValue <= 8 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(4) else CALL
                            else -> CALL
                        }
                        3 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(8) else allIn()
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 4 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 8 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        6 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 2 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        7 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        8 -> return when {
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        else -> return FOLD
                    }
                } else {
                    when (handRank) {
                        1 -> return allIn()
                        2 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(8) else CALL
                            callValue <= 4 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 8 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            else -> CALL
                        }
                        3 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(6) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 4 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 8 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(4) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 5 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                            callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 3 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        in 6..7 -> return when {
                            hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                            callValue <= 2 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        8 -> return when {
                            callValue <= BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        else -> return FOLD
                    }
                }
            }

            // more than 600 chips
            if (isDealer) {
                when (handRank) {
                    1 -> return when {
                        callValue <= 2 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(8) else allIn()
                        callValue <= 4 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(12) else betBlinds(4)
                        else -> allIn()
                    }
                    2 -> return when {
                        hasHandPair -> if (pot < 10 * BIG_BLIND) betBlinds(10) else betBlinds(4)
                        callValue <= 4 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(4) else CALL
                        callValue <= 8 * BIG_BLIND -> if (pot < 8 * BIG_BLIND) betBlinds(1) else CALL
                        else -> CALL
                    }
                    3 -> return when {
                        hasHandPair -> if (pot < 10 * BIG_BLIND) betBlinds(8) else allIn()
                        callValue <= 2 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(2) else CALL
                        callValue <= 4 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 8 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    4 -> return when {
                        hasHandPair -> if (pot < 10 * BIG_BLIND) betBlinds(4) else CALL
                        callValue <= 2 * BIG_BLIND -> if (pot < 10 * BIG_BLIND) betBlinds(2) else CALL
                        callValue <= 6 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    5 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                        callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 4 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    6 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 2 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    7 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    8 -> return when {
                        callValue <= BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    else -> return FOLD
                }
            } else {
                when (handRank) {
                    1 -> return when {
                        callValue <= 2 * BIG_BLIND -> if (pot < 9 * BIG_BLIND) betBlinds(7) else allIn()
                        callValue <= 4 * BIG_BLIND -> if (pot < 9 * BIG_BLIND) betBlinds(10) else betBlinds(2)
                        else -> allIn()
                    }
                    2 -> return when {
                        hasHandPair -> if (pot < 9 * BIG_BLIND) betBlinds(8) else betBlinds(2)
                        callValue <= 4 * BIG_BLIND -> if (pot < 9 * BIG_BLIND) betBlinds(3) else CALL
                        callValue <= 8 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        else -> CALL
                    }
                    3 -> return when {
                        hasHandPair -> if (pot < 9 * BIG_BLIND) betBlinds(6) else allIn()
                        callValue <= 2 * BIG_BLIND -> if (pot < 9 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 4 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 8 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    4 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(3) else CALL
                        callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(2) else CALL
                        callValue <= 6 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    5 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 2 * BIG_BLIND -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 4 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    6 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= 2 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    7 -> return when {
                        hasHandPair -> if (pot < 6 * BIG_BLIND) betBlinds(1) else CALL
                        callValue <= BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    8 -> return when {
                        callValue <= BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    else -> return FOLD
                }
            }
        }
        else {
            return if (validActions[CHECK]) { CHECK } else { FOLD }
        }
    }

     fun botAction(pokerChips: IntArray, bet: IntArray, pot: Int, validActions: BooleanArray): Int {

        resetValues()
        initValues(pokerChips, bet, pot)

        return calculateAction(pokerChips, bet, validActions)
    }
}