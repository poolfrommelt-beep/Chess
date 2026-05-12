package com.grandmasteredge.kids.viewmodel

import org.junit.Assert.*
import org.junit.Test

class KidsViewModelTest {
    @Test
    fun testInitialState() {
        val viewModel = KidsViewModel()
        val state = viewModel.uiState.value
        assertTrue(state.showHints)
    }
}
