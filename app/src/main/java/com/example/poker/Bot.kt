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
    private val pairRank = cards.first().rank

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

    private fun callUntilOrFold(value: Int): Int {
        return if (callValue <= value) {
            CALL
        } else {
            FOLD
        }
    }

    private fun callUntilOrRaiseBlinds(value: Int, blinds: Int): Int {
        return if (callValue <= value) {
            CALL
        } else {
            betBlinds(blinds)
        }
    }

    private fun allIn(): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = totalMoney
            BET
        }
    }

    private fun raiseByMultiply(value: Int): Int {
        betValue = if (value * callValue >= totalMoney) {
            totalMoney
        } else {
            value * callValue
        }

        return BET
    }

    private fun callOrBetValue(value: Int): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = value
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

    private fun allInPairOrBetBlinds(blinds: Int): Int {
       return if (hasHandPair) {
            allIn()
        } else {
            betBlinds(blinds)
        }
    }

    private fun allInPairFromOrFold(rank: Int): Int {
        return if (hasHandPair && pairRank > rank) {
            allIn()
        } else {
            FOLD
        }
    }

    private fun allInPairFromOrCheck(rank: Int): Int {
        return if (hasHandPair && pairRank > rank) {
            allIn()
        } else {
            CHECK
        }
    }

    private fun preFlopAction(pokerChips: IntArray, bet: IntArray, validActions: BooleanArray): Int {

        // Dealer: player called - check and bet available
        // Blind: player didn't play yet - fold, call and bet available

        when {
            bet[PLAYER] == BIG_BLIND -> {
                // less than 160 chips
                if (botStack < 4) {
                    return if (isDealer) {
                        // player didn't play yet - fold, call and bet available
                        when (handRank) {
                            in 1..3 -> allIn()
                            in 4..6 -> if (hasHandPair) { allIn() } else { CALL }
                            else -> allInPairFromOrFold(THREE)
                        }
                    } else {
                        // player called - check and bet available
                        when (handRank) {
                            in 1..3 -> allIn()
                            in 4..6 -> if (hasHandPair) { allIn() } else { CHECK }
                            else -> allInPairFromOrCheck(THREE)
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
                            4 -> if (hasHandPair) { allIn() } else { betBlinds(2) }
                            5 -> if (hasHandPair) { allIn() } else { CALL }
                            6 -> if (hasHandPair) { allIn() } else { FOLD }
                            else -> if (hasHandPair) { CALL } else { FOLD }
                        }
                    } else {
                        // player called - check and bet available
                        return when (handRank) {
                            in 1..2 -> allIn()
                            3 -> if (hasHandPair) { allIn() } else { betBlinds(3) }
                            4 -> if (hasHandPair) { allIn() } else { betBlinds(2) }
                            5 -> if (hasHandPair) { betBlinds(4) } else { CHECK }
                            6 -> if (hasHandPair) { betBlinds(2) } else { CHECK }
                            else -> if (hasHandPair) { betBlinds(1) } else { CHECK }
                        }
                    }
                }

                // between 320 and 600 chips
                if (botStack in 8..15) {
                    if (isDealer) {
                        // player didn't play yet - fold, call and bet available
                        return when (handRank) {
                            1 -> allIn()
                            2 -> if (hasHandPair) { betBlinds(10) } else { betBlinds(8) }
                            3 -> if (hasHandPair) { betBlinds(8) } else { betBlinds(6) }
                            4 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(4) }
                            in 5..6 -> if (hasHandPair) { betBlinds(4) } else { CALL }
                            else -> if (hasHandPair) { CALL } else { FOLD }
                        }
                    } else {
                        // player called - check and bet available
                        return when (handRank) {
                            1 -> allIn()
                            2 -> if (hasHandPair) { betBlinds(8) } else { betBlinds(6) }
                            3 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(4) }
                            4 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(2) }
                            in 5..6 -> if (hasHandPair) { betBlinds(1) } else { CHECK }
                            else -> CHECK
                        }
                    }
                }

                // more than 600 chips

                if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    return when (handRank) {
                        1 -> betBlinds(15)
                        2 -> if (hasHandPair) { betBlinds(10) } else { betBlinds(6) }
                        3 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(4) }
                        4 -> if (hasHandPair) { betBlinds(5) } else { betBlinds(3) }
                        5 -> if (hasHandPair) { betBlinds(4) } else { CALL }
                        6 -> if (hasHandPair) { betBlinds(2) } else { CALL }
                        else -> if (hasHandPair) { CALL } else { FOLD }
                    }
                } else {
                    // player called - check and bet available
                    return when (handRank) {
                        1 -> betBlinds(13)
                        2 -> if (hasHandPair) { betBlinds(8) } else { betBlinds(4) }
                        3 -> if (hasHandPair) { betBlinds(6) } else { betBlinds(2) }
                        4 -> if (hasHandPair) { betBlinds(4) } else { betBlinds(1) }
                        5 -> if (hasHandPair) { betBlinds(2) } else { CALL }
                        6 -> if (hasHandPair) { betBlinds(1) } else { CALL }
                        else -> CHECK
                    }
                }
            }
            bet[PLAYER] > BIG_BLIND -> {
                // less than 160 chips
                if (botStack < 4) {
                    if (isDealer) {
                        return when (handRank) {
                            in 1..4 -> allIn()
                            5 -> if (hasHandPair) { allIn() } else { CALL }
                            in 6..7-> if (hasHandPair) { CALL } else { FOLD }
                            else -> FOLD
                        }
                    } else {
                        return when (handRank) {
                            in 1..4 -> allIn()
                            5 -> if (hasHandPair) { allIn() } else { CALL }
                            6 -> if (hasHandPair) { betBlinds(2) } else { FOLD }
                            7 -> if (hasHandPair) { CALL } else { FOLD }
                            else -> FOLD
                        }
                    }
                }

                // between 160 and 320 chips
                if (botStack in 4..8) {
                    if (isDealer) {
                        when (handRank) {
                            in 1..3 -> return allIn()
                            4 -> return if (hasHandPair) {
                                callUntilOrRaiseBlinds(4 * BIG_BLIND, 4)
                            } else {
                                callUntilOrFold(2 * BIG_BLIND)
                            }
                            5 -> return if (hasHandPair) {
                                callUntilOrRaiseBlinds(3 * BIG_BLIND, 3)
                            } else {
                                callUntilOrFold(2 * BIG_BLIND)
                            }
                            6-> return  if (hasHandPair) {
                                callUntilOrRaiseBlinds(2 * BIG_BLIND, 2)
                            } else {
                                callUntilOrFold(BIG_BLIND)
                            }
                            7-> return  if (hasHandPair) {
                                callUntilOrFold(2 * BIG_BLIND)
                            } else {
                                callUntilOrFold(BIG_BLIND)
                            }
                            else -> return FOLD

                        }
                    } else {
                        when (handRank) {
                            in 1..3 -> return allIn()
                            4 -> return if (hasHandPair) {
                                callUntilOrRaiseBlinds(3 * BIG_BLIND, 3)
                            } else {
                                callUntilOrFold(2 * BIG_BLIND)
                            }
                            5 -> return if (hasHandPair) {
                                callUntilOrRaiseBlinds(2 * BIG_BLIND, 2)
                            } else {
                                callUntilOrFold(2 * BIG_BLIND)
                            }
                            6-> return  if (hasHandPair) {
                                callUntilOrRaiseBlinds(2 * BIG_BLIND, 2)
                            } else {
                                callUntilOrFold(BIG_BLIND)
                            }
                            7-> return  if (hasHandPair) {
                                callUntilOrFold(2 * BIG_BLIND)
                            } else {
                                FOLD
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
                            2 -> {
                                return when {
                                    callValue <= 5 * BIG_BLIND -> betBlinds(10)
                                    callValue < 8 * BIG_BLIND -> betBlinds(12)
                                    else -> if (hasHandPair) { allIn() } else { CALL }
                                }
                            }
                            3 -> {
                                return when {
                                    callValue <= 3 * BIG_BLIND -> betBlinds(6)
                                    callValue in 3 * BIG_BLIND.. 6 * BIG_BLIND -> betBlinds(8)
                                    callValue in 6 * BIG_BLIND.. 10 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            4 -> {
                                return when {
                                    callValue < 2 * BIG_BLIND -> betBlinds(2)
                                    callValue < 10 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            5 -> {
                                return when {
                                    callValue < 2 * BIG_BLIND -> betBlinds(2)
                                    callValue < 4 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            6 -> {
                                return when {
                                    callValue < 4 * BIG_BLIND -> if (hasHandPair) { CALL } else { FOLD }
                                    else -> FOLD
                                }
                            }
                            7 -> {
                                return when {
                                    callValue < 2 * BIG_BLIND -> if (hasHandPair) { CALL } else { FOLD }
                                    else -> FOLD
                                }
                            }
                            else -> return FOLD

                        }
                    } else {
                        when (handRank) {
                            1 -> return allIn()
                            2 -> {
                                return when {
                                    callValue <= 4 * BIG_BLIND -> betBlinds(8)
                                    callValue < 8 * BIG_BLIND -> betBlinds(10)
                                    else -> if (hasHandPair) { allIn() } else { CALL }
                                }
                            }
                            3 -> {
                                return when {
                                    callValue <= 2 * BIG_BLIND -> betBlinds(4)
                                    callValue in 2 * BIG_BLIND.. 6 * BIG_BLIND -> betBlinds(6)
                                    callValue in 6 * BIG_BLIND.. 8 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            4 -> {
                                return when {
                                    callValue <= BIG_BLIND -> betBlinds(2)
                                    callValue < 6 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            5 -> {
                                return when {
                                    callValue <= BIG_BLIND -> betBlinds(1)
                                    callValue < 4 * BIG_BLIND -> CALL
                                    else -> FOLD
                                }
                            }
                            6 -> {
                                return when {
                                    callValue <= 3 * BIG_BLIND -> if (hasHandPair) { CALL } else { FOLD }
                                    else -> FOLD
                                }
                            }
                            7 -> {
                                return when {
                                    callValue <= BIG_BLIND -> if (hasHandPair) { CALL } else { FOLD }
                                    else -> FOLD
                                }
                            }
                            else -> return FOLD

                        }
                    }
                }

                // more than 600 chips

                if (isDealer) {
                    when (handRank) {
                        1 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(6)
                                callValue <= 4 * BIG_BLIND -> betBlinds(10)
                                callValue <= 10 * BIG_BLIND -> betBlinds(20)
                                else -> allIn()
                            }
                        }
                        2 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(4)
                                callValue <= 4 * BIG_BLIND -> betBlinds(6)
                                callValue <= 10 * BIG_BLIND -> betBlinds(10)
                                else -> CALL
                            }
                        }
                        4 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(2)
                                callValue <= 4 * BIG_BLIND -> betBlinds(4)
                                callValue <= 10 * BIG_BLIND -> CALL
                                hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        5 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(1)
                                callValue <= 6 * BIG_BLIND -> CALL
                                hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        6 -> {
                            return when {
                                callValue <= 4 * BIG_BLIND -> CALL
                                callValue <= 6 * BIG_BLIND && hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        7 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> CALL
                                callValue <= 4 * BIG_BLIND && hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        else -> return FOLD

                    }
                } else {
                    when (handRank) {
                        1 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(4)
                                callValue <= 4 * BIG_BLIND -> betBlinds(8)
                                callValue <= 10 * BIG_BLIND -> betBlinds(15)
                                else -> allIn()
                            }
                        }
                        2 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(2)
                                callValue <= 4 * BIG_BLIND -> betBlinds(4)
                                callValue <= 8 * BIG_BLIND -> betBlinds(8)
                                else -> CALL
                            }
                        }
                        4 -> {
                            return when {
                                callValue <= 2 * BIG_BLIND -> betBlinds(1)
                                callValue <= 4 * BIG_BLIND -> betBlinds(2)
                                callValue <= 8 * BIG_BLIND && hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        5 -> {
                            return when {
                                callValue <= 4 * BIG_BLIND -> CALL
                                callValue <= 6 * BIG_BLIND && hasHandPair-> CALL
                                else -> FOLD
                            }
                        }
                        6 -> {
                            return when {
                                callValue <= 4 * BIG_BLIND && hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        7 -> {
                            return when {
                                callValue <=  BIG_BLIND -> CALL
                                callValue <=  2 * BIG_BLIND && hasHandPair -> CALL
                                else -> FOLD
                            }
                        }
                        else -> return FOLD
                    }
                }
            }
            else -> return if (validActions[CHECK]) { CHECK } else { FOLD }
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