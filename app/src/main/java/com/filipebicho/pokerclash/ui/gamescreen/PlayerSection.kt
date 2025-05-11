package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
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
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.ui.GameBoardScreen
import kotlin.math.roundToInt

@Composable
fun PlayerSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerCards(gameUiState, modifier = Modifier.weight(0.35f))
            SliderSection(gameUiState, modifier = Modifier.weight(0.65f))
        }
        ButtonSection(modifier = Modifier.weight(0.3f))
    }
}

@Composable
private fun PlayerCards(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Box (modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(0.dp, 0.dp, 4.dp, 4.dp),
        contentAlignment = Alignment.BottomCenter){
        Cards(gameUiState, modifier)
        PlayerInfo(gameUiState)
        Box(modifier = Modifier.align(Alignment.TopEnd)) { DealerChipImage() }
    }
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
private fun PlayerInfo(gameUiState: GameUiState) {
    Column(
        modifier = Modifier
            .zIndex(3f)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = gameUiState.playerName,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
            modifier = Modifier.background(Color.DarkGray,RoundedCornerShape(5.dp)).fillMaxWidth()
        )

        HorizontalDivider(
            color = Color.White,
            thickness = 1.dp,
        )

        Text(
            text = gameUiState.playerMoney.toString(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
            modifier = Modifier.background(Color.Black,RoundedCornerShape(5.dp)).fillMaxWidth()
        )
    }
}


@Composable
private fun Cards(gameUiState: GameUiState, modifier: Modifier) {
    Row(
        modifier.fillMaxWidth().zIndex(2f).fillMaxHeight().padding(0.dp, 0.dp, 0.dp, 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        var card1: Card? = null
        var card2: Card? = null
        if (gameUiState.playerCards.isNotEmpty()) {
            card1 = gameUiState.playerCards[0]
            card2 = gameUiState.playerCards[1]
        }

        Box(modifier.fillMaxWidth().weight(.5f)) { CardImage(card1) }
        Box(modifier.fillMaxWidth().weight(.5f)) { CardImage(card2) }
    }
}

@Composable
private fun CardImage(card: Card?) {
    Image(
        painter = painterResource(id = card?.getCardDrawableResource() ?: R.drawable.card_back),
        contentScale = ContentScale.Fit,
        contentDescription = "card",
    )
}

@Composable
private fun SliderSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SmallBetButtonsSection(modifier)
        BetSlider(gameUiState, modifier)
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
private fun BetSlider(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableIntStateOf(gameUiState.minPlayerBet) }

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
                        .background(Color.DarkGray, shape = RoundedCornerShape(5.dp)),
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
            valueRange = gameUiState.minPlayerBet.toFloat()..1500.toFloat(),
            modifier = Modifier.weight(0.7f).height(30.dp).align(Alignment.CenterVertically)
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
    GameBoardScreen(gameUiState = GameUiState())
}