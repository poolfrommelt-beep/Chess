package com.grandmasteredge.multiplayer.viewmodel

import androidx.lifecycle.ViewModel
import com.grandmasteredge.core.logic.Board
import com.grandmasteredge.core.logic.FenParser
import com.grandmasteredge.core.logic.GameState
import com.grandmasteredge.core.model.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MultiplayerUiState(
    val gameState: GameState = FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
    val feedback: String = "White to move"
)

class MultiplayerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MultiplayerUiState())
    val uiState: StateFlow<MultiplayerUiState> = _uiState.asStateFlow()

    fun onMove(move: Move) {
        val currentState = _uiState.value
        val piece = currentState.gameState.board.getPiece(move.from)

        // Turn enforcement: only allow moving the piece of the current turn's color
        if (piece?.color != currentState.gameState.turn) {
            _uiState.update { it.copy(feedback = "Not your turn! ${currentState.gameState.turn.name.lowercase().replaceFirstChar { c -> c.uppercase() }} to move") }
            return
        }

        val nextBoard = currentState.gameState.board.movePiece(move)

        if (nextBoard != currentState.gameState.board) {
            val nextTurn = currentState.gameState.turn.opposite()
            val nextState = currentState.gameState.copy(
                board = nextBoard,
                turn = nextTurn
            )

            _uiState.update {
                it.copy(
                    gameState = nextState,
                    feedback = "${nextTurn.name.lowercase().replaceFirstChar { c -> c.uppercase() }} to move"
                )
            }
        }
    }

    fun resetGame() {
        _uiState.value = MultiplayerUiState()
    }
}
