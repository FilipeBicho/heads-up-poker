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
import com.filipebicho.pokerclash.ui.gamescreen.BotSection
import com.filipebicho.pokerclash.ui.gamescreen.GameSection
import com.filipebicho.pokerclash.ui.gamescreen.PlayerSection

@Composable
fun GameBoardScreen(gameUiState: GameUiState) {
    Background()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxSize(),
    ) {
        TopRow(modifier = Modifier.weight(0.12f))
        MiddleRow(modifier = Modifier.weight(0.58f))
        BottomRow(gameUiState, modifier = Modifier.weight(0.25f))
    }
}

@Composable
fun TopRow(modifier: Modifier = Modifier) {
    BotSection()
}

@Composable
fun MiddleRow(modifier: Modifier = Modifier) {
    GameSection(modifier)
}

@Composable
fun BottomRow(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    PlayerSection(gameUiState, modifier)
}


@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(gameUiState = GameUiState())
}