package com.filipebicho.pokerclash

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.filipebicho.pokerclash.ui.theme.PokerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerTheme {
                hideStatusBar(window)
                PokerApp()
            }
        }
    }
}

@Composable
fun Background() {
    PokerTheme {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentScale = ContentScale.FillBounds,
            contentDescription = "Background",
        )
    }
}

private fun hideStatusBar(window: Window) {

    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}