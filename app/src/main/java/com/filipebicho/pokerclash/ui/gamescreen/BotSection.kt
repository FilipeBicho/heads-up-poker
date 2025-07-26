package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.cards.BOT

@Composable
fun BotSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxHeight()) {
        WinCount(gameUiState.playerName, gameUiState.playerWins, modifier = Modifier.weight(0.33f), displayDealer = gameUiState.dealer == BOT)
        BotCards(gameUiState = gameUiState, modifier = Modifier.weight(0.33f))
        WinCount(gameUiState.simulatedPlayerName, gameUiState.botWins, modifier = Modifier.weight(0.33f), false)
    }
}

@Composable
fun WinCount(
    name: String,
    wins: Int, modifier:
    Modifier = Modifier,
    displayDealer: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
        Text(
            text = "$wins",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = Color.LightGray
        )
        Text(
            text = "Wins",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )

        if (displayDealer) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                DealerChipImage()
            }
        }
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



