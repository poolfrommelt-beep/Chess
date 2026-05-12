package com.grandmasteredge.core.logic

import com.grandmasteredge.core.model.*

object FenParser {
    fun parse(fen: String): GameState {
        val parts = fen.split(" ")
        if (parts.size < 1) throw IllegalArgumentException("Invalid FEN")

        val board = parseBoard(parts[0])
        val turn = if (parts.getOrNull(1) == "b") Color.BLACK else Color.WHITE
        val castling = parts.getOrNull(2) ?: "-"
        val enPassant = parts.getOrNull(3)?.let { if (it == "-") null else Square.fromString(it) }
        val halfMove = parts.getOrNull(4)?.toIntOrNull() ?: 0
        val fullMove = parts.getOrNull(5)?.toIntOrNull() ?: 1

        return GameState(board, turn, castling, enPassant, halfMove, fullMove)
    }

    private fun parseBoard(boardFen: String): Board {
        val pieces = mutableMapOf<Square, Piece>()
        val ranks = boardFen.split("/")
        for (r in 0 until 8) {
            val rankStr = ranks[r]
            var file = 0
            for (char in rankStr) {
                if (char.isDigit()) {
                    file += char.toString().toInt()
                } else {
                    val color = if (char.isUpperCase()) Color.WHITE else Color.BLACK
                    val type = when (char.lowercaseChar()) {
                        'p' -> PieceType.PAWN
                        'n' -> PieceType.KNIGHT
                        'b' -> PieceType.BISHOP
                        'r' -> PieceType.ROOK
                        'q' -> PieceType.QUEEN
                        'k' -> PieceType.KING
                        else -> throw IllegalArgumentException("Unknown piece: $char")
                    }
                    pieces[Square(file, 7 - r)] = Piece(type, color)
                    file++
                }
            }
        }
        return Board(pieces)
    }

    fun toFen(state: GameState): String {
        val sb = StringBuilder()
        // Board
        for (rank in 7 downTo 0) {
            var emptyCount = 0
            for (file in 0..7) {
                val piece = state.board.getPiece(Square(file, rank))
                if (piece == null) {
                    emptyCount++
                } else {
                    if (emptyCount > 0) {
                        sb.append(emptyCount)
                        emptyCount = 0
                    }
                    sb.append(piece.getSymbol())
                }
            }
            if (emptyCount > 0) sb.append(emptyCount)
            if (rank > 0) sb.append("/")
        }

        sb.append(" ")
        sb.append(if (state.turn == Color.WHITE) "w" else "b")
        sb.append(" ")
        sb.append(state.castlingRights)
        sb.append(" ")
        sb.append(state.enPassantSquare?.toString() ?: "-")
        sb.append(" ")
        sb.append(state.halfMoveClock)
        sb.append(" ")
        sb.append(state.fullMoveNumber)

        return sb.toString()
    }
}
