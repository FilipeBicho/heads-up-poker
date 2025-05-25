package com.filipebicho.pokerclash

import com.filipebicho.pokerclash.cards.Card

data class GameUiState(

    val dealer: Int = -1,

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
    val displaySummary: Boolean = false,

    // Money
    val playerMoney: Int = 1500,
    val botMoney: Int = 1500,
    val playerBet: Int = 0,
    val playerMinRaise: Int = 0,
    val playerCurrentRaise: Int = 0,
    val playerCall: Int = 0,
    val botBet: Int = 0,
    val pot: Int = 0,

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
    val isPlayerTurn: Boolean = false,
    val displayBetButton: Boolean = false,
    val displayAllInButton: Boolean = false,

    // Count display
    val playerWins: Int = 0,
    val botWins: Int = 0
)
