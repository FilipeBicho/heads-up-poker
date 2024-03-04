package com.example.poker

import com.example.poker.helper.HandGroup
import kotlin.math.abs

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3

open class Bot (odds: Odds, private val cards: List<Card>, tableCards: List<Card>, isDealer: Boolean) {

    private var handRank: Int = 0
    private var action: Int = 0
    var betValue: Int = 0
    private var playerStack: Int = 0
    private var botStack: Int = 0
    private var callValue: Int = 0
    private var totalMoney: Int = 0
    private var isDealer: Boolean = false
    private val hasHandPair = cards.first().rank == cards.last().rank

    init {
        handRank = HandGroup(cards).group
        this.isDealer = isDealer
    }

    /**
     * Init stack and call value
     */
    private fun initValues(pokerChips: IntArray, bet:IntArray) {

        totalMoney = pokerChips[BOT]

        if (pokerChips[PLAYER] > 0) {
            playerStack = pokerChips[PLAYER]/BIG_BLIND
        }

        if (totalMoney > 0) {
            botStack = pokerChips[BOT]/ BIG_BLIND
        }

        if (playerStack > 0 && botStack > 0) {
            callValue = abs(bet[BOT] - bet[PLAYER])
        }
    }

    /**
     * Reset stack and call value
     */
    private fun resetValues() {
        playerStack = 0
        botStack = 0
        callValue = 0
        totalMoney = 0
    }

    private fun allIn(): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = totalMoney
            BET
        }
    }

    private fun betBlinds(blinds: Int): Int {
        betValue = if (blinds * BIG_BLIND >= totalMoney) {
            totalMoney
        } else {
            blinds * BIG_BLIND
        }

        return BET
    }


    private fun preFlopAction(pokerChips: IntArray, bet: IntArray, validActions: BooleanArray): Int {

        // Dealer: player called - check and bet available
        // Blind: player didn't play yet - fold, call and bet available


        if (bet[PLAYER] == BIG_BLIND) {
            // less than 160 chips
            if (botStack < 4) {
                if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> betBlinds(2)
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
                        5 -> if (hasHandPair) { allIn() } else { CALL }
                        in 5..6 -> if (hasHandPair) { allIn() } else { FOLD }
                        in 6..7-> if (hasHandPair) { CALL } else { FOLD }
                        else -> FOLD
                    }
                } else {
                    return when (handRank) {
                        in 1..2 -> allIn()
                        3 -> if (hasHandPair) { allIn() } else { betBlinds(1) }
                        5 -> if (hasHandPair) { allIn() } else { CALL }
                        in 5..6 -> if (hasHandPair) { allIn() } else { FOLD }
                        in 6..7-> if (hasHandPair) { CALL } else { FOLD }
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
                            hasHandPair -> betBlinds(6)
                            callValue <= 2 * BIG_BLIND -> betBlinds(4)
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> betBlinds(4)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> betBlinds(2)
                            callValue <= 2 * BIG_BLIND -> betBlinds(1)
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        6 -> return when {
                            hasHandPair -> betBlinds(1)
                            callValue <= 2 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        7 -> return when {
                            hasHandPair -> betBlinds(1)
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
                            hasHandPair -> betBlinds(4)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 5 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> betBlinds(2)
                            callValue <= 2 * BIG_BLIND -> betBlinds(1)
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> betBlinds(1)
                            callValue <= 2 * BIG_BLIND -> betBlinds(1)
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        in 6..7 -> return when {
                            hasHandPair -> betBlinds(1)
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
                            hasHandPair -> betBlinds(10)
                            callValue <= 4 * BIG_BLIND -> betBlinds(6)
                            callValue <= 8 * BIG_BLIND -> betBlinds(1)
                            else -> CALL
                        }
                        3 -> return when {
                            hasHandPair -> betBlinds(8)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 4 * BIG_BLIND -> betBlinds(1)
                            callValue <= 8 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> betBlinds(4)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 6 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> betBlinds(2)
                            callValue <= 2 * BIG_BLIND -> betBlinds(1)
                            callValue <= 4 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        6 -> return when {
                            hasHandPair -> betBlinds(1)
                            callValue <= 2 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        7 -> return when {
                            hasHandPair -> betBlinds(1)
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
                            hasHandPair -> betBlinds(8)
                            callValue <= 4 * BIG_BLIND -> betBlinds(4)
                            callValue <= 8 * BIG_BLIND -> betBlinds(1)
                            else -> CALL
                        }
                        3 -> return when {
                            hasHandPair -> betBlinds(6)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 4 * BIG_BLIND -> betBlinds(1)
                            callValue <= 8 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        4 -> return when {
                            hasHandPair -> betBlinds(4)
                            callValue <= 2 * BIG_BLIND -> betBlinds(2)
                            callValue <= 5 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        5 -> return when {
                            hasHandPair -> betBlinds(2)
                            callValue <= 2 * BIG_BLIND -> betBlinds(1)
                            callValue <= 3 * BIG_BLIND -> CALL
                            else -> FOLD
                        }
                        in 6..7 -> return when {
                            hasHandPair -> betBlinds(1)
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
                        callValue <= 2 * BIG_BLIND -> betBlinds(8)
                        callValue <= 4 * BIG_BLIND -> betBlinds(12)
                        callValue <= 8 * BIG_BLIND -> betBlinds(15)
                        else -> allIn()
                    }
                    2 -> return when {
                        hasHandPair -> betBlinds(10)
                        callValue <= 4 * BIG_BLIND -> betBlinds(6)
                        callValue <= 8 * BIG_BLIND -> betBlinds(1)
                        else -> CALL
                    }
                    3 -> return when {
                        hasHandPair -> betBlinds(8)
                        callValue <= 2 * BIG_BLIND -> betBlinds(2)
                        callValue <= 4 * BIG_BLIND -> betBlinds(1)
                        callValue <= 8 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    4 -> return when {
                        hasHandPair -> betBlinds(4)
                        callValue <= 2 * BIG_BLIND -> betBlinds(2)
                        callValue <= 6 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    5 -> return when {
                        hasHandPair -> betBlinds(2)
                        callValue <= 2 * BIG_BLIND -> betBlinds(1)
                        callValue <= 4 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    6 -> return when {
                        hasHandPair -> betBlinds(1)
                        callValue <= 2 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    7 -> return when {
                        hasHandPair -> betBlinds(1)
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
                        callValue <= 2 * BIG_BLIND -> betBlinds(8)
                        callValue <= 4 * BIG_BLIND -> betBlinds(10)
                        callValue <= 8 * BIG_BLIND -> betBlinds(13)
                        else -> allIn()
                    }
                    2 -> return when {
                        hasHandPair -> betBlinds(8)
                        callValue <= 4 * BIG_BLIND -> betBlinds(4)
                        callValue <= 8 * BIG_BLIND -> betBlinds(1)
                        else -> CALL
                    }
                    3 -> return when {
                        hasHandPair -> betBlinds(6)
                        callValue <= 2 * BIG_BLIND -> betBlinds(1)
                        callValue <= 6 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    4 -> return when {
                        hasHandPair -> betBlinds(2)
                        callValue <= 2 * BIG_BLIND -> betBlinds(1)
                        callValue <= 5 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    5 -> return when {
                        hasHandPair -> betBlinds(1)
                        callValue <= 4 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    6 -> return when {
                        hasHandPair -> betBlinds(1)
                        callValue <= 2 * BIG_BLIND -> CALL
                        else -> FOLD
                    }
                    7 -> return when {
                        hasHandPair -> betBlinds(1)
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


    fun botAction(pokerChips: IntArray, bet: IntArray, totalPot: Int, round: Int, validActions: BooleanArray): Int {

        resetValues()
        initValues(pokerChips, bet)

        when (round) {
            PRE_FLOP -> return preFlopAction(pokerChips, bet, validActions)
        }

        return action
    }
}