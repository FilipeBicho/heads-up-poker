package com.example.poker

import com.example.poker.helper.HandGroup

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3

open class Bot (odds: Odds, private val cards: List<Card>, tableCards: List<Card>, isDealer: Boolean) {

    private var cardsRank: Int = 0
    private var action: Int = 0
    private var betValue: Int = 0
    private var playerStack: Int = 0
    private var botStack: Int = 0
    private var callValue: Int = 0
    private var isDealer: Boolean = false

    init {
        cardsRank = HandGroup(cards).group
        this.isDealer = isDealer
    }

    /**
     * Init stack and call value
     */
    private fun initValues(pokerChips: IntArray, bet:IntArray) {

        if (pokerChips[PLAYER] > 0) {
            playerStack = pokerChips[PLAYER]/BIG_BLIND
        }

        if (pokerChips[COMPUTER] > 0) {
            botStack = pokerChips[COMPUTER]/ BIG_BLIND
        }

        if (playerStack > 0 && botStack > 0) {
            callValue = bet[COMPUTER] - bet[PLAYER]
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

        val preFlopOdds: Int = PreFlopOdds(cards).getOdds()
        val hasHandPair = cards.first().rank == cards.last().rank

        // player didn't raised
        if (bet[PLAYER] <= BIG_BLIND) {

            // if any stack is below 160 chips
            if (playerStack < 4 || botStack < 4) {

                if (cardsRank in 1..3 || (hasHandPair && cards.first().rank >= FIVE)) {
                    action = BET
                    betValue = pokerChips[COMPUTER]
                }

            }

            // if any stack is between 160 and 320 chips
            if ((playerStack in 4..8 || botStack in 4..8)) {

            }

            // if any stack is between 320 and 480 chips
            if ((playerStack in 8..12 || botStack in 8..12)) {

            }

            // if any stack if bigger than 480 chips
            if (playerStack > 12 || botStack > 12) {

            }
        } else {

            // if any stack is below 160 chips
            if (playerStack < 4 || botStack < 4) {
                //
            }

            // if any stack is between 160 and 320 chips
            if ((playerStack in 4..8 || botStack in 4..8)) {

            }

            // if any stack is between 320 and 480 chips
            if ((playerStack in 8..12 || botStack in 8..12)) {

            }

            // if any stack if bigger than 480 chips
            if (playerStack > 12 || botStack > 12) {

            }
        }

        return action
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