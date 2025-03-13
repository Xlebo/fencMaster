package com.xlebo.viewModel

/**
 * Placeholder class for ui state persistence, for now some CSV saving logic for Desktop,
 * an HttpClient for communication with backend later on.
 */
interface PersistenceHandler {

    fun getExistingTournaments(): List<String>

    fun saveTournamentState(uiState: com.xlebo.viewModel.TournamentState)

    fun loadTournamentState(fileName: String): com.xlebo.viewModel.TournamentState
}
