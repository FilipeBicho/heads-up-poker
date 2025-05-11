package com.filipebicho.pokerclash

import com.filipebicho.pokerclash.cards.Card

data class GameUiState(

    // Names
    val playerName: String = "",
    val botName: String = "",
    val botModel: String = "",

    // Summary Text
    val name: List<String> = mutableListOf(),
    val playerText: String = "",
    val botText: String = "",
    val actionText: String = "",
    val gameSummary: List<List<String>> = ArrayList(),

    // Money
    val playerMoney: Int = 1500,
    val botMoney: Int = 1500,
    val playerBetValue: Int = 0,
    val botBetValue: Int = 0,
    val totalPot: Int = 0,
    val currentPot: Int = 0,
    val minPlayerBet: Int = BIG_BLIND,

    // Cards
    val playerCards: List<Card> = ArrayList(),
    val botCards: List<Card> = ArrayList(),
    val tableCards: List<Card> = ArrayList(),

    // Display cards
    val displayFlop: Boolean = false,
    val displayTurn: Boolean = false,
    val displayRiver: Boolean = false,
    val displayBotCards: Boolean = true,
    val showdown: Boolean = false,
    val newGame: Boolean = false,

    // Display Bet Buttons
    val displayFoldButton: Boolean = false,
    val displayCallButton: Boolean = false,
    val displayCheckButton: Boolean = false,
    val displayBetButtons: Boolean = false,
    val displayBetButton: Boolean = false,
    val displayRaiseButton: Boolean = false,
    val displayAllInButton: Boolean = false,

    // Count display
    val playerWins: Int = 0,
    val botWins: Int = 0
)
