package com.example.poker.bot

import com.example.poker.BIG_BLIND
import com.example.poker.cards.BOT
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.botValidActions
import com.example.poker.game.Data.pokerChips
import com.example.poker.game.Data.round
import kotlin.math.abs

const val FOLD = 0
const val CHECK = 1
const val CALL = 2
const val BET = 3
const val RAISE = 4
const val ALLIN = 5

open class Bot {

    private var action: Int = 0
    private var playerStack: Int = 0
    private var totalMoney: Int = 0

    var betValue: Int = 0
    protected var botStack: Int = 0
    protected var callValue: Int = 0
    protected var pot: Int = 0
    protected var isDealer: Boolean = false
    protected var hasHandPair = false

    /**
     * Init stack and call value
     */
    private fun initValues() {

        totalMoney = pokerChips[BOT]

        if (pokerChips[PLAYER] > 0) {
            playerStack = pokerChips[PLAYER]/ BIG_BLIND
        }

        if (totalMoney > 0) {
            botStack = pokerChips[BOT]/ BIG_BLIND
        }

        if (playerStack > 0 && botStack > 0) {
            callValue = abs(bet[BOT] - bet[PLAYER])
        }

        hasHandPair = botCards.first().rank == botCards.last().rank
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

    protected fun allIn(): Int {
        return if (totalMoney - callValue < 0) {
            CALL
        } else {
            betValue = totalMoney
            BET
        }
    }

    protected fun betBlinds(blinds: Int): Int {
        betValue = if (blinds * BIG_BLIND >= totalMoney) {
            totalMoney
        } else {
            blinds * BIG_BLIND
        }

        return BET
    }


    open fun calculateAction(): Int {
        resetValues()
        initValues()

//        when (round) {
//            PRE_FLOP -> return PreFlopBot().calculateAction()
//            else -> {
//                betValue = BIG_BLIND
//                action = BET
//            }
//        }

        action = if (botValidActions[ALLIN]) {
            ALLIN
        }
        else if (botValidActions[RAISE]) {
            if (pokerChips[BOT] + bet[BOT] > bet[PLAYER] * 2) {
                betValue = bet[PLAYER] * 2
                RAISE
            } else {
                ALLIN
            }
        }
        else if (botValidActions[BET]) {
            if (pokerChips[BOT] > BIG_BLIND) {
                betValue = BIG_BLIND
                BET
            } else {
                ALLIN
            }
        }
        else if (botValidActions[CALL]) {
            CALL
        } else {
            CHECK
        }


        return action
    }
}