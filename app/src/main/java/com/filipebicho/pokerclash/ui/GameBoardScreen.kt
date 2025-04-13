package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.core.text.isDigitsOnly
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.PLAYER
import com.filipebicho.pokerclash.data.Data.minPlayerBet
import com.filipebicho.pokerclash.data.Data.pokerChips
import kotlin.math.roundToInt

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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White)
            .padding(8.dp), // Added padding inside the row
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Add content for the middle row
        Text(text = "Middle Row", color = Color.White)
    }
}

@Composable
fun BottomRow(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Top part of BottomRow (60% of BottomRow height)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerInfoSection(modifier = Modifier.weight(0.3f))
            SliderSection(modifier = Modifier.weight(0.6f))
        }
        ActionButtonSection(modifier = Modifier.weight(0.7f))
    }
}

@Composable
private fun PlayerInfoSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight() ){

    }
}

@Composable
private fun SliderSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().fillMaxHeight())
        {
            BetSlider()
        }
}

@Composable
private fun BetSlider() {
    var sliderPosition by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .background(
                Color.DarkGray.copy(alpha = 0.9f),
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        Box(modifier = Modifier.weight(0.2f)) {
            BasicTextField(
                value = sliderPosition.toString(),
                onValueChange = {
                    if (it.isNotEmpty() && it.isDigitsOnly()) {
                        sliderPosition = it.toInt()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
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
                value = sliderPosition.toFloat(),
                onValueChange = { sliderPosition = it.roundToInt() },
                onValueChangeFinished = {  },
                modifier = Modifier.padding(end = 10.dp),
                valueRange = minPlayerBet.toFloat()..1500.toFloat(),
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
private fun ActionButtonSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton(
            "Fold",
            {},
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            "Call",
            {},
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            "Bet",
            {},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.button_red))
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(12.dp)
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

@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(
        gameViewModel = GameViewModel()
    )
}