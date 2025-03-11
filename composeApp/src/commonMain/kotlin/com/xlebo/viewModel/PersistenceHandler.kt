package com.xlebo.viewModel

import com.xlebo.model.TournamentState

/**
 * Placeholder class for ui state persistence, for now some CSV saving logic for Desktop,
 * an HttpClient for communication with backend later on.
 */
interface PersistenceHandler {

    fun getExistingTournaments(): List<String>

    fun saveTournamentState(uiState: SharedUiState)

    fun loadTournamentState(fileName: String): SharedUiState
}
