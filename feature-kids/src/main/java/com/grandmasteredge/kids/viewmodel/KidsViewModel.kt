package com.grandmasteredge.kids.viewmodel

import androidx.lifecycle.ViewModel
import com.grandmasteredge.core.logic.FenParser
import com.grandmasteredge.core.logic.GameState
import com.grandmasteredge.core.model.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class KidsUiState(
    val gameState: GameState = FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
    val feedback: String = "Let's play chess! You are White.",
    val showHints: Boolean = true
)

class KidsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KidsUiState())
    val uiState: StateFlow<KidsUiState> = _uiState.asStateFlow()

    fun onMove(move: Move) {
        val currentState = _uiState.value
        val nextBoard = currentState.gameState.board.movePiece(move)

        if (nextBoard != currentState.gameState.board) {
            val nextTurn = currentState.gameState.turn.opposite()
            _uiState.update {
                it.copy(
                    gameState = currentState.gameState.copy(board = nextBoard, turn = nextTurn),
                    feedback = "Great move! Now it's ${nextTurn.name.lowercase()}'s turn."
                )
            }
        }
    }

    fun toggleHints() {
        _uiState.update { it.copy(showHints = !it.showHints) }
    }
}
