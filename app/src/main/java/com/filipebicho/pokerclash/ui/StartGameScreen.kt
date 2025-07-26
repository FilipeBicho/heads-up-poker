package com.filipebicho.pokerclash.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.R
import kotlinx.coroutines.delay

@Composable
fun StartGameScreen(onStartButtonClicked: (String) -> Unit) {
    var playerName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    StartGameBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.weight(0.3f))
                Title()
                Spacer(modifier = Modifier.height(32.dp))
                Logo()
                Spacer(modifier = Modifier.height(40.dp))
                PlayerNameTextField(
                    playerName = playerName,
                    onPlayerNameChanged = { playerName = it }
                )
                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(
                    visible = playerName.isNotBlank(),
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                ) {
                    StartButton(
                        playerName = playerName,
                        onStartButtonClicked = {
                            focusManager.clearFocus()
                            onStartButtonClicked(it)
                        },
                    )
                }

                if (playerName.isBlank()) {
                    Spacer(modifier = Modifier.height(56.dp + 24.dp))
                }

                Spacer(Modifier.weight(0.7f))
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.scale(scale.value)
    ) {
        Text(
            text = "AI",
            style = TextStyle(
                color = colorResource(id = R.color.poker_red),
                fontSize = 100.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
        )
        Text(
            text = "Poker Clash",
            style = TextStyle(
                color = colorResource(id = R.color.off_white),
                fontSize = 50.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
        )
    }
}

@Composable
fun Logo() {
    val infiniteTransition = rememberInfiniteTransition(label = "PulsingLogoTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        ), label = "PulsingLogoScale"
    )

    Image(
        painter = painterResource(R.drawable.logo),
        contentScale = ContentScale.Fit,
        contentDescription = "Poker Clash Logo",
        modifier = Modifier
            .size(120.dp)
            .scale(scale)
    )
}

@Composable
fun PlayerNameTextField(playerName: String, onPlayerNameChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .width(250.dp)
            .height(65.dp),
        value = playerName,
        onValueChange = onPlayerNameChanged,
        placeholder = { Text("Enter Your Nickname", color = colorResource(id = R.color.off_white).copy(alpha = 0.7f)) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = colorResource(id = R.color.off_white), fontWeight = FontWeight.SemiBold),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.poker_red),
            unfocusedBorderColor = colorResource(id = R.color.off_white),
            focusedLabelColor = colorResource(id = R.color.poker_red),
            unfocusedLabelColor = colorResource(id = R.color.off_white).copy(alpha = 0.4f),
            cursorColor = colorResource(id = R.color.poker_red),
            focusedContainerColor = colorResource(id = R.color.dark_background).copy(alpha = 0.8f),
            unfocusedContainerColor = colorResource(id = R.color.dark_background).copy(alpha = 0.5f)
        )
    )
}

@Composable
fun StartButton(playerName: String, onStartButtonClicked: (String) -> Unit) {
    val scale = remember { Animatable(1f) }

    Button(
        onClick = {
            onStartButtonClicked(playerName)
        },
        modifier = Modifier
            .width(250.dp)
            .height(56.dp)
            .scale(scale.value)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.poker_red))
            .border(
                2.dp,
                colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "START GAME",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            letterSpacing = 1.1.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun StartGameScreenPreview() {
    MaterialTheme {
        StartGameScreen(onStartButtonClicked = {})
    }
}

@Composable
fun StartGameBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val blackShade = Color.Black
    val darkGrayShade = Color.DarkGray

    // 1. Use InfiniteTransition for continuous animation
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundColorTransition")


    // 2. Animate colors using the infiniteTransition
    val gradientStartColor by infiniteTransition.animateColor(
        initialValue = blackShade,
        targetValue = darkGrayShade,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // This makes it go back and forth
        ),
        label = "GradientStartColorInfinite"
    )

    val gradientEndColor by infiniteTransition.animateColor(
        initialValue = darkGrayShade,
        targetValue = blackShade,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // This makes it go back and forth
        ),
        label = "GradientEndColorInfinite"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(gradientStartColor, gradientEndColor)
                    )
                )
        )
        content()
    }
}
