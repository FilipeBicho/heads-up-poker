package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.StartGameBackground
import com.filipebicho.pokerclash.data.Data.botOptions

@Composable
fun BotSelectionScreen(
    onBotButtonClicked: (Pair<String, String>) -> Unit
) {
    StartGameBackground()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column {
            Text(
                text = "Select Bot AI Model",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            Spacer(modifier = Modifier.height(8.dp))
            botOptions.forEach { bot ->
                BotButton(bot = bot, onBotButtonClicked)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun BotButton(bot: Pair<String, String>, onBotButtonClicked: (Pair<String, String>) -> Unit) {
    Button(
        onClick = { onBotButtonClicked(bot) },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                2.dp,
                colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            )
            .height(40.dp)
            .width(180.dp)
            .background(colorResource(id = R.color.button_red)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = bot.first,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun BotSelectionScreenPreview() {
    BotSelectionScreen (onBotButtonClicked = {})
}