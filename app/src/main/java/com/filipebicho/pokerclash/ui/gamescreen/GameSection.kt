package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card

@Composable
fun GameSection(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Bet(bet = gameUiState.botBet)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MainPot(value = gameUiState.mainPot)
                TableCards(gameUiState = gameUiState)
                RoundPot(roundPot = gameUiState.roundPot)
                Hand(hand = gameUiState.playerHandResult)
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier
                            .background(
                                Color.DarkGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(5.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        text = "Debug: ${gameUiState.playerMoney + gameUiState.botMoney + gameUiState.mainPot}"
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            SummaryToggleButton(gameUiState = gameUiState, gameViewModel = gameViewModel)
        }

        Box(contentAlignment = Alignment.CenterEnd) {
            Bet(bet = gameUiState.playerBet)
        }
    }
}

@Composable
private fun SummaryToggleButton(gameUiState: GameUiState, gameViewModel: GameViewModel) {
    Box(modifier = Modifier.clickable {
        gameViewModel.toggleGameSummary()
    }) {
        if (gameUiState.displaySummary) {
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = "Summary",
                tint = Color.White,
                modifier = Modifier.padding(0.dp)
            )
        } else {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                contentDescription = "Summary",
                tint = Color.White,
                modifier = Modifier.padding(0.dp)
            )
        }

    }
}

@Composable
private fun Bet(bet: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .background(
                    Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            fontSize = 12.sp,
            color = Color.White,
            text = "Bet: $bet"
        )
    }
}

@Composable
private fun MainPot(value: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier
                .background(
                    Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            color = Color.White,
            text = "Pot: $value"
        )
    }
}

@Composable
private fun RoundPot(roundPot: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier
                .background(
                    Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            color = Color.White,
            fontSize = 10.sp,
            text = "Round Pot: $roundPot"
        )
    }
}

@Composable
private fun TableCards(gameUiState: GameUiState) {
    Row(
        modifier = Modifier.padding(30.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[0], display = gameUiState.displayFlop)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[1], display = gameUiState.displayFlop)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[2], display = gameUiState.displayFlop)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[3], display = gameUiState.displayTurn)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[4], display = gameUiState.displayRiver)
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
private fun Hand(hand: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = hand,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}