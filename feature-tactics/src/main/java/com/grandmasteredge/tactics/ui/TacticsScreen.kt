package com.grandmasteredge.tactics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grandmasteredge.tactics.viewmodel.TacticsViewModel
import com.grandmasteredge.ui.components.Chessboard
import com.grandmasteredge.ui.components.EvaluationBar

@Composable
fun TacticsScreen(
    viewModel: TacticsViewModel = TacticsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tactics Riddle",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EvaluationBar(
                evaluation = uiState.evaluation,
                modifier = Modifier.fillMaxHeight()
            )

            Chessboard(
                board = uiState.gameState.board,
                onMove = { viewModel.onMove(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = uiState.feedback,
            color = if (uiState.isSolved) Color.Green else Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        uiState.hint?.let {
            Text(
                text = "Hint: $it",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.showHint() }) {
                Text("Show Hint")
            }
            Button(onClick = { /* Reset puzzle logic */ }) {
                Text("Reset")
            }
        }
    }
}
