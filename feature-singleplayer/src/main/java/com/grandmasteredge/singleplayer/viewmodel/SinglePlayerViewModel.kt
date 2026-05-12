package com.grandmasteredge.singleplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grandmasteredge.core.logic.Board
import com.grandmasteredge.core.logic.FenParser
import com.grandmasteredge.core.logic.GameState
import com.grandmasteredge.core.model.Color
import com.grandmasteredge.core.model.Move
import com.grandmasteredge.core.model.Square
import com.grandmasteredge.engine.stockfish.EngineService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SinglePlayerUiState(
    val gameState: GameState = FenParser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
    val feedback: String = "Your turn (White)",
    val isThinking: Boolean = false,
    val engineEval: Double = 0.0
)

class SinglePlayerViewModel : ViewModel() {
    private val engineService = EngineService()
    private val _uiState = MutableStateFlow(SinglePlayerUiState())
    val uiState: StateFlow<SinglePlayerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            engineService.evaluation.collect { eval ->
                _uiState.update { it.copy(engineEval = eval) }
            }
        }
        viewModelScope.launch {
            engineService.bestMove.collect { moveStr ->
                if (moveStr != null && _uiState.value.gameState.turn == Color.BLACK) {
                    delay(500) // Small delay for better UX
                    makeEngineMove(moveStr)
                }
            }
        }
    }

    fun onMove(move: Move) {
        val currentState = _uiState.value
        if (currentState.isThinking || currentState.gameState.turn != Color.WHITE) return

        val piece = currentState.gameState.board.getPiece(move.from)
        if (piece?.color != Color.WHITE) return

        val nextBoard = currentState.gameState.board.movePiece(move)
        if (nextBoard != currentState.gameState.board) {
            val nextState = currentState.gameState.copy(
                board = nextBoard,
                turn = Color.BLACK
            )

            _uiState.update {
                it.copy(
                    gameState = nextState,
                    feedback = "Stockfish is thinking...",
                    isThinking = true
                )
            }
            engineService.startAnalysis(FenParser.toFen(nextState))
        }
    }

    private fun makeEngineMove(moveStr: String) {
        // moveStr is like "e2e4"
        val fromSq = Square.fromString(moveStr.substring(0, 2))
        val toSq = Square.fromString(moveStr.substring(2, 4))
        val move = Move(fromSq, toSq)

        val currentState = _uiState.value
        val nextBoard = currentState.gameState.board.movePiece(move)
        val nextState = currentState.gameState.copy(
            board = nextBoard,
            turn = Color.WHITE
        )

        _uiState.update {
            it.copy(
                gameState = nextState,
                feedback = "Your turn (White)",
                isThinking = false
            )
        }
    }

    fun resetGame() {
        engineService.stopAnalysis()
        _uiState.value = SinglePlayerUiState()
    }

    override fun onCleared() {
        super.onCleared()
        engineService.stopAnalysis()
    }
}
