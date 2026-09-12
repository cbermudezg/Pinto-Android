package com.sloth.partyquest

import com.sloth.partyquest.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun testFivePlayersInitialized() {
        val state = viewModel.uiState.value
        assertEquals(5, state.players.size)
        assertEquals(1, state.players[0].id)
        assertEquals(2, state.players[1].id)
        assertEquals(3, state.players[2].id)
        assertEquals(4, state.players[3].id)
        assertEquals(5, state.players[4].id)
    }

    @Test
    fun testDealtCardsForFivePlayers() {
        val state = viewModel.uiState.value
        state.players.forEach { player ->
            assertEquals(3, player.faceUpCards.size)
            assertEquals(3, player.faceDownCards.size)
            assertEquals(3, player.onHandCards.size)
        }
    }

    @Test
    fun testCheckTurn() {
        val state = viewModel.uiState.value
        val currentPlayer = state.getCurrentPlayer()
        assertTrue(viewModel.checkTurn(currentPlayer))
    }
}
