package com.example.poker.bot

import com.example.poker.BIG_BLIND
import com.example.poker.cards.BOT
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import com.example.poker.game.Data.bet
import com.example.poker.game.Data.botCards
import com.example.poker.game.Data.botValidActions
import com.example.poker.game.Data.dealer
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

    var betValue: Int = 0
    protected var botStack: Int = 0
    protected var callValue: Int = 0
    protected var pot: Int = 0
    protected var isDealer: Boolean = false
    protected var hasHandPair = false

    protected open fun initValues() {
        isDealer = BOT == dealer
        playerStack = if (pokerChips[PLAYER] > 0) pokerChips[PLAYER]/BIG_BLIND else 0
        botStack = if (pokerChips[BOT] > 0) pokerChips[BOT]/BIG_BLIND else 0
        callValue = if (playerStack > 0 && botStack > 0) abs(bet[BOT] - bet[PLAYER]) else 0
        hasHandPair = botCards.first().rank == botCards.last().rank
    }

    protected fun allIn(): Int {
        return if (pokerChips[PLAYER] == 0) CALL else ALLIN
    }

    protected fun betBlinds(blinds: Int): Int {

        if (pokerChips[PLAYER] == 0) {
            return CALL
        }

        betValue = if (blinds * BIG_BLIND >= pokerChips[BOT]) {
            pokerChips[BOT]
        } else {
            bet[PLAYER] + blinds * BIG_BLIND
        }

        return if (bet[PLAYER] > 0) RAISE else BET
    }

    open fun calculateAction(): Int {

        val bot = if (round == PRE_FLOP) PreFlopBot() else InGameBot()

        when (round) {
            PRE_FLOP -> {
                action = bot.calculateAction()
                if (action == BET || action == RAISE) {
                    betValue = bot.betValue
                }
            }
            else -> {
                action = if (botValidActions[BET]) {
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
            }
        }

        return action
    }
}