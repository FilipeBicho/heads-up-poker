package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.filipebicho.pokerclash.GameUiState
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card

@Composable
fun BotSection(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxHeight()) {
        InfoSection(modifier = Modifier.weight(0.33f))
        BotCards(gameUiState, modifier = Modifier.weight(0.33f))
        WinCountSection(modifier = Modifier.weight(0.33f))
    }
}

@Composable
fun InfoSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Information Section", fontSize = 10.sp, color = Color.White)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            DealerChipImage()
        }
    }
}

@Composable
private fun DealerChipImage() {
    Image(
        painter = painterResource(id = R.drawable.dealer),
        contentDescription = "Dealer chip image",
        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp).size(20.dp)
    )
}

@Composable
fun WinCountSection(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Winner count", fontSize = 12.sp, color = Color.White)
    }
}

@Composable
private fun BotCards(gameUiState: GameUiState, modifier: Modifier = Modifier) {
    Box (modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(0.dp, 0.dp, 4.dp, 4.dp),
        contentAlignment = Alignment.BottomCenter){
        Cards(gameUiState, modifier)
        BotInfo()
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
        if (gameUiState.botCards.isNotEmpty()) {
            card1 = gameUiState.botCards[0]
            card2 = gameUiState.botCards[1]
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
private fun BotInfo() {
    Column(
        modifier = Modifier
            .zIndex(4f)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Bot1",
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
            text = "300",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
            modifier = Modifier.background(Color.Black,RoundedCornerShape(5.dp)).fillMaxWidth()
        )
    }
}

