package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.cards.Card

@Composable
fun BotSection() {
    Row {
        InformationSection(modifier = Modifier.weight(0.33f))
        BotSection(modifier = Modifier.weight(0.33f))
        WinCountSection(modifier = Modifier.weight(0.33f))
    }
}

@Composable
fun InformationSection(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Information Section", fontSize = 10.sp, color = Color.White)
    }
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
private fun BotSection(modifier: Modifier = Modifier) {
    Box (modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(0.dp, 20.dp, 0.dp, 0.dp),
        contentAlignment = Alignment.BottomCenter){
        Cards(modifier)
        BotInfo()
    }

}

@Composable
private fun Cards(modifier: Modifier) {
    Row(
        modifier.fillMaxWidth().zIndex(2f).fillMaxHeight(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(modifier.fillMaxWidth().weight(.5f)) {
            CardImage(Card(12,3))
        }
        Box(modifier.fillMaxWidth().weight(.5f)) {
            CardImage(Card(11,2))
        }
    }
}

@Composable
private fun CardImage(card: Card) {
    val context = LocalContext.current
    val imageId = R.drawable.card_back

    Image(
        painter = painterResource(id = imageId),
        contentScale = ContentScale.FillWidth,
        contentDescription = "card",
    )
}

@Composable
private fun BotInfo() {
    Column(
        modifier = Modifier
            .zIndex(3f)
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(2.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Bot",
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
            text = "300",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
        )
    }
}

