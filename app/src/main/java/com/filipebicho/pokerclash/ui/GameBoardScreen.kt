package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.ui.gamescreen.BotSection
import com.filipebicho.pokerclash.ui.gamescreen.GameSection
import com.filipebicho.pokerclash.ui.gamescreen.PlayerSection

@Composable
fun GameBoardScreen(gameUiState: GameUiState, gameViewModel: GameViewModel) {
    Background()

    Column(
        modifier = Modifier
            .padding(16.dp, 4.dp, 16.dp, 2.dp)
            .fillMaxSize(),
    ) {
        TopRow(gameUiState, modifier = Modifier.weight(0.12f))
        MiddleRow(gameUiState, gameViewModel, modifier = Modifier.weight(0.58f))
        BottomRow(gameUiState, modifier = Modifier.weight(0.25f))
    }
}

@Composable
fun TopRow(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    BotSection(gameUiState, modifier)
}

@Composable
fun MiddleRow(gameUiState: GameUiState, gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    GameSection(gameUiState, gameViewModel, modifier)
}

@Composable
fun BottomRow(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    PlayerSection(gameUiState, modifier)
}


@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(gameUiState = GameUiState(), gameViewModel = GameViewModel())
}