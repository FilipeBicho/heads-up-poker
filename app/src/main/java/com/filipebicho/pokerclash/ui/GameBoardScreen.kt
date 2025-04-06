package com.filipebicho.pokerclash.ui

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R

@Composable
fun GameBoardScreen(gameViewModel: GameViewModel) {
    Background()

    Column(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier.fillMaxWidth()
            .weight(0.12f)
            .border(1.dp, Color.White)) {

        }


        Row(modifier = Modifier.fillMaxWidth()
            .weight(0.58f)
            .border(1.dp, Color.White)) {

        }

        Row(modifier = Modifier.fillMaxWidth()
            .weight(0.25f)
            .fillMaxWidth()
            .border(1.dp, Color.White)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth().weight(0.6f)) {

                }
                Row(modifier = Modifier.fillMaxWidth()
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(10.dp)) {
                    ActionButton("Fold", {}, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                    ActionButton("Call", {}, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                    ActionButton("Bet", {}, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                }
            }

        }

    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                2.dp,
                colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .background(colorResource(id = R.color.button_red))
    )
    {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.align(Alignment.Center)
        )
        {
            Text(text = text, fontSize = 15.sp)
        }
    }
}

@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(
        gameViewModel = GameViewModel()
    )
}