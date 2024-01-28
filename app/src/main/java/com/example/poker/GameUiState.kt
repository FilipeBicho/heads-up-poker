package com.example.poker

data class GameUiState(

    val playerName: String = "Filipe",

    val computerName: String = "Computer",

    val playerMoney: Int = INITIAL_MONEY,

    val computerMoney: Int = INITIAL_MONEY,

    val playerText: String = "",

    val playerBetValue: Int = 0,

    val computerText: String = "",

    val computerBetValue: Int = 0,

    val totalPot: Int = 0,

    val currentPot: Int = 0,

    val displayFlop: Boolean = false,

    val displayTurn: Boolean = false,

    val displayRiver: Boolean = false,

    val displayComputerCards: Boolean = false,

    val displayFoldButton: Boolean = false,

    val displayCallButton: Boolean = false,

    val displayCheckButton: Boolean = false,

    val displayBetButton: Boolean = false,

    val turnDelayTime: Int = 1000,

    val riverDelayTime: Int = 1000,

    val showdown: Boolean = false,

    val winnerText: String = "",

    val gameSummary: MutableList<List<String>> = ArrayList()
)
