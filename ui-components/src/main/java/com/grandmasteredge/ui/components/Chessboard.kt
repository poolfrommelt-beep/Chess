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
                                            val targetFile = (file + (dragOffset.x / squareSize.toPx() + if (dragOffset.x > 0) 0.5f else -0.5f)).toInt().coerceIn(0, 7)
                                            val targetRank = (rank - (dragOffset.y / squareSize.toPx() + if (dragOffset.y > 0) 0.5f else -0.5f)).toInt().coerceIn(0, 7)
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
    val textMeasurer = rememberTextMeasurer()
    val symbol = when (piece.type) {
        com.grandmasteredge.core.model.PieceType.KING -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♔" else "♚"
        com.grandmasteredge.core.model.PieceType.QUEEN -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♕" else "♛"
        com.grandmasteredge.core.model.PieceType.ROOK -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♖" else "♜"
        com.grandmasteredge.core.model.PieceType.BISHOP -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♗" else "♝"
        com.grandmasteredge.core.model.PieceType.KNIGHT -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♘" else "♞"
        com.grandmasteredge.core.model.PieceType.PAWN -> if (piece.color == com.grandmasteredge.core.model.Color.WHITE) "♙" else "♟"
    }

    Canvas(modifier = modifier.padding(4.dp)) {
        val fontSize = size.minDimension.toSp() * 0.8f
        val textLayoutResult = textMeasurer.measure(
            text = symbol,
            style = TextStyle(
                fontSize = fontSize,
                color = if (piece.color == com.grandmasteredge.core.model.Color.WHITE) ComposeColor.White else ComposeColor.Black
            )
        )

        // Draw a subtle shadow/outline for white pieces on light squares
        if (piece.color == com.grandmasteredge.core.model.Color.WHITE) {
            drawText(
                textMeasurer = textMeasurer,
                text = symbol,
                topLeft = Offset(
                    (size.width - textLayoutResult.size.width) / 2 + 1.dp.toPx(),
                    (size.height - textLayoutResult.size.height) / 2 + 1.dp.toPx()
                ),
                style = TextStyle(fontSize = fontSize, color = ComposeColor.Black.copy(alpha = 0.5f))
            )
        }

        drawText(
            textMeasurer = textMeasurer,
            text = symbol,
            topLeft = Offset(
                (size.width - textLayoutResult.size.width) / 2,
                (size.height - textLayoutResult.size.height) / 2
            ),
            style = TextStyle(
                fontSize = fontSize,
                color = if (piece.color == com.grandmasteredge.core.model.Color.WHITE) ComposeColor.White else ComposeColor.Black
            )
        )
    }
}
