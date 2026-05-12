package com.grandmasteredge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun EvaluationBar(
    evaluation: Double, // + for white, - for black
    modifier: Modifier = Modifier
) {
    val displayEval = String.format("%.1f", abs(evaluation))
    val isWhiteLeading = evaluation >= 0

    // Normalize eval for the bar: 0.0 is even, 5.0 is max height
    val weight = (abs(evaluation).coerceAtMost(5.0) / 5.0).toFloat()

    Column(
        modifier = modifier
            .width(30.dp)
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        if (isWhiteLeading) {
            Spacer(modifier = Modifier.weight(1f - (weight * 0.5f)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight * 0.5f + 0.5f)
                    .background(Color.White),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = displayEval,
                    color = Color.Black,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight * 0.5f + 0.5f)
                    .background(Color.Black),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = displayEval,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f - (weight * 0.5f)))
        }
    }
}
