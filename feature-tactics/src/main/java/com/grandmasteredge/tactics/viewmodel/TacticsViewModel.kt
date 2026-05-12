package com.grandmasteredge.tactics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grandmasteredge.core.logic.Board
import com.grandmasteredge.core.logic.FenParser
import com.grandmasteredge.core.logic.GameState
import com.grandmasteredge.core.model.Move
import com.grandmasteredge.engine.stockfish.EngineService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TacticsUiState(
    val gameState: GameState = GameState(),
    val solution: List<String> = emptyList(),
    val currentMoveIndex: Int = 0,
    val isSolved: Boolean = false,
    val feedback: String = "",
    val evaluation: Double = 0.0,
    val hint: String? = null
)

class TacticsViewModel(
    private val engineService: EngineService = EngineService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TacticsUiState())
    val uiState: StateFlow<TacticsUiState> = _uiState.asStateFlow()

    init {
        loadPuzzle("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3", listOf("d2d4"))

        viewModelScope.launch {
            engineService.evaluation.collect { eval ->
                _uiState.update { it.copy(evaluation = eval) }
            }
        }
    }

    fun loadPuzzle(fen: String, solution: List<String>) {
        val state = FenParser.parse(fen)
        _uiState.value = TacticsUiState(gameState = state, solution = solution)
        engineService.startAnalysis(fen)
    }

    fun onMove(move: Move) {
        val currentState = _uiState.value
        if (currentState.isSolved) return

        val moveStr = move.toString()
        val expectedMove = currentState.solution.getOrNull(currentState.currentMoveIndex)

        if (moveStr == expectedMove) {
            val nextState = currentState.gameState.board.movePiece(move)
            val newGameState = currentState.gameState.copy(
                board = nextState,
                turn = currentState.gameState.turn.opposite()
            )

            val isLastMove = currentState.currentMoveIndex == currentState.solution.size - 1

            _uiState.update {
                it.copy(
                    gameState = newGameState,
                    currentMoveIndex = it.currentMoveIndex + 1,
                    isSolved = isLastMove,
                    feedback = if (isLastMove) "Correct! Puzzle solved." else "Good move! Keep going.",
                    hint = null
                )
            }

            engineService.startAnalysis(FenParser.toFen(newGameState))
        } else {
            _uiState.update { it.copy(feedback = "Incorrect move. Try again.") }
        }
    }

    fun showHint() {
        viewModelScope.launch {
            _uiState.update { it.copy(hint = engineService.bestMove.value) }
        }
    }
}
