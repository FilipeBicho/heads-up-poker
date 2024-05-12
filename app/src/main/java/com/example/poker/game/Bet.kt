package com.example.poker.game

import com.example.poker.BIG_BLIND
import com.example.poker.GameUiState
import com.example.poker.POT
import com.example.poker.SMALL_BLIND
import com.example.poker.bot.ALLIN
import com.example.poker.bot.BET
import com.example.poker.bot.Bot
import com.example.poker.bot.CALL
import com.example.poker.bot.CHECK
import com.example.poker.bot.FOLD
import com.example.poker.bot.RAISE
import com.example.poker.cards.BOT
import com.example.poker.cards.PLAYER
import com.example.poker.cards.PRE_FLOP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Bet {

    // gameplay state
    private val gameplayStateFlow = MutableStateFlow(GameplayState())
    private val gameplayState: StateFlow<GameplayState> = gameplayStateFlow.asStateFlow()

    // ui state
    private val uiStateFlow = MutableStateFlow(GameUiState())
    private val uiState: StateFlow<GameUiState> = uiStateFlow.asStateFlow()

    lateinit var pokerChips: MutableList<Int>
    lateinit var bet: MutableList<Int>
    var totalPotValue: Int = -1

    private var dealer: Int = -1
    private var blind: Int = -1

    private var player: Int = -1
    private var opponent: Int = -1

    var round = PRE_FLOP

    private var computerBotValidActions = BooleanArray(6){false}

    private var playerName: List<String>

    private var gameSummaryList: MutableList<String> = mutableListOf()
    private var gameSummaryMap: MutableList<List<String>> = mutableListOf()

    private lateinit var computerBot: Bot

    init {
        gameSummaryMap = uiState.value.gameSummary.toMutableList()
        playerName = listOf(uiState.value.playerName)
    }

    private fun setValues() {
        pokerChips = gameplayState.value.pokerChips
        bet = gameplayState.value.bet
        blind = gameplayState.value.blind
        player = gameplayState.value.player
        opponent = gameplayState.value.opponent
        totalPotValue = gameplayState.value.totalPotValue
    }

    /**
     * all in is available if:
     *  - there is a previous bet:
     *      - player chips and player bet is equal or small than 2 times opponent bet
     *  - there is no previous bet:
     *      - player chips are smaller or equal to big bling
     */
    private fun isAllInAvailable(): Boolean {
        return if (bet[opponent] > 0) {
            pokerChips[player] + bet[player] <= bet[opponent] * 2
        } else {
            pokerChips[player] <= BIG_BLIND
        }
    }

    /**
     * bet is available if:
     *  - there is no previous bet
     *  - player chips value is bigger then big bling
     */
    private fun isBetAvailable(): Boolean {
        return bet[opponent] == 0 && pokerChips[player] > BIG_BLIND
    }

    /**
     * raise is available if:
     *  - there is a previous bet
     *  - player chips and player bet (if any) is bigger than 2 times opponent bet
     */
    private fun isRaiseAvailable(): Boolean {
        return bet[opponent] > 0 && pokerChips[player] + bet[player] > bet[opponent] * 2
    }

    fun preFlop() {
        setValues()
        if (pokerChips[blind] <= BIG_BLIND) {
            if (pokerChips[blind] <= SMALL_BLIND) {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pays all in
                bet[dealer] = bet[blind]
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]
                totalPotValue += pokerChips[POT]

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

                uiStateFlow.update { currentState ->
                    currentState.copy(
                        playerMoney = pokerChips[PLAYER],
                        computerMoney = pokerChips[BOT],
                        playerBetValue = bet[dealer],
                        currentPot = pokerChips[POT],
                        totalPot = totalPotValue,
                        gameSummary = gameSummaryMap
                    )
                }
                gameplayStateFlow.update { currentState ->
                    currentState.copy(
                        bet = bet,
                        pokerChips = pokerChips,
                        totalPotValue = totalPotValue
                    )
                }

                // TODO : showdown
            } else {

                // blind makes all in
                bet[blind] = pokerChips[blind]
                pokerChips[blind] = 0

                // dealer pay small blind
                bet[dealer] = SMALL_BLIND
                pokerChips[dealer] -= bet[dealer]

                // calculate pot
                pokerChips[POT] = bet[blind] + bet[dealer]

                gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
                gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"

                player = dealer

                uiStateFlow.update { currentState ->
                    currentState.copy(
                        playerMoney = pokerChips[PLAYER],
                        computerMoney = pokerChips[BOT],
                        playerBetValue = bet[dealer],
                        currentPot = pokerChips[POT],
                        totalPot = totalPotValue,
                        gameSummary = gameSummaryMap
                    )
                }
                gameplayStateFlow.update { currentState ->
                    currentState.copy(
                        bet = bet,
                        pokerChips = pokerChips,
                        totalPotValue = totalPotValue
                    )
                }

                if (player == PLAYER) {
                    uiStateFlow.update { currentState -> currentState.copy(
                        displayFoldButton = true,
                        displayCheckButton = false,
                        displayCallButton = true,
                        displayBetButton = false,
                        displayRaiseButton = false,
                        displayAllInButton = false
                    )}
                } else {
                    computerBotValidActions[FOLD] = true
                    computerBotValidActions[CHECK] = false
                    computerBotValidActions[CALL] = true
                    computerBotValidActions[BET] = false
                    computerBotValidActions[RAISE] = false
                    computerBotValidActions[ALLIN] = false

                    //TODO: bot
                }
            }
        } else if (pokerChips[dealer] <= SMALL_BLIND) {

            // dealer makes all in
            bet[dealer] = pokerChips[dealer]
            pokerChips[dealer] = 0

            // blind pays all in
            bet[blind] = bet[player]
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]
            totalPotValue += pokerChips[POT]

            gameSummaryList += "${playerName[blind]} makes all in ${bet[blind]} €"
            gameSummaryList += "${playerName[dealer]} pays all in ${bet[dealer]} €"

            uiStateFlow.update { currentState ->
                currentState.copy(
                    playerMoney = pokerChips[PLAYER],
                    computerMoney = pokerChips[BOT],
                    playerBetValue = bet[dealer],
                    currentPot = pokerChips[POT],
                    totalPot = totalPotValue,
                    gameSummary = gameSummaryMap
                )
            }
            gameplayStateFlow.update { currentState ->
                currentState.copy(
                    bet = bet,
                    pokerChips = pokerChips,
                    totalPotValue = totalPotValue
                )
            }

            // TODO : showdown
        } else {

            // dealer pay small blind
            bet[dealer] = SMALL_BLIND
            pokerChips[dealer] -= bet[dealer]

            // blind pay big blind
            bet[blind] = BIG_BLIND
            pokerChips[blind] -= bet[blind]

            // calculate pot
            pokerChips[POT] = bet[blind] + bet[dealer]

            gameSummaryList += "${playerName[dealer]} pays small blind ${bet[dealer]} €"
            gameSummaryList += "${playerName[blind]} pays big blind ${bet[blind]} €"

            player = dealer

            if (player == PLAYER) {
                uiStateFlow.update { currentState -> currentState.copy(
                    displayFoldButton = true,
                    displayCheckButton = false,
                    displayCallButton = true,
                    displayBetButton = isBetAvailable(),
                    displayRaiseButton = isRaiseAvailable(),
                    displayAllInButton = isAllInAvailable()
                )}
            } else {
                computerBotValidActions[FOLD] = true
                computerBotValidActions[CHECK] = false
                computerBotValidActions[CALL] = true
                computerBotValidActions[BET] = isBetAvailable()
                computerBotValidActions[RAISE] = isRaiseAvailable()
                computerBotValidActions[ALLIN] = isAllInAvailable()

                //TODO: bot
            }
        }
    }

}