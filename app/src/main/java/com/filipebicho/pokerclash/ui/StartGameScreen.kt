package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.StartGameBackground
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextGeometricTransform

@Composable
fun StartGameScreen(onStartButtonClicked: (String) -> Unit) {
    StartGameBackground()

    var playerName by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(0.dp, 50.dp)
    ) {
        GameTitle()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        PlayerNameTextField(playerName = playerName, onPlayerNameChanged = { playerName = it })

        Spacer(modifier = Modifier.height(16.dp))

        if (playerName.isNotEmpty())
            StartButton(playerName = playerName, onStartButtonClicked = onStartButtonClicked)
    }
}

@Composable
fun GameTitle() {
    Text(
        text = "AI Poker Clash",
        color = Color.White,
        fontSize = 40.sp,
        fontFamily = FontFamily.Serif,
        style = TextStyle(
            shadow = Shadow(color = Color.Red, offset = Offset(5.0f, 10.0f) , blurRadius = 3f)
        ),
    )
}

@Composable
fun PlayerNameTextField(playerName: String, onPlayerNameChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedPlaceholderColor = colorResource(id = R.color.teal_700),
            unfocusedLabelColor = colorResource(id = R.color.border_gray),
            focusedLabelColor = colorResource(id = R.color.teal_700),
            focusedBorderColor = colorResource(id = R.color.teal_700),
            unfocusedBorderColor = colorResource(id = R.color.border_gray)
        ),
        value = playerName,
        onValueChange = onPlayerNameChanged,
        label = { Text("Player Name") },
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        placeholder = { Text("Enter your name") }
    )
}

@Composable
fun StartButton(playerName: String, onStartButtonClicked: (String) -> Unit) {
    Button(
        onClick = { onStartButtonClicked(playerName) },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                2.dp,
                colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            )
            .height(48.dp)
            .width(200.dp)
            .background(colorResource(id = R.color.button_red)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "Start",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun StartGameScreenPreview() {
    StartGameScreen(onStartButtonClicked = {})
}