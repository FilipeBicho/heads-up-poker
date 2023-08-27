package com.example.poker

import android.annotation.SuppressLint
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {

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
                        DealerChipImage()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    if (gameViewModel.player1Cards.isNotEmpty()) {
                        PlayerCards(gameViewModel.player1Cards)
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
                    Text(
                        text = "Pot: ${gameViewModel.pot} €",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Column {
                        Text(
                            text = "${gameViewModel.player1Bet} €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.Top),
                            fontSize = 15.sp,
                        )

                        if (gameViewModel.tableCards.isNotEmpty()) {
                            Row(modifier = Modifier.weight(0.6f)) {
                                TableCards(Modifier.padding(all = 5.dp), gameViewModel.tableCards)
                            }
                        }

                        Text(
                            text = "75 €",
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
                        BigBlindButton(text = "2 BB")
                        BigBlindButton(text = "Pot")
                        BigBlindButton(text = "Max")
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
                        DealerChipImage()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    if (gameViewModel.player2Cards.isNotEmpty()) {
                        PlayerCards(gameViewModel.player2Cards)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.4f)
                        ) {
                            BetSlider()
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .weight(0.6f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            MainBetButton("Fold") { gameViewModel.dealFlop() }
                            MainBetButton("Call") { gameViewModel.dealTurn() }
                            MainBetButton("Bet") { gameViewModel.dealRiver() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerCards(cards: MutableList<Card>) {
    Box {
        CardImage(
            card = cards.first(),
            Modifier
                .rotate(-5f)
                .padding(top = 5.dp, bottom = 5.dp, end = 15.dp)
                .align(Alignment.TopCenter)
        )
        CardImage(
            card = cards.last(),
            Modifier
                .zIndex(2f)
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                .rotate(5f)
                .align(Alignment.TopCenter)
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
                text = "Filipe Bicho",
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
                text = "1500 €",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TableCards(modifier: Modifier, tableCards: MutableList<Card>) {
    for (card in tableCards) {
        CardImage(card = card, modifier)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun CardImage(card: Card, modifier: Modifier) {
    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(
        card.getCardImagePath(),
        "drawable",
        context.packageName
    )

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
private fun BetSlider() {
    var betValue by remember { mutableStateOf(0) }

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
                onValueChange = { betValue = it.toInt() },
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
                modifier = Modifier.padding(end = 10.dp),
                valueRange = 0f..1500f,
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
private fun MainBetButton(text: String, onClick: () -> Unit) {

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
            onClick = onClick ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )
        {
            Text(text = text, fontSize = 15.sp)
        }
    }
}

@Composable
private fun BigBlindButton(text: String) {

    OutlinedButton(
        onClick = {},
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
