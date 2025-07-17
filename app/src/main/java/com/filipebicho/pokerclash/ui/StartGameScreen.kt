package com.filipebicho.pokerclash.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.R // Assuming your R file is here

// Define some modern poker-themed colors (adjust as needed)
val PokerRed = Color(0xFFB71C1C)
val PokerGreen = Color(0xFF2E7D32)
val DarkBackground = Color(0xFF121212)
val SubtleGray = Color(0xFF424242)
val OffWhite = Color(0xFFF5F5F5)


@Composable
fun StartGameScreen(onStartButtonClicked: (String) -> Unit) {
    var playerName by remember { mutableStateOf("") }

    StartGameBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // Handles keyboard overlap
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {

                AnimatedPokerTitle()
                Spacer(modifier = Modifier.height(32.dp))
                PulsingLogo() // Added pulsing animation
                Spacer(modifier = Modifier.height(48.dp))
                PlayerNameTextField(
                    playerName = playerName,
                    onPlayerNameChanged = { playerName = it }
                )
                Spacer(modifier = Modifier.height(32.dp))

                // AnimatedVisibility for the button
                AnimatedVisibility(
                    visible = playerName.isNotBlank(),
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                ) {
                    StartButton(
                        playerName = playerName,
                        onStartButtonClicked = onStartButtonClicked
                    )
                }
                // Maintain consistent spacing when button is not visible
                if (playerName.isBlank()) {
                    Spacer(modifier = Modifier.height(56.dp)) // Approximate button height + padding
                }
            }
        }
    }
}

@Composable
fun PulsingLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "PulsingLogoTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = "PulsingLogoScale"
    )

    Image(
        painter = painterResource(R.drawable.logo), // Ensure you have a logo in drawable
        contentScale = ContentScale.Fit,
        contentDescription = "Poker Clash Logo",
        modifier = Modifier
            .size(160.dp) // Slightly larger
            .scale(scale) // Apply pulsing scale
            .clip(CircleShape) // Give it a circular frame
            .border(2.dp, PokerRed, CircleShape) // Gold border
    )
}

@Composable
fun AnimatedPokerTitle(modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0.5f) } // Initial scale for animation

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
        modifier = modifier.scale(scale.value) // Apply scale animation
    ) {
        Text(
            text = "AI",
            style = TextStyle(
                color = PokerRed, // Use gold for accent
                fontSize = 120.sp, // Slightly larger
                fontFamily = FontFamily.Serif, // A more classic poker font
                fontWeight = FontWeight.Bold,
                shadow = Shadow( // Add a subtle shadow
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
        )
        Text(
            text = "Poker Clash",
            style = TextStyle(
                color = OffWhite,
                fontSize = 55.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal, // Less bold for contrast
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
fun PlayerNameTextField(playerName: String, onPlayerNameChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .width(280.dp) // Wider text field
            .height(65.dp),
        value = playerName,
        onValueChange = onPlayerNameChanged,
        label = { Text("Enter Your Nickname", color = OffWhite.copy(alpha = 0.7f)) },
        placeholder = { Text("e.g., AceHigh", color = SubtleGray) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = OffWhite, fontWeight = FontWeight.SemiBold),
        shape = RoundedCornerShape(12.dp), // More rounded corners
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PokerRed,
            unfocusedBorderColor = SubtleGray,
            focusedLabelColor = PokerRed,
            unfocusedLabelColor = OffWhite.copy(alpha = 0.7f),
            cursorColor = PokerRed,
            focusedContainerColor = DarkBackground.copy(alpha = 0.5f), // Darker, slightly transparent
            unfocusedContainerColor = DarkBackground.copy(alpha = 0.3f)
        )
    )
}

@Composable
fun StartButton(playerName: String, onStartButtonClicked: (String) -> Unit) {
    val scale = remember { Animatable(1f) }

    Button(
        onClick = {
            onStartButtonClicked(playerName)
            // Optional: Add a little click animation
            // CoroutineScope(Dispatchers.Main).launch {
            //     scale.animateTo(0.95f, animationSpec = tween(50))
            //     scale.animateTo(1f, animationSpec = tween(50))
            // }
        },
        modifier = Modifier
            .width(280.dp)
            .height(56.dp)
            .scale(scale.value)
            .clip(RoundedCornerShape(12.dp)) // Consistent rounded corners
            .background(
                Brush.horizontalGradient( // Add a subtle gradient
                    colors = listOf(PokerRed, PokerRed.copy(alpha = 0.7f))
                )
            )
            .border(
                1.dp,
                PokerRed.copy(alpha = 0.5f), // Subtle gold border
                RoundedCornerShape(12.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Background is handled by the modifier
            contentColor = OffWhite
        ),
        elevation = ButtonDefaults.buttonElevation( // Add some elevation
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = "DEAL ME IN", // More thematic text
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif, // A clean sans-serif for the button
            textAlign = TextAlign.Center,
            letterSpacing = 1.1.sp // Add some letter spacing
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun StartGameScreenPreview() {
    MaterialTheme { // Wrap in MaterialTheme for consistent theming if not already done
        StartGameScreen(onStartButtonClicked = {})
    }
}

@Composable
fun StartGameBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.intro_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.5f // Make the background image more subtle
        )
        // Darker overlay for better text contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground.copy(alpha = 0.85f)) // Darker overlay
        )
        content()
    }
}
