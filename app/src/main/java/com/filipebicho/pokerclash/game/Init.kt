package com.filipebicho.pokerclash.game

import java.util.Locale
import com.filipebicho.pokerclash.bot.ChatgptBot
import com.filipebicho.pokerclash.bot.NO_ACTION
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Dealer
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.cards.PRE_FLOP
import com.filipebicho.pokerclash.data.Data.action
import com.filipebicho.pokerclash.data.Data.actionHistory
import com.filipebicho.pokerclash.data.Data.bet
import com.filipebicho.pokerclash.data.Data.betting
import com.filipebicho.pokerclash.data.Data.bigBlind
import com.filipebicho.pokerclash.data.Data.blind
import com.filipebicho.pokerclash.data.Data.blindLevels
import com.filipebicho.pokerclash.data.Data.botMoney
import com.filipebicho.pokerclash.data.Data.cardDealer
import com.filipebicho.pokerclash.data.Data.checkAvailable
import com.filipebicho.pokerclash.data.Data.botCards
import com.filipebicho.pokerclash.data.Data.botWins
import com.filipebicho.pokerclash.data.Data.chatGptBot
import com.filipebicho.pokerclash.data.Data.currentBot
import com.filipebicho.pokerclash.data.Data.dealer
import com.filipebicho.pokerclash.data.Data.displayLevelTimerJob
import com.filipebicho.pokerclash.data.Data.gameNumber
import com.filipebicho.pokerclash.data.Data.gameSummaryList
import com.filipebicho.pokerclash.data.Data.gameSummaryMap
import com.filipebicho.pokerclash.data.Data.level
import com.filipebicho.pokerclash.data.Data.levelUp
import com.filipebicho.pokerclash.data.Data.mainPot
import com.filipebicho.pokerclash.data.Data.odds
import com.filipebicho.pokerclash.data.Data.opponent
import com.filipebicho.pokerclash.data.Data.player
import com.filipebicho.pokerclash.data.Data.playerCards
import com.filipebicho.pokerclash.data.Data.playerMoney
import com.filipebicho.pokerclash.data.Data.playerWins
import com.filipebicho.pokerclash.data.Data.pokerChips
import com.filipebicho.pokerclash.data.Data.round
import com.filipebicho.pokerclash.data.Data.roundPot
import com.filipebicho.pokerclash.data.Data.smallBlind
import com.filipebicho.pokerclash.data.Data.tableCards
import com.filipebicho.pokerclash.data.Data.uiStateFlow
import com.filipebicho.pokerclash.data.INITIAL_BIG_BLIND
import com.filipebicho.pokerclash.data.INITIAL_SMALL_BLIND
import com.filipebicho.pokerclash.data.LEVEL_TIMER
import com.filipebicho.pokerclash.odds.Combinations
import com.filipebicho.pokerclash.odds.Odds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.text.format
import kotlin.time.Duration.Companion.seconds

class Init(var coroutineScope: CoroutineScope, stats: Stats) {

    init {
        betting = Betting(coroutineScope, stats)
        chatGptBot = ChatgptBot(stats)
    }

    private fun blindTimer(): Flow<Int> = flow {
        var count = 0
        while (!levelUp) {
            emit(count)
            delay(1.seconds)
            count++

            if (count >= LEVEL_TIMER) {
                levelUp = true
                uiStateFlow.update { currentState -> currentState.copy(
                    levelUp = levelUp
                )}
            }
        }
    }

    private fun dealCards() {
        round = PRE_FLOP

        playerCards.clear()
        botCards.clear()
        tableCards.clear()

        cardDealer = Dealer()
        cardDealer.shuffle()
        cardDealer.setPlayerCards(playerCards, botCards)
        cardDealer.setFlopCards(tableCards)
        cardDealer.setTurnCard(tableCards)
        cardDealer.setRiverCard(tableCards)

        odds = Odds(Combinations(tableCards.subList(0,3)).combinations)

        uiStateFlow.update { currentState -> currentState.copy(
            displayBotCards = false,
            displayPlayerCards = true,
            displayFlop = false,
            displayTurn = false,
            displayRiver = false,
            showdown = false,
            playerCards = playerCards.toList(),
            botCards = botCards.toList(),
            tableCards = tableCards.toList()
        )}
    }

    /**
     * Called at the begin of each new game iteration
     */
    private fun initValues() {
        action = NO_ACTION

        // players
        bet[PLAYER] = 0
        bet[BOT] = 0

        // pot
        roundPot = 0
        mainPot = 0

        if (levelUp && level < 10) {
            level += 1
            levelUp = false
            smallBlind = blindLevels[level]?.smallBlind ?: INITIAL_SMALL_BLIND
            bigBlind = blindLevels[level]?.bigBlind ?: INITIAL_BIG_BLIND
        }

        checkAvailable = true
        gameSummaryList.clear()

        if (gameSummaryMap.isNotEmpty()) {
            gameNumber += 1
        }

        gameSummaryList.add("Game ${gameNumber+1}")
        gameSummaryMap.add(gameNumber, gameSummaryList.toList())

        actionHistory.clear()

        // init or change dealer
        dealer = if (dealer == -1) {
            (0..1).random()
        } else {
            if (dealer == 0) 1 else 0
        }
        blind = if (dealer == 0) 1 else 0
        player = dealer
        opponent = blind

        uiStateFlow.update { currentState -> currentState.copy(
            playerBet = 0,
            botBet = 0,
            playerMinRaise = 0,
            mainPot = 0,
            roundPot = 0,
            bigBlind = bigBlind,
            gameSummary = gameSummaryMap,
            playerHandResult = "",
            playerOdds = -1,
            botOdds = -1,
            isPlayerTurn = player == PLAYER,
            newGame = false,
            dealer = dealer,
            displayGameResult = false,
            displayLevelTimer = true,
            level = level,
            levelUp = levelUp,
            winner = -1,
            winningHand = null,
            displayFold = false,
            displayPot = true,
            playerWins = playerWins[currentBot],
            botWins = botWins[currentBot],
        )}
    }

    fun initLevelTimer() {
        // max level
        if (level >= 10) {
            return
        }

        if (displayLevelTimerJob != null && displayLevelTimerJob!!.isActive) {
            return
        }

        levelUp = false
        displayLevelTimerJob = coroutineScope.launch {
            blindTimer().onEach { secondsPassed ->
                val minutes = secondsPassed / 60
                val seconds = secondsPassed % 60
                val formattedTime = String.format(Locale.UK, "%02d:%02d", minutes, seconds)

                uiStateFlow.update { currentState -> currentState.copy(
                    levelTimer = formattedTime
                )}
            }.collect()
        }
    }

    /**
     * Called at the begin of a new game
     */
    fun initGame() {
        pokerChips[PLAYER] = playerMoney
        pokerChips[BOT] = botMoney
        levelUp = false
        level = 4
        smallBlind = blindLevels[level]?.smallBlind ?: INITIAL_SMALL_BLIND
        bigBlind = blindLevels[level]?.bigBlind ?: INITIAL_BIG_BLIND
        displayLevelTimerJob?.cancel()
        displayLevelTimerJob = null
        newGame()
    }

    fun newGame() {
        initValues()
        dealCards()

        if (displayLevelTimerJob == null || !displayLevelTimerJob!!.isActive) {
            initLevelTimer()
        }

        betting.preFlop()
    }
}