package com.grandmasteredge.multiplayer.viewmodel

import org.junit.Assert.*
import org.junit.Test

class MultiplayerViewModelTest {
    @Test
    fun testInitialState() {
        val viewModel = MultiplayerViewModel()
        val state = viewModel.uiState.value
        assertEquals("White to move", state.feedback)
    }
}
