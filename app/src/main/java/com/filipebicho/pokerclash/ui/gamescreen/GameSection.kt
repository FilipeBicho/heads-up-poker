package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card
import kotlinx.coroutines.delay

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
            if (gameUiState.showdown && gameUiState.playerOdds != -1 && gameUiState.botOdds != -1) {
                val oddsColor = if (gameUiState.botOdds > gameUiState.playerOdds)
                    Color.Green.copy(alpha = 0.5f)
                else if (gameUiState.botOdds < gameUiState.playerOdds)
                    Color.Red.copy(alpha = 0.5f)
                else
                    Color.Yellow.copy(alpha = 0.5f)
                Odds(odds = "${gameUiState.botOdds} %", color = oddsColor)
            } else {
                Bet(bet = "Bet ${gameUiState.botBet}", gameUiState.botBet > 0)
            }
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

            if (gameUiState.showdown && gameUiState.playerOdds != -1 && gameUiState.botOdds != -1) {
                val oddsColor = if (gameUiState.playerOdds > gameUiState.botOdds)
                    Color.Green.copy(alpha = 0.5f)
                else if (gameUiState.playerOdds < gameUiState.botOdds)
                    Color.Red.copy(alpha = 0.5f)
                else
                    Color.Yellow.copy(alpha = 0.5f)
                Odds(odds = "${gameUiState.playerOdds} %", color = oddsColor)
            } else {
                Bet(bet = "Bet ${gameUiState.playerBet}", gameUiState.playerBet > 0)
            }
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
private fun Bet(bet: String, display: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .background(
                    Color.DarkGray.copy(alpha = if (display) 0.5f else 0f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp)
                .alpha(if (display) 1f else 0f),
            fontSize = 12.sp,
            color = Color.White,
            text = bet
        )
    }
}

@Composable
private fun Odds(odds: String, color: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .background(
                    color,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            fontSize = 12.sp,
            color = Color.White,
            text = odds
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

    // Local states to control individual card appearance
    var showFlopCard1 by remember { mutableStateOf(false) }
    var showFlopCard2 by remember { mutableStateOf(false) }
    var showFlopCard3 by remember { mutableStateOf(false) }

    val cardDisplayDelay = 500L

    // Effect for Flop cards
    LaunchedEffect(gameUiState.displayFlop) {
        if (gameUiState.displayFlop) {
            showFlopCard1 = true
            delay(cardDisplayDelay)
            showFlopCard2 = true
            delay(cardDisplayDelay)
            showFlopCard3 = true
        } else {
            // Optionally reset if displayFlop becomes false (e.g., new round)
            showFlopCard1 = false
            showFlopCard2 = false
            showFlopCard3 = false
        }
    }

    Row(
        modifier = Modifier.padding(30.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[0], display = showFlopCard1)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[1], display = showFlopCard2)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[2], display = showFlopCard3)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[3], display = gameUiState.displayTurn)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            TableCardImage(card = gameUiState.tableCards[4], display = gameUiState.displayRiver)
        }
    }
}

@Composable
private fun TableCardImage(card: Card?, display: Boolean) {
    // Animate rotation from 90 (edge-on from one side) to 0 (face forward)
    val rotationY by animateFloatAsState(
        targetValue = if (display) 0f else 90f,
        animationSpec = tween(durationMillis = 600),
        label = "cardFlipRotation"
    )

    // Animate scaleX to make it appear like it's unfolding
    // When not displayed, scaleX is 0. When displayed, scaleX is 1.
    val scaleX by animateFloatAsState(
        targetValue = if (display) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "cardScaleX"
    )

    Image(
        painter = painterResource(card?.getCardDrawableResource() ?: R.drawable.card_back),
        contentDescription = "Playing Card - ${card?.toString() ?: "Face"}",
        modifier = Modifier
            .graphicsLayer {
                this.rotationY = rotationY
                this.scaleX = scaleX // Apply horizontal scale
                cameraDistance = 12 * density
                // Ensure the pivot point for rotation and scaling makes sense.
                // Default is center, which should work for this.
            }
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