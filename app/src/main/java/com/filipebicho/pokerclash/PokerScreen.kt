package com.filipebicho.pokerclash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.filipebicho.pokerclash.data.Data.uiState
import com.filipebicho.pokerclash.ui.BotSelectionScreen
import com.filipebicho.pokerclash.ui.StartGameScreen
import com.filipebicho.pokerclash.ui.GameBoardScreen

/**
 * enum values that represent the screens in the app
 */
enum class PokerScreen() {
    Start(),
    BotSelection(),
    Game()
}

@Composable
fun PokerApp(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val gameUiState by uiState.collectAsState()
    // Define a development flag (you'd likely get this from build config)
    val isDevelopmentMode = true // Set to false for production

    // Use a LaunchedEffect to run initialization only once when the key changes
    LaunchedEffect(Unit) {
        if (isDevelopmentMode) {
            // Temporarily initialize for direct game access in development
            viewModel.setPlayerName("Filipe")
            viewModel.setBot(Pair("GPT 4o mini", "gpt-4o"))
            viewModel.startGame()
        }
    }

    NavHost(
        navController = navController,
        startDestination = PokerScreen.Game.name,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        composable(route = PokerScreen.Start.name) {
            StartGameScreen(
                onStartButtonClicked = {
                    viewModel.setPlayerName(it)
                    navController.navigate(PokerScreen.BotSelection.name)
                },
            )
        }
        composable(route = PokerScreen.BotSelection.name) {
            BotSelectionScreen(
                onBotButtonClicked = {
                    viewModel.setBot(it)
                    viewModel.startGame()
                    navController.navigate(PokerScreen.Game.name)
                }
            )
        }

        composable(route = PokerScreen.Game.name) {
            GameBoardScreen(gameUiState, viewModel)
        }
    }
}
