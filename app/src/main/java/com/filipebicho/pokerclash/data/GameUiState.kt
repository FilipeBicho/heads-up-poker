package com.filipebicho.pokerclash.data

import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.hand.Hand

data class GameUiState(

    val dealer: Int = -1,

    // Names
    val playerName: String = "",
    val simulatedPlayerName: String = "",
    val chatgptModel: String = "GPT-4o",

    // Actions
    val actions: List<String> = mutableListOf(),

    // Hand
    val playerHandResult: String = "",

    // Summary Text
    val name: List<String> = mutableListOf(),
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
    val mainPot: Int = 0,
    val roundPot: Int = 0,
    val displayPot: Boolean = true,

    // Cards
    val playerCards: List<Card> = ArrayList(),
    val botCards: List<Card> = ArrayList(),
    val tableCards: List<Card> = ArrayList(),
    val winningHand: Hand? = null,

    // Odds
    val playerOdds: Int = -1,
    val botOdds: Int = -1,

    // Round
    val displayFlop: Boolean = false,
    val displayTurn: Boolean = false,
    val displayRiver: Boolean = false,
    val displayBotCards: Boolean = true,
    val displayPlayerCards: Boolean = true,
    val showdown: Boolean = false,
    val newGame: Boolean = false,
    val displayGameResult: Boolean = false,
    val winner: Int = -1,
    val displayFold: Boolean = false,
    val level: Int = 1,
    val levelTimer: String = "",
    val displayLevelTimer: Boolean = false,
    val levelUp: Boolean = false,

    // Display Bet Buttons
    val displayFoldButton: Boolean = false,
    val displayCallButton: Boolean = false,
    val displayCheckButton: Boolean = false,
    val displayBetButton: Boolean = false,
    val displayMinSmallButton: Boolean = false,
    val display3BBSmallButton: Boolean = false,
    val displayPotSmallButton: Boolean = false,
    val displayAllInSmallButton: Boolean = false,
    val isPlayerTurn: Boolean = false,

    // Count display
    val playerWins: Int = 0,
    val botWins: Int = 0
)
