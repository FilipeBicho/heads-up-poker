package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
@Preview
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Background()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                ) {
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                        if (!gameViewModel.isPlayerDealer()) {
                            DealerChipImage()
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        if (gameUiState.showdown) {
                            Text(
                                text = "${gameUiState.computerOddsValue} %",
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    if (gameViewModel.computerCards.isNotEmpty()) {
                        CardsSection(
                            gameViewModel.computerCards,
                            gameUiState.computerName,
                            gameUiState.computerMoney,
                            gameUiState.displayComputerCards
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                ) {

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                ) {
                    Column (
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Pot: ${gameUiState.currentPot} €",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                        Text(
                            text = "Total: ${gameUiState.totalPot} €",
                            fontSize = 12.sp
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Column {
                        Text(
                            text = "${gameUiState.computerBet} €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.Top),
                            fontSize = 15.sp,
                        )

                        if (gameViewModel.tableCards.isNotEmpty()) {
                            Row(modifier = Modifier.weight(0.6f)) {

                                AnimatedVisibility(
                                    visible = gameUiState.displayFlop,
                                    enter = expandHorizontally(tween(1000, 0))
                                ) {
                                    CardImage(card = gameViewModel.tableCards[0], Modifier.padding(all = 5.dp), true)
                                }

                                AnimatedVisibility(
                                    visible = gameUiState.displayFlop,
                                    enter = expandHorizontally(tween(1000, 0))
                                ) {
                                    CardImage(card = gameViewModel.tableCards[1], Modifier.padding(all = 5.dp), true)
                                }

                                AnimatedVisibility(
                                    visible = gameUiState.displayFlop,
                                    enter = expandHorizontally(tween(1000, 0))
                                ) {
                                    CardImage(card = gameViewModel.tableCards[2], Modifier.padding(all = 5.dp), true)
                                }

                                AnimatedVisibility(
                                    visible = gameUiState.displayTurn,
                                    enter = expandHorizontally(tween(1000, gameUiState.turnDelayTime))
                                ) {
                                    CardImage(card = gameViewModel.tableCards[3], Modifier.padding(all = 5.dp), true)
                                }
                                AnimatedVisibility(
                                    visible = gameUiState.displayRiver,
                                    enter = expandHorizontally(tween(1000, gameUiState.riverDelayTime))
                                ) {
                                    CardImage(card = gameViewModel.tableCards[4], Modifier.padding(all = 5.dp), true)
                                }
                            }
                        }

                        Text(
                            text = "${gameUiState.playerBet} €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.Bottom),
                            fontSize = 15.sp,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .height(25.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        if (gameViewModel.isPlayerTurn()) {
                            if (BIG_BLIND * 2 > gameUiState.totalPot) {
                                BetButton(text = "2 BB") { gameViewModel.updatePlayerBet(BIG_BLIND * 2) }
                            }
                            BetButton(text = "Pot") { gameViewModel.updatePlayerBet(gameUiState.totalPot) }
                            BetButton(text = "Max") { gameViewModel.updatePlayerBet(gameUiState.playerMoney) }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)

                ) {
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        if (gameViewModel.isPlayerDealer()) {
                            DealerChipImage()
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        if (gameUiState.showdown) {
                            Text(
                                text = "${gameUiState.playerOddsValue} %",
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    if (gameViewModel.playerCards.isNotEmpty()) {
                        CardsSection(
                            gameViewModel.playerCards,
                            gameUiState.playerName,
                            gameUiState.playerMoney,
                            true
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                ) {
                    if (gameViewModel.isPlayerTurn() && gameUiState.playerMoney > 0) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.4f)
                            ) {
                                BetSlider(gameUiState, gameViewModel)
                            }

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(0.6f),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                if (gameUiState.displayFoldButton) {
                                    GameActionButton("Fold") { gameViewModel.fold() }
                                }

                                if (gameUiState.displayCheckButton) {
                                    GameActionButton("Check") { gameViewModel.check() }
                                }

                                if (gameUiState.displayCallButton) {
                                    GameActionButton("Call") { gameViewModel.call() }
                                }

                                if (gameUiState.displayBetButton) {
                                    GameActionButton("Bet") { gameViewModel.bet() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardsSection(cards: MutableList<Card>, name: String, money: Int, displayCard: Boolean) {
    Box {
        CardImage(
            card = cards.first(),
            Modifier
                .rotate(-5f)
                .padding(top = 5.dp, bottom = 5.dp, end = 15.dp)
                .align(Alignment.TopCenter),
            displayCard
        )
        CardImage(
            card = cards.last(),
            Modifier
                .zIndex(2f)
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                .rotate(5f)
                .align(Alignment.TopCenter),
            displayCard
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .zIndex(3f)
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter)
                .width(150.dp)
                .background(Color.Black.copy(alpha = 0.9f), shape = RoundedCornerShape(5.dp))
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Divider(
                color = Color.White,
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            Text(
                text = "$money €",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun CardImage(card: Card, modifier: Modifier, displayCards: Boolean) {
    val context = LocalContext.current
    val imageId = if (displayCards) {
        context.resources.getIdentifier(
            card.getCardImagePath(),
            "drawable",
            context.packageName
        )
    } else {
        R.drawable.card_back
    }

    Image(
        modifier = modifier,
        painter = painterResource(id = imageId),
        contentScale = ContentScale.Fit,
        contentDescription = "card"
    )
}

@Composable
private fun DealerChipImage() {
    Image(
        painter = painterResource(id = R.drawable.dealer),
        contentDescription = "Dealer chip image",
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun GameActionButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                2.dp,
                colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            )
            .height(35.dp)
            .width(80.dp)
            .background(colorResource(id = R.color.button_red))
    )
    {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.align(Alignment.Center)
        )
        {
            Text(text = text, fontSize = 15.sp)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BetSlider(gameUiState: GameUiState, gameViewModel: GameViewModel) {
    var betValue by remember(key1 = gameUiState.playerBetValue) { mutableStateOf(gameUiState.playerBetValue) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .background(
                Color.DarkGray.copy(alpha = 0.9f),
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        Box(modifier = Modifier.weight(0.2f)) {
            BasicTextField(
                value = betValue.toString(),
                onValueChange = {
                    if (it.isNotEmpty() && it.isDigitsOnly() && it.toInt() <= gameUiState.playerMoney) {
                        betValue = it.toInt()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        gameViewModel.updatePlayerBet(betValue)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                maxLines = 1,
                textStyle = TextStyle(color = Color.Black, textAlign = TextAlign.Center),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .background(Color.White)
            )
        }

        Box(modifier = Modifier.weight(0.8f)) {
            Slider(
                value = betValue.toFloat(),
                onValueChange = { betValue = it.roundToInt() },
                onValueChangeFinished = { gameViewModel.updatePlayerBet(betValue) },
                modifier = Modifier.padding(end = 10.dp),
                valueRange = BIG_BLIND.toFloat()..gameUiState.playerMoney.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.LightGray,
                    activeTrackColor = colorResource(id = R.color.button_red),
                    inactiveTrackColor = Color.Black
                ),
            )
        }
    }
}

@Composable
private fun BetButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray.copy(alpha = 0.9f),
            contentColor = Color.White
        ),
        modifier = Modifier.defaultMinSize(minWidth = ButtonDefaults.MinWidth)
    ) {
        Text(text)
    }
}
