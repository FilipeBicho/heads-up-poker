package com.filipebicho.pokerclash

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

private fun hideStatusBar(window: Window) {

    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}