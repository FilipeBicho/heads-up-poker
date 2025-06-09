package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.cards.BOT

@Composable
fun BotSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxHeight()) {
        InfoSection(gameUiState = gameUiState, modifier = Modifier.weight(0.33f))
        BotCards(gameUiState = gameUiState, modifier = Modifier.weight(0.33f))
        WinCountSection(gameUiState = gameUiState, modifier = Modifier.weight(0.33f))
    }
}

@Composable
private fun InfoSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(0.dp, 5.dp, 0.dp, 0.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Model: ${gameUiState.chatgptModel}", fontSize = 10.sp, color = Color.White)

        if (gameUiState.dealer == BOT) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                DealerChipImage()
            }
        }
    }
}

@Composable
fun WinCountSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "${gameUiState.playerName} wins: ${gameUiState.playerWins}", fontSize = 10.sp, color = Color.White)
        Text(text = "${gameUiState.simulatedPlayerName} wins: ${gameUiState.botWins}", fontSize = 10.sp, color = Color.White)
    }
}

@Composable
private fun BotCards(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 4.dp, 4.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Cards(
            cards = gameUiState.botCards,
            display = gameUiState.displayBotCards,
            modifier = modifier
        )
        NameAndMoneySection(
            name = gameUiState.simulatedPlayerName,
            action = gameUiState.actions[BOT],
            money = gameUiState.botMoney
        )
    }
}



