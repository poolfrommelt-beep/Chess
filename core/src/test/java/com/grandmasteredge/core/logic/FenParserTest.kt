package com.grandmasteredge.core.logic

import com.grandmasteredge.core.model.*
import org.junit.Assert.*
import org.junit.Test

class FenParserTest {

    @Test
    fun testStartingPosition() {
        val startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        val state = FenParser.parse(startFen)

        assertEquals(Color.WHITE, state.turn)
        assertEquals("KQkq", state.castlingRights)
        assertNull(state.enPassantSquare)
        assertEquals(0, state.halfMoveClock)
        assertEquals(1, state.fullMoveNumber)

        assertEquals(Piece(PieceType.ROOK, Color.WHITE), state.board.getPiece(Square(0, 0)))
        assertEquals(Piece(PieceType.KING, Color.WHITE), state.board.getPiece(Square(4, 0)))
        assertEquals(Piece(PieceType.PAWN, Color.BLACK), state.board.getPiece(Square(0, 6)))
    }

    @Test
    fun testToFen() {
        val startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        val state = FenParser.parse(startFen)
        val generatedFen = FenParser.toFen(state)
        assertEquals(startFen, generatedFen)
    }

    @Test
    fun testMove() {
        val startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        val state = FenParser.parse(startFen)
        val move = Move(Square(4, 1), Square(4, 3)) // e2e4
        val newBoard = state.board.movePiece(move)

        assertNull(newBoard.getPiece(Square(4, 1)))
        assertEquals(Piece(PieceType.PAWN, Color.WHITE), newBoard.getPiece(Square(4, 3)))
    }
}
