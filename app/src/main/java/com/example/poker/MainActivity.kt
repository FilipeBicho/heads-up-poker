package com.example.poker

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.poker.ui.theme.PokerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    hideStatusBar(window)
                    Background()
                }
            }
        }
    }
}

@Composable
fun Background() {
    PokerTheme {
        val backgroundImage = painterResource(id = R.drawable.background)
            Image(
                contentScale = ContentScale.FillBounds,
                painter = backgroundImage,
                contentDescription = "Background"
            )
    }
}

@Composable
fun ResultPreview(flop: String, turn: String, river: String, result: String) {
    Column {
        Text(text = flop)
        Text(text = turn)
        Text(text = river)
        Text(text = result)
    }
}

@Composable
fun HandImage(card1: Card, card2: Card, modifier: Modifier) {
    Row {
        card1.CardImage()
        card2.CardImage()
    }
}

private fun hideStatusBar(window: Window) {

    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}