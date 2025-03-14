package com.xlebo.viewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.xlebo.model.Participant
import com.xlebo.model.TournamentState
import com.xlebo.networking.HemaRatingClient
import com.xlebo.screens.table.groupsInProgress.Match
import com.xlebo.utils.Constants
import com.xlebo.utils.generateGroupOrderOptimized
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class TournamentState(
    val tournamentState: TournamentState = TournamentState.NEW,
    val name: String = "Nový turnaj",
    val participants: List<Participant> = listOf(),
    val category: String = "1",
    val groupCount: String = "",
    val lowRank: String = "",
    val highRank: String = "",
    val groupMaxPoints: String = Constants.GROUP_MAX_POINTS.toString(),
    val playoffMaxPoints: String = Constants.PLAYOFF_MAX_POINTS.toString(),
    val groupsResults: Map<Match, Pair<Int, Int>> = mapOf(),
    val matchOrders: Map<Int, List<Match>> = mapOf()
)

class SharedViewModel(
    private val hemaRating: HemaRatingClient,
    private val coroutineScope: CoroutineScope,
    private val persistenceHandler: PersistenceHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow(TournamentState())
    val uiState = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { TournamentState() }
    }

    fun setParticipants(participants: List<Participant>) {
        val orderedParticipants = participants.mapIndexed { index, p -> p.copy(order = index + 1) }
        _uiState.update { current -> current.copy(participants = orderedParticipants) }
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

        val sortedParticipants = _uiState.value.participants.sortedBy { it.rank }

        if (_uiState.value.lowRank.isEmpty()) {
            _uiState.update { current ->
                current.copy(lowRank = sortedParticipants[sortedParticipants.size * 2 / 3].rank.toString())
            }
        }

        if (_uiState.value.highRank.isEmpty()) {
            _uiState.update { current ->
                current.copy(highRank = sortedParticipants[sortedParticipants.size / 3].rank.toString())
            }
        }

        if (_uiState.value.groupCount.isEmpty()) {
            _uiState.update { current ->
                current.copy(groupCount = (sortedParticipants.size / 7 + 1).toString())
            }
        }

        val lowRankThreshold = _uiState.value.lowRank.toInt()
        val highRankThreshold = _uiState.value.highRank.toInt()

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

    fun setGroupMaxPoints(points: String) {
        _uiState.update { current -> current.copy(groupMaxPoints = points) }
    }

    fun setPlayoffMaxPoints(points: String) {
        _uiState.update { current -> current.copy(playoffMaxPoints = points) }
    }

    fun generateGroupOrder(matches: List<Match>, group: Int) {
        val orderedMatches = generateGroupOrderOptimized(matches)
        _uiState.update { current ->
            current.copy(
                matchOrders = current.matchOrders.toMutableMap()
                    .apply { this[group] = orderedMatches })
        }
        persistenceHandler.saveTournamentState(uiState.value)
    }
}