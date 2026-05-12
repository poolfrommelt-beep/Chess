package com.grandmasteredge.multiplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grandmasteredge.multiplayer.viewmodel.MultiplayerViewModel
import com.grandmasteredge.ui.components.Chessboard

@Composable
fun MultiplayerScreen(
    viewModel: MultiplayerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Local Multiplayer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Chessboard(
            board = uiState.gameState.board,
            onMove = { viewModel.onMove(it) },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        )

        Text(
            text = uiState.feedback,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { viewModel.resetGame() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Game")
        }
    }
}
