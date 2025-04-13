package com.filipebicho.pokerclash.ui

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.data.Data.minPlayerBet
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
    Column(modifier = modifier.fillMaxWidth().border(1.dp, Color.White)) {
        // Top part of BottomRow (60% of BottomRow height)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerInfoSection(modifier = Modifier.weight(0.3f))
            SliderSection(modifier = Modifier.weight(0.7f))
        }
        ActionButtonSection(modifier = Modifier.weight(0.3f))
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
    Column(modifier = modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)) {
        SmallBetButtonsSection(modifier)
        BetSlider(modifier)
    }
}

@Composable
private fun SmallBetButtonsSection(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom){
        SmallBetButton(
            "Min",
            {},
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            "3 BB",
            {},
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            "Pot",
            {},
            modifier = Modifier.weight(1f)
        )
        SmallBetButton(
            "Max",
            {},
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun SmallBetButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(2.dp, 0.dp),
        modifier = modifier.height(30.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.button_red),
            contentColor = Color.White
        ),
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
private fun BetSlider(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableIntStateOf(0) }

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
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.25f)
                .height(30.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center) { innerTextField() }
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
            valueRange = minPlayerBet.toFloat()..1500.toFloat(),
            modifier = Modifier.weight(0.7f)
                .height(30.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun ActionButtonSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.button_red),
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