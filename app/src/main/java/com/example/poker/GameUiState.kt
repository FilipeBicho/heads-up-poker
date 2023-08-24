package com.example.poker

data class GameUiState(

    var player1Cards: ArrayList<Card> = ArrayList(),
    var player2Cards: ArrayList<Card> = ArrayList(),
    var tableCards: MutableList<Card> = ArrayList(),
    val pot: Int = 0,
    var player1Bet: Int = 0
)
