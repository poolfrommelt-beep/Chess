package com.grandmasteredge.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToSinglePlayer: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    onNavigateToTactics: () -> Unit,
    onNavigateToKids: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Grandmaster Edge",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        MenuButton(
            text = "Play vs CPU",
            icon = "🤖",
            onClick = onNavigateToSinglePlayer
        )

        Spacer(modifier = Modifier.height(16.dp))

        MenuButton(
            text = "Local Multiplayer",
            icon = "👥",
            onClick = onNavigateToMultiplayer
        )

        Spacer(modifier = Modifier.height(16.dp))

        MenuButton(
            text = "Tactics Training",
            icon = "🧩",
            onClick = onNavigateToTactics
        )

        Spacer(modifier = Modifier.height(16.dp))

        MenuButton(
            text = "Kids Mode",
            icon = "👶",
            onClick = onNavigateToKids
        )
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = icon, fontSize = 24.sp, modifier = Modifier.padding(end = 16.dp))
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}
