package com.filipebicho.pokerclash.game

import com.filipebicho.pokerclash.data.Data.actionHistory

class Stats {

    var handsPlayed: Int = 0
    var voluntarilyPutMoneyInPot: Int = 0
    var preFlopRaises: Int = 0
    var continuationBet: Int = 0
    var continuationBetFaced: Int = 0
    var foldsToContinuationBet: Int = 0
    var riverBets: Int = 0
    var riverBluffsDetected: Int = 0

    fun updateStatsAfterHand() {
        try {
            handsPlayed += 1
            if (opponentVoluntarilyPutInPreFlop()) voluntarilyPutMoneyInPot += 1
            if (opponentRaisedPreFlop()) preFlopRaises += 1
            if (opponentMadeContinuationBet()) continuationBet += 1
            if (opponentFacedContinuationBet()) continuationBetFaced += 1
            if (opponentFoldedToContinuationBet()) foldsToContinuationBet += 1
            if (opponentBetRiver()) riverBets += 1
            if (opponentBluffedRiverAndGotCalled()) riverBluffsDetected += 1
        } catch (_: Exception) {}
    }

    fun safePercentage(numerator: Int, denominator: Int): Int {
        return if (denominator > 0) ((numerator.toDouble() / denominator) * 100).toInt() else 0
    }

    fun opponentVoluntarilyPutInPreFlop(): Boolean {
        var opponentPaidBlind = false
        for (action in actionHistory) {
            val a = action.lowercase()
            if (a.contains("pre-flop") && a.contains("opponent")) {
                when {
                    a.contains("pay small blind") || a.contains("pay big blind") -> {
                        opponentPaidBlind = true
                    }
                    a.contains("call") || a.contains("bet") || a.contains("raise") -> {
                        if (!opponentPaidBlind || a.contains("raise") || a.contains("bet")) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun opponentRaisedPreFlop(): Boolean {
        return actionHistory.any {
            it.contains("pre-flop", ignoreCase = true) &&
                    it.contains("opponent", ignoreCase = true) &&
                    (it.contains("raise", ignoreCase = true) || it.contains("bet", ignoreCase = true))
        }
    }

    fun opponentMadeContinuationBet(): Boolean {
        if (!opponentRaisedPreFlop()) return false

        val flopBets = actionHistory.filter {
            it.contains("flop", ignoreCase = true) && it.contains("bet", ignoreCase = true)
        }

        val firstFlopBet = flopBets.firstOrNull()
        return firstFlopBet?.contains("opponent", ignoreCase = true) == true
    }

    fun opponentFacedContinuationBet(): Boolean {
        val youWereAggressor = actionHistory.any {
            it.contains("pre-flop", ignoreCase = true) &&
                    it.contains("you", ignoreCase = true) &&
                    (it.contains("raise", ignoreCase = true) || it.contains("bet", ignoreCase = true))
        }

        if (!youWereAggressor) return false

        val yourFlopBet = actionHistory.any {
            it.contains("flop", ignoreCase = true) &&
                    it.contains("you", ignoreCase = true) &&
                    it.contains("bet", ignoreCase = true)
        }

        return yourFlopBet
    }

    fun opponentFoldedToContinuationBet(): Boolean {
        if (!opponentFacedContinuationBet()) return false

        val opponentFold = actionHistory.any {
            it.contains("flop", ignoreCase = true) &&
                    it.contains("opponent", ignoreCase = true) &&
                    it.contains("fold", ignoreCase = true)
        }

        return opponentFold
    }

    fun opponentBetRiver(): Boolean {
        return actionHistory.any {
            it.contains("river", ignoreCase = true) &&
            it.contains("opponent", ignoreCase = true) &&
            it.contains("bet", ignoreCase = true)
        }
    }

    fun opponentBluffedRiverAndGotCalled(): Boolean {
        val opponentBetRiver = actionHistory.any {
            it.contains("river", ignoreCase = true) &&
            it.contains("opponent", ignoreCase = true) &&
            it.contains("bet", ignoreCase = true)
        }

        val youCalledRiver = actionHistory.any {
            it.contains("river", ignoreCase = true) &&
            it.contains("you", ignoreCase = true) &&
            it.contains("call", ignoreCase = true)
        }

        val opponentShowedAndLost = actionHistory.any {
            it.contains("showdown", ignoreCase = true) &&
            it.contains("opponent", ignoreCase = true) &&
            it.contains("lost", ignoreCase = true)
        }

        return opponentBetRiver && youCalledRiver && opponentShowedAndLost
    }
}