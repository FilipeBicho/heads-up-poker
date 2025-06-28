package com.filipebicho.pokerclash.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt

@Composable
fun StartGameScreen(onStartButtonClicked: (String) -> Unit) {
    var playerName by remember { mutableStateOf("") }

    StartGameBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
            ) {
                AnimatedPokerTitle()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom section: Input and Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 32.dp, end = 32.dp)
            ) {
                PlayerNameTextField(playerName = playerName, onPlayerNameChanged = { playerName = it })
                Spacer(modifier = Modifier.height(24.dp))
                if (playerName.isNotEmpty()) {
                    StartButton(playerName = playerName, onStartButtonClicked = onStartButtonClicked)
                } else {
                    Spacer(modifier = Modifier.height(52.dp))
                }
            }
        }
    }
}

@Composable
fun AnimatedPokerTitle(modifier: Modifier = Modifier) { // Added modifier for flexibility, though not used in current StartGameScreen directly
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, delayMillis = 0)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        )
    }

    Text(
        text = "AI Poker Clash",
        style = TextStyle(
            color =colorResource(id = R.color.poker_red),
            fontSize = 44.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            shadow = Shadow(
                color = Color.White.copy(alpha = 0.5f),
                offset = Offset(3.0f, 3.0f),
                blurRadius = 5f
            ),
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .scale(scale.value)
            .alpha(alpha.value)
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
            unfocusedBorderColor = colorResource(id = R.color.border_gray),
            unfocusedContainerColor = Color.LightGray,
            focusedContainerColor = Color.DarkGray
        ),
        value = playerName,
        onValueChange = onPlayerNameChanged,
        label = { Text("Insert your name...") },
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        placeholder = { Text("Insert your name...") }
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
            .background(colorResource(id = R.color.poker_red)),
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

@Composable
fun StartGameBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) { // Use a Box to layer the image and content
        Image(
            painter = painterResource(id = R.drawable.intro_background), // Replace with your image name
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)) // Adjust alpha for desired dimness
            // 0.0f = fully transparent, 1.0f = fully opaque black
            // 0.5f to 0.7f is often a good range
        )
        // The actual screen content is placed on top of the image
        content()
    }
}