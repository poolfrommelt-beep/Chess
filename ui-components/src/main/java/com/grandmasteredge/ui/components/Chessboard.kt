package com.grandmasteredge.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grandmasteredge.core.logic.Board
import com.grandmasteredge.core.model.Move
import com.grandmasteredge.core.model.Piece
import com.grandmasteredge.core.model.Square
import kotlin.math.floor

@Composable
fun Chessboard(
    board: Board,
    onMove: (Move) -> Unit,
    modifier: Modifier = Modifier
) {
    var draggedSquare by remember { mutableStateOf<Square?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier.aspectRatio(1f)) {
        val squareSize = maxWidth / 8

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw coordinate labels
            val textStyle = TextStyle(fontSize = 10.sp, color = ComposeColor.Gray)
            for (i in 0..7) {
                // Files a-h
                drawText(
                    textMeasurer = textMeasurer,
                    text = ('a' + i).toString(),
                    topLeft = Offset(i * squareSize.toPx() + 2.dp.toPx(), size.height - 14.dp.toPx()),
                    style = textStyle
                )
                // Ranks 1-8
                drawText(
                    textMeasurer = textMeasurer,
                    text = (i + 1).toString(),
                    topLeft = Offset(size.width - 12.dp.toPx(), (7 - i) * squareSize.toPx() + 2.dp.toPx()),
                    style = textStyle
                )
            }
        }

        Column {
            for (rank in 7 downTo 0) {
                Row {
                    for (file in 0..7) {
                        val square = Square(file, rank)
                        val isDark = (file + rank) % 2 == 0
                        val bgColor = if (isDark) ComposeColor(0xFF769656) else ComposeColor(0xFFeeeed2)

                        Box(
                            modifier = Modifier
                                .size(squareSize)
                                .background(bgColor)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            draggedSquare = square
                                            dragOffset = Offset.Zero
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                        },
                                        onDragEnd = {
                                            val targetFile = (file + dragOffset.x / squareSize.toPx()).toInt().coerceIn(0, 7)
                                            val targetRank = (rank - dragOffset.y / squareSize.toPx()).toInt().coerceIn(0, 7)
                                            val targetSquare = Square(targetFile, targetRank)
                                            if (targetSquare != square) {
                                                onMove(Move(square, targetSquare))
                                            }
                                            draggedSquare = null
                                            dragOffset = Offset.Zero
                                        },
                                        onDragCancel = {
                                            draggedSquare = null
                                            dragOffset = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            val piece = board.getPiece(square)
                            if (piece != null && draggedSquare != square) {
                                PieceIcon(piece = piece, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }

        // Render dragged piece
        draggedSquare?.let { sq ->
            board.getPiece(sq)?.let { piece ->
                val initialX = sq.file * squareSize.value
                val initialY = (7 - sq.rank) * squareSize.value

                PieceIcon(
                    piece = piece,
                    modifier = Modifier
                        .size(squareSize)
                        .offset(
                            x = initialX.dp + with(density) { dragOffset.x.toDp() },
                            y = initialY.dp + with(density) { dragOffset.y.toDp() }
                        )
                )
            }
        }
    }
}

@Composable
fun PieceIcon(piece: Piece, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.padding(8.dp)) {
        val color = if (piece.color == com.grandmasteredge.core.model.Color.WHITE) ComposeColor.White else ComposeColor.Black
        val outlineColor = if (piece.color == com.grandmasteredge.core.model.Color.WHITE) ComposeColor.Black else ComposeColor.White

        when (piece.type) {
            com.grandmasteredge.core.model.PieceType.PAWN -> {
                drawCircle(color = color, radius = size.minDimension / 4, center = Offset(size.width/2, size.height/3))
                drawRect(color = color, topLeft = Offset(size.width/3, size.height/2), size = Size(size.width/3, size.height/3))
            }
            com.grandmasteredge.core.model.PieceType.KING -> {
                drawRect(color = color, topLeft = Offset(size.width/4, size.height/4), size = Size(size.width/2, size.height/2))
                drawLine(color = outlineColor, start = Offset(size.width/2, size.height/4), end = Offset(size.width/2, size.height*0.75f), strokeWidth = 2.dp.toPx())
                drawLine(color = outlineColor, start = Offset(size.width/4, size.height/2), end = Offset(size.width*0.75f, size.height/2), strokeWidth = 2.dp.toPx())
            }
            else -> {
                // Generic shape for other pieces
                drawCircle(color = color, radius = size.minDimension / 3)
            }
        }
        // Draw outline
        drawCircle(color = outlineColor, radius = size.minDimension / 3, style = Stroke(width = 1.dp.toPx()))
    }
}
