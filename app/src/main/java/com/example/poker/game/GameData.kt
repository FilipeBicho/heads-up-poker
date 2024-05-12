package com.example.poker.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.poker.cards.Card
import com.example.poker.cards.PRE_FLOP
import com.example.poker.odds.Odds

object GameData {
    var player: Int = -1
    var opponent: Int = -1
    var dealer: Int = -1
    var blind: Int = -1
    var totalPotValue: Int = 0
    var round: Int = PRE_FLOP

    var pokerChips: MutableList<Int> = mutableListOf(0,0,0)
    var bet: MutableList<Int> = mutableListOf(0,0,0)
    var checkAvailable: Boolean = true

    var gameSummaryMap: MutableList<List<String>> = ArrayList()
    var gameSummaryList: MutableList<String> = ArrayList()

    var gameNumber: Int = -1
    var playerName: List<String> = listOf("Player", "Computer")

    var playerCards: SnapshotStateList<Card> = mutableStateListOf()
    var computerCards: SnapshotStateList<Card> = mutableStateListOf()
    var tableCards: SnapshotStateList<Card> = mutableStateListOf()

    var odds: Odds = Odds(mutableListOf())

    var Bet: Bet = Bet()
}