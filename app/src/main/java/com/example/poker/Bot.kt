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

    private fun allIn(): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = totalMoney
            BET
        }
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

    private fun allInPairMinRank(rank: Int): Int {
        return if (hasHandPair && pairRank > rank) {
            allIn()
        } else {
            if (isDealer) {
                FOLD
            } else {
                CHECK
            }
        }
    }

    private fun callPairMinRank(rank: Int): Int {
        return if (hasHandPair && pairRank > rank) {
            CALL
        } else {
            FOLD
        }
    }

    private fun preFlopAction(pokerChips: IntArray, bet: IntArray, validActions: BooleanArray): Int {

        // Dealer: player called - check and bet available
        // Blind: player didn't play yet - fold, call and bet available
        if (bet[PLAYER] == BIG_BLIND) {
            // less than 160 chips
            if (botStack < 4) {
                return if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    when (handRank) {
                        in 1..3 -> allIn()
                        4 -> allInPairOrBetBlinds(4)
                        5 -> allInPairOrBetBlinds(2)
                        else -> {
                            return allInPairMinRank(THREE)
                        }
                    }
                } else {
                    // player called - check and bet available
                    when (handRank) {
                        in 1..3 -> allIn()
                        4 -> allInPairOrBetBlinds(2)
                        5 -> allInPairOrBetBlinds(1)
                        else -> {
                            return allInPairMinRank(THREE)
                        }
                    }
                }
            }

            // between 160 and 320 chips
            if (botStack in 4..8) {
                return if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    when (handRank) {
                        in 1..3 -> allIn()
                        4 -> allInPairOrBetBlinds(4)
                        5 -> allInPairOrBetBlinds(2)
                        else -> {
                            return allInPairMinRank(FIVE)
                        }
                    }
                } else {
                    // player called - check and bet available
                    when (handRank) {
                        in 1..3 -> allIn()
                        4 -> betBlinds(2)
                        5 -> betBlinds(1)
                        else -> {
                            return allInPairMinRank(FIVE)
                        }
                    }
                }
            }

            // between 320 and 600 chips
            if (botStack in 8..15) {

                return if (isDealer) {
                    // player didn't play yet - fold, call and bet available
                    when (handRank) {
                        1 -> allIn()
                        2 -> betBlinds(8)
                        3 -> betBlinds(6)
                        4 -> betBlinds(4)
                        else -> {
                            return allInPairMinRank(SIX)
                        }
                    }
                } else {
                    // player called - check and bet available
                    when (handRank) {
                        1 -> allIn()
                        2 -> betBlinds(8)
                        3 -> betBlinds(6)
                        4 -> betBlinds(4)
                        else -> {
                            return allInPairMinRank(SIX)
                        }
                    }
                }
            }

            // more than 600 chips

            return if (isDealer) {
                // player didn't play yet - fold, call and bet available
                when (handRank) {
                    1 -> betBlinds(15)
                    2 -> betBlinds(8)
                    in 3..4 -> betBlinds(4)
                    else -> {
                        return allInPairMinRank(SIX)
                    }
                }
            } else {
                // player called - check and bet available
                when (handRank) {
                    1 -> betBlinds(15)
                    2 -> betBlinds(8)
                    in 3..4 -> betBlinds(4)
                    else -> {
                        return allInPairMinRank(SIX)
                    }
                }
            }
        }

        // player raised - fold, call, bet available
        if (bet[PLAYER] > BIG_BLIND) {


            // less than 160 chips
            if (botStack < 4) {
                return if (isDealer) {
                    when (handRank) {
                        in 1..4 -> allIn()
                        in 5..6 -> CALL
                        else -> callPairMinRank(FOUR)

                    }
                } else {
                    when (handRank) {
                        in 1..4 -> allIn()
                        in 5..6 -> CALL
                        else -> callPairMinRank(THREE)
                    }
                }
            }

            // between 160 and 320 chips
            if (botStack in 4..8) {
                return if (isDealer) {
                    when (handRank) {
                        in 1..3 -> allIn()
                        in 4..6 -> callUntilOrFold(2 * BIG_BLIND)
                        in 7..8 -> callUntilOrFold(BIG_BLIND)
                        else -> FOLD

                    }
                } else {
                    when (handRank) {
                        in 1..3 -> allIn()
                        in 4..6 -> callUntilOrFold(2 * BIG_BLIND)
                        in 7..8 -> callUntilOrFold(BIG_BLIND)
                        else -> FOLD
                    }
                }
            }

            // between 320 and 600 chips
            if (botStack in 8..15) {
                return if (isDealer) {
                    when (handRank) {
                        in 1..3 -> allInPairMinRank(SEVEN)
                        in 4..5 -> {
                            return when {
                                callValue < 3 * BIG_BLIND -> betBlinds(6)
                                callValue in 3 * BIG_BLIND.. 5 * BIG_BLIND -> betBlinds(8)
                                else -> CALL
                            }
                        }
                        6 -> callUntilOrFold(3 * BIG_BLIND)
                        7 -> callUntilOrFold(2 * BIG_BLIND)
                        8 -> callUntilOrFold(BIG_BLIND)
                        else -> FOLD

                    }
                } else {
                    // player called - check and bet available
                    when (handRank) {
                        in 1..2 -> allInPairMinRank(SEVEN)
                        in 3..4 -> {
                            return when {
                                callValue < 3 * BIG_BLIND -> betBlinds(5)
                                callValue in 3 * BIG_BLIND.. 5 * BIG_BLIND -> betBlinds(8)
                                else -> CALL
                            }
                        }
                        5 -> callUntilOrFold(3 * BIG_BLIND)
                        6 -> callUntilOrFold(2 * BIG_BLIND)
                        7 -> callUntilOrFold(BIG_BLIND)
                        else -> FOLD
                    }
                }
            }

            // more than 600 chips

            return if (isDealer) {
                when (handRank) {
                    in 1..2 -> allInPairMinRank(EIGHT)
                    in 3..5 -> {
                        return when {
                            callValue < 3 * BIG_BLIND -> betBlinds(8)
                            callValue in 3 * BIG_BLIND.. 5 * BIG_BLIND -> betBlinds(10)
                            else -> CALL
                        }
                    }
                    6 -> callUntilOrFold(3 * BIG_BLIND)
                    7 -> callUntilOrFold(2 * BIG_BLIND)
                    8 -> callUntilOrFold(BIG_BLIND)
                    else -> FOLD

                }
            } else {
                // player called - check and bet available
                when (handRank) {
                    in 1..2 -> allInPairMinRank(EIGHT)
                    in 3..4 -> {
                        return when {
                            callValue < 3 * BIG_BLIND -> betBlinds(5)
                            callValue in 3 * BIG_BLIND.. 5 * BIG_BLIND -> betBlinds(8)
                            else -> CALL
                        }
                    }
                    5 -> callUntilOrFold(3 * BIG_BLIND)
                    6 -> callUntilOrFold(2 * BIG_BLIND)
                    7 -> callUntilOrFold(BIG_BLIND)
                    else -> FOLD
                }
            }
        }

        return if (validActions[CHECK]) {
            CHECK
        } else {
            FOLD
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