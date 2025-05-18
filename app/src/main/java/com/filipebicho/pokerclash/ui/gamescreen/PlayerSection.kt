package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.ui.GameBoardScreen
import kotlin.math.roundToInt

@Composable
fun PlayerSection(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    if (!gameUiState.displaySummary) {
        Column(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerCards(gameUiState = gameUiState, modifier = Modifier.weight(0.35f))
                SliderSection(
                    gameUiState = gameUiState,
                    modifier = Modifier
                        .weight(0.65f)
                        .alpha(if (gameUiState.isPlayerTurn) 1f else 0f)
                )
            }
            ButtonSection(
                gameUiState = gameUiState,
                gameViewModel = gameViewModel,
                modifier = Modifier.weight(0.3f)
            )
        }
    } else {
        SummarySection(gameUiState, modifier)
    }
}

/**
 * Display the player cards, name and money
 */
@Composable
private fun PlayerCards(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 4.dp, 4.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Cards(cards = gameUiState.playerCards, display = true, modifier = modifier)
        NameAndMoneySection(name = gameUiState.playerName, money = gameUiState.playerMoney)
        if (gameUiState.dealer == PLAYER) {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                DealerChipImage()
            }
        }
    }
}

/**
 * Display the slider and small bet buttons
 */
@Composable
private fun SliderSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SmallBetButtonsSection(gameUiState = gameUiState, modifier = modifier)
        BetSlider(gameUiState = gameUiState, modifier = modifier)
    }
}

@Composable
private fun SmallBetButtonsSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    val enabled = gameUiState.isPlayerTurn
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        SmallBetButton(
            text = "Min",
            onClick = {},
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            text = "3 BB",
            onClick = {},
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            text = "Pot",
            onClick = {},
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            text = "Max",
            onClick = {},
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SmallBetButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(2.dp, 0.dp),
        modifier = modifier.height(30.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.button_red),
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            ),
        )
    }
}

@Composable
private fun BetSlider(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableIntStateOf(gameUiState.minPlayerBet) }
    val enabled = gameUiState.isPlayerTurn

    Row(modifier = modifier) {
        BasicTextField(
            value = sliderPosition.toString(),
            onValueChange = {},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
            enabled = enabled,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.25f)
                .height(30.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 8.dp, 0.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(5.dp)),
                    contentAlignment = Alignment.Center
                ) { innerTextField() }
            }
        )
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { sliderPosition = it.roundToInt() },
            colors = SliderDefaults.colors(
                thumbColor = Color.LightGray,
                activeTrackColor = colorResource(id = R.color.button_red),
                inactiveTrackColor = Color.Black
            ),
            valueRange = gameUiState.minPlayerBet.toFloat()..1500.toFloat(),
            enabled = enabled,
            modifier = Modifier
                .weight(0.7f)
                .height(30.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun ButtonSection(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val enabled = gameUiState.isPlayerTurn
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            text = "Fold",
            onClick = { gameViewModel.fold() },
            enabled = enabled && gameUiState.displayFoldButton,
            modifier = Modifier.weight(1f)
        )

        if (gameUiState.displayCheckButton) {
            Button(
                text = "Check",
                onClick = { gameViewModel.check() },
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
        }

        if (gameUiState.displayCallButton) {
            Button(
                text = "Call ${gameUiState.playerCall}",
                onClick = { gameViewModel.call() },
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            text = "Bet ${gameUiState.playerRaiseBet}",
            onClick = { gameViewModel.bet(gameUiState.playerRaiseBet) },
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().alpha(if (enabled) 1f else 0f),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.button_red),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(12.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun SummarySection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState(), true, null, true)
            ) {
                gameUiState.gameSummary.forEach { it ->
                    it.forEach {
                        Text(
                            text = it,
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(gameUiState = GameUiState(), gameViewModel = GameViewModel())
}