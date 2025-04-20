package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.data.Data.minPlayerBet
import com.filipebicho.pokerclash.ui.GameBoardScreen
import kotlin.math.roundToInt

@Composable
fun PlayerSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().border(1.dp, Color.White)) {
        // Top part of BottomRow (60% of BottomRow height)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoSection(modifier = Modifier.weight(0.35f))
            SliderSection(modifier = Modifier.weight(0.65f))
        }
        ButtonSection(modifier = Modifier.weight(0.3f))
    }
}

@Composable
private fun InfoSection(modifier: Modifier = Modifier) {
    Box (modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(0.dp, 0.dp, 4.dp, 4.dp
        ),
        contentAlignment = Alignment.BottomCenter){
        Cards(modifier)
        PlayerInfo(modifier)
    }

}

@Composable
private fun PlayerInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .zIndex(3f)
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(2.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Filipe",
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
        )

        HorizontalDivider(
            color = Color.White,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Text(
            text = "1300",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
        )
    }
}


@Composable
private fun Cards(modifier: Modifier) {
    Row(
        modifier.fillMaxWidth().zIndex(2f).fillMaxHeight(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Box(modifier.fillMaxWidth().weight(.5f)) {
            CardImage(Card(1,1))
        }
        Box(modifier.fillMaxWidth().weight(.5f)) {
            CardImage(Card(1,2))
        }
    }
}

@Composable
private fun CardImage(card: Card) {
    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(
            card.getCardImagePath(),
            "drawable",
            context.packageName)

    Image(
        painter = painterResource(id = imageId),
        contentScale = ContentScale.FillWidth,
        contentDescription = "card",
    )
}

@Composable
private fun SliderSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(0.dp, 60.dp, 0.dp, 0.dp)) {
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
                        .padding(0.dp, 0.dp, 8.dp, 0.dp)
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
private fun ButtonSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            "Fold",
            {},
            modifier = Modifier.weight(1f)
        )
        Button(
            "Call",
            {},
            modifier = Modifier.weight(1f)
        )
        Button(
            "Bet",
            {},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Button(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
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