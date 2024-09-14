package com.example.poker

data class GameUiState(

    val playerName: String = "Filipe",

    val botName: String = "Bot",

    val playerMoney: Int = 1500,

    val botMoney: Int = 1500,

    val playerText: String = "",

    val playerBetValue: Int = 0,

    val botText: String = "",

    val botBetValue: Int = 0,

    val minBetValue: Int = 0,

    val totalPot: Int = 0,

    val currentPot: Int = 0,

    val displayFlop: Boolean = false,

    val displayTurn: Boolean = false,

    val displayRiver: Boolean = false,

    val displayBotCards: Boolean = true,

    val displayFoldButton: Boolean = false,

    val displayCallButton: Boolean = false,

    val displayCheckButton: Boolean = false,

    val displayBetButton: Boolean = false,

    val displayRaiseButton: Boolean = false,

    val displayAllInButton: Boolean = false,

    val turnDelayTime: Int = 1000,

    val riverDelayTime: Int = 1000,

    val showdown: Boolean = false,

    val actionText: String = "",

    val gameSummary: List<List<String>> = ArrayList()
)
