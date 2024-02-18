package com.example.poker

import com.example.poker.helper.HandGroup

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3

class Bot (odds: Odds, cards: List<Card>, tableCards: List<Card>, isDealer: Boolean) {

    protected var cardsStrength: Int = 0
    protected var action: Int = 0
    protected var bet: Int = 0
    protected var playerStack: Int = 0
    protected var botStack: Int = 0
    protected var callValue: Int = 0
    protected var isDealer: Boolean = false

    init {
        cardsStrength = HandGroup(cards).group
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

    private fun preFlopAction(): Int {

        val botStackWithCall = botStack + callValue

        // player didn't raised
        if (callValue <= BIG_BLIND) {

            // if any stack is below 160 chips
            if (playerStack < 4 || botStackWithCall < 4) {

            }

            // if any stack is between 160 and 320 chips
            if ((playerStack in 4..8 || botStackWithCall in 4..8)) {

            }

            // if any stack is between 320 and 480 chips
            if ((playerStack in 8..12 || botStackWithCall in 8..12)) {

            }

            // if any stack if bigger than 480 chips
            if (playerStack > 12 || botStackWithCall > 12) {

            }
        } else {

            // if any stack is below 160 chips
            if (playerStack < 4 || botStackWithCall < 4) {
                //
            }

            // if any stack is between 160 and 320 chips
            if ((playerStack in 4..8 || botStackWithCall in 4..8)) {

            }

            // if any stack is between 320 and 480 chips
            if ((playerStack in 8..12 || botStackWithCall in 8..12)) {

            }

            // if any stack if bigger than 480 chips
            if (playerStack > 12 || botStackWithCall > 12) {

            }
        }

        return action
    }


    fun botAction(pokerChips: IntArray, bet: IntArray, totalPot: Int, round: Int): Int {

        resetValues()
        initValues(pokerChips, bet)

        when (round) {
            PRE_FLOP -> return preFlopAction()
        }

        return action
    }
}