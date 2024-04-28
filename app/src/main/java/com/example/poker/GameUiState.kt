package com.example.poker

data class GameUiState(

    val playerName: String = "Filipe",

    val computerName: String = "Bot",

    val playerMoney: Int = 200,

    val computerMoney: Int = 200,

    val playerText: String = "",

    val playerBetValue: Int = 0,

    val computerText: String = "",

    val computerBetValue: Int = 0,

    val totalPot: Int = 0,

    val currentPot: Int = 0,

    val displayFlop: Boolean = false,

    val displayTurn: Boolean = false,

    val displayRiver: Boolean = false,

    val displayComputerCards: Boolean = true,

    val displayFoldButton: Boolean = false,

    val displayCallButton: Boolean = false,

    val displayCheckButton: Boolean = false,

    val displayBetButton: Boolean = false,

    val turnDelayTime: Int = 1000,

    val riverDelayTime: Int = 1000,

    val showdown: Boolean = false,

    val winnerText: String = "",

    val gameSummary: List<List<String>> = ArrayList()
)
