package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipebicho.pokerclash.Background
import com.filipebicho.pokerclash.GameViewModel
import com.filipebicho.pokerclash.R

@Composable
fun GameBoardScreen(gameViewModel: GameViewModel) {
    Background()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxSize(),
    ) {
        // Top Row (12% of screen height)
        TopRow(modifier = Modifier.weight(0.12f))

        // Middle Row (58% of screen height)
        MiddleRow(modifier = Modifier.weight(0.58f))

        // Bottom Row (25% of screen height)
        BottomRow(modifier = Modifier.weight(0.25f))
    }
}

@Composable
fun TopRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White)
            .padding(8.dp), // Added padding inside the row
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        // TODO: Add content for the top row
        Text(text = "Top Row", color = Color.White)
    }
}

@Composable
fun MiddleRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White)
            .padding(8.dp), // Added padding inside the row
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Add content for the middle row
        Text(text = "Middle Row", color = Color.White)
    }
}

@Composable
fun BottomRow(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Top part of BottomRow (60% of BottomRow height) - Consider adding content here
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f), // Padding for the top part
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Add content for the top part of the bottom row
            Text(text = "Bottom Row - Top", color = Color.White)
        }
        // Bottom part of BottomRow (40% of BottomRow height) - Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between buttons
        ) {
            ActionButton(
                "Fold",
                {},
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                "Call",
                {},
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                "Bet",
                {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.button_red))
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.border_gray),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(12.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Preview
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(
        gameViewModel = GameViewModel()
    )
}