package com.xlebo.viewModel

import androidx.lifecycle.ViewModel
import com.xlebo.model.Participant
import com.xlebo.model.TournamentState
import com.xlebo.networking.HemaRatingClient
import com.xlebo.utils.Constants
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class SharedUiState(
    val tournamentState: TournamentState = TournamentState.NEW,
    val name: String = "Nový turnaj",
    val participants: List<Participant> = listOf(),
    val category: String = "1",
    val groupCount: String = "6",
    val lowRank: String = Constants.LOW_SKILL_THRESHOLD.toString(),
    val highRank: String = Constants.HIGH_SKILL_THRESHOLD.toString()
)

class SharedViewModel(
    private val hemaRating: HemaRatingClient,
    private val coroutineScope: CoroutineScope,
    private val persistenceHandler: PersistenceHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { SharedUiState() }
    }

    fun setParticipants(participants: List<Participant>) {
        _uiState.update { current -> current.copy(participants = participants) }
    }

    fun addParticipant(participant: Participant) {
        _uiState.update { current ->
            current.copy(participants = current.participants + participant)
        }
    }

    fun setName(name: String) {
        check(_uiState.value.tournamentState == TournamentState.NEW)
        _uiState.update { current -> current.copy(name = name) }
    }

    fun updateParticipant(participant: Participant) {
        val newList = _uiState.value.participants.toMutableList()
        newList[participant.order - 1] = participant
        _uiState.update { current -> current.copy(participants = newList) }
    }

    fun fetchParticipantRanks(category: Int) {
        _uiState.value.participants.chunked(20).forEach {
            coroutineScope.launch {
                val ranks = hemaRating.fetchParticipantRanks(it, category)
                Napier.i { "Fetched ranks: $ranks" }
                ranks.forEach { Napier.i { "$it" } }
                ranks.forEach { updated -> updateParticipant(updated) }
            }
        }
    }

    fun setCategory(category: String) {
        _uiState.update { current -> current.copy(category = category) }
    }

    fun saveData(tournamentState: TournamentState = _uiState.value.tournamentState) {
        if (_uiState.value.tournamentState != tournamentState)
            _uiState.update { current -> current.copy(tournamentState = tournamentState) }
        persistenceHandler.saveTournamentState(_uiState.value)
    }

    fun loadTournamentState(tournament: String) {
        val newState = persistenceHandler.loadTournamentState(tournament)
        _uiState.update { _ -> newState }
        Napier.i { "Loaded state: ${uiState.value}" }
    }

    fun generateGroups() {
        val lowRank = mutableListOf<Participant>()
        val midRank = mutableListOf<Participant>()
        val highRank = mutableListOf<Participant>()

        val lowRankThreshold = _uiState.value.lowRank.toIntOrNull() ?: return
        val highRankThreshold = _uiState.value.highRank.toIntOrNull() ?: return

        if (lowRankThreshold < highRankThreshold) {
            // TODO drop some actual alert, no one reads logs
            Napier.w { "Bruh; $lowRankThreshold should be greater than $highRankThreshold" }
            return
        }

        _uiState.value.participants.forEach { participant ->
            when {
                participant.rank!! < highRankThreshold -> highRank.add(participant)
                participant.rank > lowRankThreshold -> lowRank.add(participant)
                else -> midRank.add(participant)
            }
        }

        lowRank.shuffle(); midRank.shuffle(); highRank.shuffle()

        val groupCount = _uiState.value.groupCount.toIntOrNull() ?: return

        (highRank + midRank + lowRank).forEachIndexed { index, participant ->
            updateParticipant(participant.copy(group = index % groupCount + 1))
        }
    }

    fun setHighRank(rank: String) {
        _uiState.update { current -> current.copy(highRank = rank) }
    }

    fun setLowRank(rank: String) {
        _uiState.update { current -> current.copy(lowRank = rank) }
    }

    fun setGroupCount(count: String) {
        _uiState.update { current -> current.copy(groupCount = count) }
    }
}