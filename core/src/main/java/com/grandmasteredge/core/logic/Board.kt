package com.grandmasteredge.core.logic

import com.grandmasteredge.core.model.*

data class Board(
    val pieces: Map<Square, Piece> = emptyMap()
) {
    fun getPiece(square: Square): Piece? = pieces[square]

    fun movePiece(move: Move): Board {
        val newPieces = pieces.toMutableMap()
        val piece = newPieces.remove(move.from) ?: return this
        newPieces[move.to] = if (move.promotion != null) {
            Piece(move.promotion, piece.color)
        } else {
            piece
        }
        return Board(newPieces)
    }
}

data class GameState(
    val board: Board = Board(),
    val turn: Color = Color.WHITE,
    val castlingRights: String = "KQkq",
    val enPassantSquare: Square? = null,
    val halfMoveClock: Int = 0,
    val fullMoveNumber: Int = 1
)
