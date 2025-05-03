package com.filipebicho.pokerclash.ui.gamescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.cards.Card

@Composable
fun GameSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Pot()
        TableCards()
        Hand()
    }
}

@Composable
private fun TableCards() {
    Row (
        modifier = Modifier.padding(30.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ){
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            CardImage(Card(1,1))
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            CardImage(Card(1,2))
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            CardImage(Card(1,1))
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            CardImage(Card(1,2))
        }
        Box(modifier = Modifier.fillMaxWidth().weight(0.2f)) {
            CardImage(Card(1,1))
        }
    }
}

@Composable
fun Pot() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.background(Color.DarkGray.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp)),
            color = Color.White,
            text = "Pot: 300€"
        )
    }
}

@Composable
fun Hand() {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, ) {
        Text(
            text = "Pair of Two",
            fontSize = 12.sp,
            color = Color.White,
        )
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