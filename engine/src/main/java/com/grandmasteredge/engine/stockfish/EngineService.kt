package com.grandmasteredge.engine.stockfish

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class EngineService {
    private val bridge = StockfishBridge()
    private val _evaluation = MutableStateFlow(0.0)
    val evaluation: StateFlow<Double> = _evaluation.asStateFlow()

    private val _bestMove = MutableStateFlow<String?>(null)
    val bestMove: StateFlow<String?> = _bestMove.asStateFlow()

    fun startAnalysis(fen: String) {
        bridge.sendCommand("position fen $fen")
        bridge.sendCommand("go depth 15")

        // Simulate engine output processing
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            _evaluation.value = (Math.random() * 4 - 2)
            _bestMove.value = "e2e4"
        }
    }

    fun stopAnalysis() {
        bridge.sendCommand("stop")
    }
}
