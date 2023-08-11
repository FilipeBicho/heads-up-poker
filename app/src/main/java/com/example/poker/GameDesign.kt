package com.example.poker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(game: Game) {
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
                        .weight(0.4f)
                        .border(1.dp, Color.White)
                ) {
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                        DealerChipImage()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .border(1.dp, Color.White)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    PlayerCards(game.player1Cards)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .border(1.dp, Color.White)
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
                        .border(1.dp, Color.White)
                ) {

                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .border(1.dp, Color.White)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Column {
                        Text(
                            text = "Pot: ${game.pot} €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.Bottom),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.End
                        )

                        Row(modifier = Modifier.weight(0.6f)) {
                            TableCards(Modifier.padding(all = 5.dp), game.tableCards)
                        }

                        Text(
                            text = "${game.bet} €",
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.CenterHorizontally),
                            fontSize = 15.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                        .border(1.dp, Color.White)
                ) {

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
                        .weight(0.4f)
                        .border(1.dp, Color.White)
                ) {
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        DealerChipImage()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .border(1.dp, Color.White)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    PlayerCards(game.player2Cards)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .border(1.dp, Color.White)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                        ) {
                            Column {
                                var sliderPosition by remember { mutableStateOf(0f) }
                                Text(
                                    text = sliderPosition.roundToInt().toString()
                                )
                                Slider(
                                    value = sliderPosition,
                                    valueRange = 0f..1500f,
                                    onValueChange = { sliderPosition = it },

                                    )
                            }

                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .weight(0.5f),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Button("Fold")
                            Button("Call")
                            Button("Bet")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun PlayerCards(cards: ArrayList<Card>) {
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
fun TableCards(modifier: Modifier, tableCards: ArrayList<Card>) {
    for (card in tableCards) {
        CardImage(card = card, modifier)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CardImage(card: Card, modifier: Modifier) {
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
private fun Button(text: String) {

    val brush = Brush.linearGradient(
        listOf(colorResource(id = R.color.dark_red), Color.Black),
        start = Offset(0.0f, 50.0f),
        end = Offset(0.0f, 100.0f)
    )

    val context = LocalContext.current.applicationContext

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                2.dp,
                colorResource(id = R.color.custom_gray),
                shape = RoundedCornerShape(10.dp)
            )
            .height(40.dp)
            .width(80.dp)
            .background(brush)
    )
    {
        Button(
            onClick = { /*TODO*/ },
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
