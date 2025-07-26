package com.filipebicho.pokerclash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.filipebicho.pokerclash.R
import com.filipebicho.pokerclash.data.Data.botOptions
import com.filipebicho.pokerclash.data.Data.botWins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotSelectionScreen(
    onBotSelected: (Pair<String, String>, index: Int) -> Unit,
) {
    // Scaffold provides structure for typical Material Design screens
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Opponent") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.poker_red),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        BotSelectionBackground(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                itemsIndexed(botOptions) { index, bot ->
                    BotOptionCard(
                        botName = bot.first,
                        botDescription = bot.second,
                        wins = botWins[index],
                        onBotSelected = { onBotSelected(bot, index) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotOptionCard(
    botName: String,
    botDescription: String,
    wins: Int,
    onBotSelected: () -> Unit
) {
    Card(
        onClick = onBotSelected,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.dark_background)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "$botName icon",
                    modifier = Modifier.size(48.dp),
                    tint = colorResource(id = R.color.poker_red)
                )

                Column {
                    Text(
                        text = botName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.off_white)
                    )
                    if (botDescription.isNotBlank()) {
                        Text(
                            text = botDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )
                    }
                    Text(
                        text = "Wins: $wins",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BotSelectionScreenPreview() {
    BotSelectionScreen(onBotSelected = { bot, index ->
        // Handle the bot and index in your preview if needed
        println("Preview: Selected ${bot.first} at index $index")
    })
}

@Composable
fun BotSelectionBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.intro_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray.copy(alpha = 0.85f))
        )
        content()
    }
}
