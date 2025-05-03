package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.ui.gamescreen.GameSection
import com.filipebicho.pokerclash.ui.gamescreen.PlayerSection

@Composable
fun GameBoardScreen(gameViewModel: GameViewModel) {
    Background()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxSize(),
    ) {
        // Top Row (12% of screen height)
        TopRow(modifier = Modifier.weight(0.12f))

        // Middle Row (58% of screen height)
        MiddleRow(modifier = Modifier.weight(0.58f))

        // Bottom Row (25% of screen height)
        BottomRow(modifier = Modifier.weight(0.25f))
    }
}

@Composable
fun TopRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White)
            .padding(8.dp), // Added padding inside the row
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        // TODO: Add content for the top row
        Text(text = "Top Row", color = Color.White)
    }
}

@Composable
fun MiddleRow(modifier: Modifier = Modifier) {
    GameSection(modifier)
}

@Composable
fun BottomRow(modifier: Modifier = Modifier) {
    PlayerSection(modifier)
}



@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(
        gameViewModel = GameViewModel()
    )
}