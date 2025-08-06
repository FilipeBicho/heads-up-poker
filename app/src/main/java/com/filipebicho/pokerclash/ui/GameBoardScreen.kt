package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.data.GameUiState
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.ui.gamescreen.BotSection
import com.filipebicho.pokerclash.ui.gamescreen.GameSection
import com.filipebicho.pokerclash.ui.gamescreen.PlayerSection

@Composable
fun GameBoardScreen(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    navController: NavHostController
) {
    Background()

    Column(
        modifier = Modifier
            .padding(16.dp, 20.dp, 16.dp, 5.dp)
            .fillMaxSize(),
    ) {
        TopRow(gameUiState = gameUiState, modifier = Modifier.weight(0.12f))
        MiddleRow(
            gameUiState = gameUiState,
            gameViewModel = gameViewModel,
            navController = navController,
            modifier = Modifier.weight(0.58f)
        )
        BottomRow(
            gameUiState = gameUiState,
            gameViewModel = gameViewModel,
            modifier = Modifier.weight(0.25f)
        )
    }
}

@Composable
fun TopRow(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    BotSection(gameUiState, modifier)
}

@Composable
fun MiddleRow(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    GameSection(
        gameUiState = gameUiState,
        gameViewModel = gameViewModel,
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun BottomRow(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    PlayerSection(
        gameUiState = gameUiState,
        gameViewModel = gameViewModel,
        modifier = modifier
    )
}