package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card

@Composable
fun GameSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Bet(gameUiState.botBetValue)
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Pot(gameUiState.totalPot)
                TableCards(gameUiState)
                Hand(gameUiState)
            }
        }

        Box(contentAlignment = Alignment.CenterEnd) { Bet(gameUiState.playerBetValue) }
    }
}

@Composable
private fun Bet(bet: Int) {
    Column(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.background(Color.DarkGray.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
                .padding(5.dp),
            fontSize = 12.sp,
            color = Color.White,
            text = "Bet: $bet"
        )
    }
}

@Composable
private fun Pot(totalPot: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.background(Color.DarkGray.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
                .padding(5.dp),
            color = Color.White,
            text = "Pot: $totalPot"
        )
    }
}

@Composable
private fun TableCards(gameUiState: GameUiState) {
    Row (modifier = Modifier.padding(30.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ){
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(gameUiState.tableCards[0], gameUiState.displayFlop)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(gameUiState.tableCards[1], gameUiState.displayFlop)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(gameUiState.tableCards[2], gameUiState.displayFlop)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(gameUiState.tableCards[3], gameUiState.displayTurn)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(gameUiState.tableCards[4], gameUiState.displayRiver)
        }
    }
}

@Composable
private fun TableCardImage(card: Card?, display: Boolean) {
    Image(
        modifier = Modifier.alpha(if (display) 1f else 0f),
        painter = painterResource(card?.getCardDrawableResource() ?: R.drawable.card_back),
        contentScale = ContentScale.Fit,
        contentDescription = "card",
    )
}

@Composable
private fun Hand(gameUiState: GameUiState) {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, ) {
        Text(
            text = "Pair of Two",
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}