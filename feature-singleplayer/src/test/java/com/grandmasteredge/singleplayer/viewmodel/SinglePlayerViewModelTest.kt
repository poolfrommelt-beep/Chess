package com.grandmasteredge.singleplayer.viewmodel

import com.grandmasteredge.core.logic.FenParser
import com.grandmasteredge.core.model.Color
import com.grandmasteredge.core.model.Move
import com.grandmasteredge.core.model.Square
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SinglePlayerViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        val viewModel = SinglePlayerViewModel()
        val state = viewModel.uiState.value
        assertEquals("Your turn (White)", state.feedback)
        assertEquals(Color.WHITE, state.gameState.turn)
    }

    @Test
    fun testPlayerMoveChangesTurn() = runTest {
        val viewModel = SinglePlayerViewModel()
        // e2 to e4
        val move = Move(Square(4, 1), Square(4, 3))
        viewModel.onMove(move)

        val state = viewModel.uiState.value
        assertEquals(Color.BLACK, state.gameState.turn)
        assertEquals("Stockfish is thinking...", state.feedback)
    }
}
