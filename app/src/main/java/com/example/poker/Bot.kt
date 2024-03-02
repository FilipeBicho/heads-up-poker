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
    public var betValue: Int = 0
    private var playerStack: Int = 0
    private var botStack: Int = 0
    private var callValue: Int = 0
    private var isDealer: Boolean = false

    init {
        handRank = HandGroup(cards).group
        this.isDealer = isDealer
    }

    /**
     * Init stack and call value
     */
    private fun initValues(pokerChips: IntArray, bet:IntArray) {

        if (pokerChips[PLAYER] > 0) {
            playerStack = pokerChips[PLAYER]/BIG_BLIND
        }

        if (pokerChips[BOT] > 0) {
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
    }

    private fun preFlopAction(pokerChips: IntArray, bet: IntArray, validActions: BooleanArray): Int {
        val hasHandPair = cards.first().rank == cards.last().rank

        if (!isDealer) {
            // player called - check and bet available
            if (bet[PLAYER] == BIG_BLIND) {
                // less than 160 chips
                if (botStack < 4) {
                    if (handRank in 1..4 || (hasHandPair && cards.first().rank >= FIVE)) {
                        betValue = pokerChips[BOT]
                        return BET
                    }

                    if (handRank == 5) {
                        betValue = if (botStack > 2) {
                            2* BIG_BLIND
                        } else {
                            pokerChips[BOT]
                        }

                        return BET
                    }

                    return CHECK
                }

                // between 160 and 320 chips
                if (botStack in 4..8) {
                    if (handRank in 1..3 || (hasHandPair && cards.first().rank >= SIX)) {
                        betValue = pokerChips[BOT]
                        return BET
                    }

                    if (handRank == 4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    if (handRank == 5) {
                        betValue = 2*BIG_BLIND
                        return BET
                    }

                    return CHECK
                }

                // between 320 and 600 chips
                if (botStack in 8..15) {
                    if (handRank == 1) {
                        betValue = pokerChips[BOT]
                    }

                    if (handRank == 2) {
                        betValue = 8*BIG_BLIND
                        return BET
                    }

                    if (handRank == 3 || (hasHandPair && cards.first().rank >= SEVEN)) {
                        betValue = 6*BIG_BLIND
                    }

                    if (handRank == 4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    return CHECK
                }

                // more than 600 chips
                if (botStack > 16) {
                    if (handRank == 1) {
                        betValue = if (playerStack < 15) {
                            pokerChips[PLAYER]
                        } else {
                            15* BIG_BLIND
                        }
                        return BET
                    }

                    if (handRank == 2) {
                        betValue = 8*BIG_BLIND
                        return BET
                    }

                    if (handRank in 3..4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    if (hasHandPair && cards.first().rank >= FOUR) {
                        betValue = 2*BIG_BLIND
                        return BET
                    }

                    return CHECK
                }
            }

            // player raised - fold, call, bet available
            if (bet[PLAYER] > BIG_BLIND) {
                // less than 160 chips
                if (botStack < 4) {
                    if (handRank in 1..4 || (hasHandPair && cards.first().rank >= FIVE)) {

                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 5..6) {
                        return CALL
                    }
                    return FOLD
                }

                // between 160 and 320 chips
                if (botStack in 4..8) {
                    if (handRank in 1..3 || (hasHandPair && cards.first().rank >= SIX)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 4..5) {
                        return if (callValue < 2* BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank in 6..7) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }
                    return FOLD
                }

                // between 320 and 600 chips
                if (botStack in 8..15) {

                    if (handRank in 1..2 || (hasHandPair && cards.first().rank >= SEVEN)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 3..4) {

                        if (callValue < 3*BIG_BLIND) {
                            betValue = 5* BIG_BLIND
                            return BET
                        }

                        if (callValue in 3*BIG_BLIND..5* BIG_BLIND) {
                            betValue = 8* BIG_BLIND
                            return BET
                        }

                        return CALL
                    }

                    if (handRank == 5) {
                        return if (callValue < 3*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 6) {
                        return if (callValue < 2*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 7) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    return FOLD
                }

                // more than 600 chips
                if (botStack > 16) {
                    if (handRank in 1..2 || (hasHandPair && cards.first().rank >= EIGHT)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = 3*bet[PLAYER]
                            BET
                        }
                    }

                    if (handRank in 3..4) {

                        if (callValue < 3*BIG_BLIND) {
                            betValue = 5* BIG_BLIND
                            return BET
                        }

                        if (callValue in 3*BIG_BLIND..5* BIG_BLIND) {
                            betValue = 8* BIG_BLIND
                            return BET
                        }

                        return CALL
                    }

                    if (handRank == 5) {
                        return if (callValue < 3*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 6) {
                        return if (callValue < 2*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 7) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    return FOLD
                }
            }
        } else {

            // player didn't play yet - fold, call and bet available
            if (bet[PLAYER] == BIG_BLIND) {
                // less than 160 chips
                if (botStack < 4) {

                    if (handRank in 1..4 || (hasHandPair && cards.first().rank >= FIVE)) {
                        betValue = pokerChips[BOT]
                        return BET
                    }

                    if (handRank in 5..6) {
                        return CALL
                    }

                    return FOLD
                }

                // between 160 and 320 chips
                if (botStack in 4..8) {

                    if (handRank in 1..3 || (hasHandPair && cards.first().rank >= SIX)) {
                        betValue = pokerChips[BOT]
                        return BET
                    }

                    if (handRank == 4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    if (handRank == 5) {
                        betValue = 2*BIG_BLIND
                        return BET
                    }

                    return CALL
                }

                // between 320 and 600 chips
                if (botStack in 8..15) {

                    if (handRank == 1) {
                        betValue = pokerChips[BOT]
                    }

                    if (handRank == 2) {
                        betValue = 8*BIG_BLIND
                        return BET
                    }

                    if (handRank == 3 || (hasHandPair && cards.first().rank >= SEVEN)) {
                        betValue = 6*BIG_BLIND
                    }

                    if (handRank == 4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    if (handRank > 5) {
                        return CALL
                    }
                }

                // more than 600 chips
                if (botStack > 15) {

                    if (handRank == 1) {
                        betValue = if (playerStack < 15) {
                            pokerChips[PLAYER]
                        } else {
                            15* BIG_BLIND
                        }
                        return BET
                    }

                    if (handRank == 2) {
                        betValue = 8*BIG_BLIND
                        return BET
                    }

                    if (handRank in 3..4) {
                        betValue = 4*BIG_BLIND
                        return BET
                    }

                    if (hasHandPair && cards.first().rank >= FOUR) {
                        betValue = 2*BIG_BLIND
                        return BET
                    }

                    return CALL
                }
            }

            // player raised - fold, call and bet available
            if (bet[PLAYER] > BIG_BLIND) {
                // less than 160 chips
                if (botStack < 4) {

                    if (handRank in 1..4 || (hasHandPair && cards.first().rank >= FIVE)) {

                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 5..6) {
                        return CALL
                    }

                    if (handRank > 6) {
                        return FOLD
                    }
                }

                // between 160 and 320 chips
                if (botStack in 4..8) {

                    if (handRank in 1..3 || (hasHandPair && cards.first().rank >= SIX)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 4..6) {
                        return if (callValue < 2* BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank in 7..8) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    return FOLD
                }

                // between 320 and 600 chips
                if (botStack in 8..15) {

                    if (handRank in 1..3 || (hasHandPair && cards.first().rank >= SEVEN)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = pokerChips[BOT]
                            BET
                        }
                    }

                    if (handRank in 4..5) {

                        if (callValue < 3*BIG_BLIND) {
                            betValue = 6* BIG_BLIND
                            return BET
                        }

                        if (callValue in 3*BIG_BLIND..5* BIG_BLIND) {
                            betValue = 8* BIG_BLIND
                            return BET
                        }

                        return CALL
                    }

                    if (handRank == 6) {
                        return if (callValue < 3*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 7) {
                        return if (callValue < 2*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 8) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    return FOLD
                }

                // more than 600 chips
                if (botStack > 15) {

                    if (handRank in 1..2 || (hasHandPair && cards.first().rank >= EIGHT)) {
                        return if (pokerChips[BOT] - callValue < 0) {
                            CALL
                        } else {
                            betValue = 3*bet[PLAYER]
                            BET
                        }
                    }

                    if (handRank in 3..5) {

                        if (callValue < 3*BIG_BLIND) {
                            betValue = 8* BIG_BLIND
                            return BET
                        }

                        if (callValue in 3*BIG_BLIND..5* BIG_BLIND) {
                            betValue = 10* BIG_BLIND
                            return BET
                        }

                        return CALL
                    }

                    if (handRank == 6) {
                        return if (callValue < 3*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 7) {
                        return if (callValue < 2*BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    if (handRank == 8) {
                        return if (callValue < BIG_BLIND) {
                            CALL
                        } else {
                            FOLD
                        }
                    }

                    return FOLD
                }
            }
        }
        return if (validActions[FOLD]) {
            FOLD
        } else {
            CHECK
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