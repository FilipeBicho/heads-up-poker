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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.filipebicho.pokerclash.cards.BOT
import com.filipebicho.pokerclash.cards.Card
import com.filipebicho.pokerclash.cards.PLAYER
import kotlinx.coroutines.delay
import kotlin.text.isNotEmpty

@Composable
fun DealerChipImage() {
    Image(
        painter = painterResource(id = R.drawable.dealer),
        contentDescription = "Dealer chip image",
        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp).size(20.dp)
    )
}

@Composable
fun Cards(cards: List<Card>, display: Boolean, modifier: Modifier) {
    Row(
        modifier.fillMaxWidth().zIndex(2f).fillMaxHeight().padding(0.dp, 0.dp, 0.dp, 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        var card1: Card? = null
        var card2: Card? = null
        if (display && cards.isNotEmpty()) {
            card1 = cards[0]
            card2 = cards[1]
        }

        Box(modifier.fillMaxWidth().weight(.5f)) { CardImage(card1) }
        Box(modifier.fillMaxWidth().weight(.5f)) { CardImage(card2) }
    }
}

@Composable
fun CardImage(card: Card?) {
    Image(
        painter = painterResource(id = card?.getCardDrawableResource() ?: R.drawable.card_back),
        contentScale = ContentScale.Fit,
        contentDescription = "card",
    )
}

@Composable
fun NameAndMoneySection(name: String, action: String, money: Int) {
    var currentDisplayName by remember { mutableStateOf(name) }
    var showingActionText by remember { mutableStateOf(false) }

    LaunchedEffect(action) {
        if (action.isNotEmpty()) {
            currentDisplayName = action
            showingActionText = true
            delay(1500L) // Wait for 1.5 seconds
            if (showingActionText) {
                currentDisplayName = name
                showingActionText = false
            }
        } else {
            currentDisplayName = name
            showingActionText = false
        }
    }

    LaunchedEffect(name) {
        if (!showingActionText) {
            currentDisplayName = name
        }
    }
    Column(
        modifier = Modifier
            .zIndex(4f)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = currentDisplayName,
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
            text = money.toString(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            lineHeight = 1.5.em,
            modifier = Modifier.background(Color.Black,RoundedCornerShape(5.dp)).fillMaxWidth()
        )
    }
}