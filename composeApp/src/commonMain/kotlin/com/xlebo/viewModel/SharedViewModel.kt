package com.xlebo.viewModel

import androidx.lifecycle.ViewModel
import com.xlebo.model.GroupResults
import com.xlebo.model.GroupStatistics
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
    val groupsResults: Map<Int, GroupResults> = mapOf(),
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

    fun updateResultForGroup(groupNo: Int, pair: Match, score: Pair<String, String>) {
        val newGroupResults = _uiState.value.groupsResults.toMutableMap()
        val newResults = newGroupResults[groupNo]!!.results.toMutableMap()
        newResults[pair] = score
        newGroupResults[groupNo] = newGroupResults[groupNo]!!.copy(results = newResults)
        _uiState.update { current -> current.copy(groupsResults = newGroupResults) }
    }

    fun updateGroupResults(groupResults: GroupResults) {
        val newGroupResults = _uiState.value.groupsResults.toMutableMap()
        newGroupResults[groupResults.id] = groupResults
        _uiState.update { current -> current.copy(groupsResults = newGroupResults) }
    }

    fun setGroupsResults(groupsResults: Map<Int, GroupResults>) {
        _uiState.update { current -> current.copy(groupsResults = groupsResults) }
    }

    fun calculateGroupStatistics() {
        // replace all Vs by actual values
        _uiState.update { current ->
            current.copy(groupsResults = _uiState.value.groupsResults.map { gr ->
                gr.key to gr.value.copy(
                    results = gr.value.results.map {
                        it.key to (
                                it.value.first.replace("V", _uiState.value.groupMaxPoints) to
                                        it.value.second.replace("V", _uiState.value.groupMaxPoints)
                                )
                    }.toMap()
                )
            }.toMap())
        }

        _uiState.value.groupsResults.values.forEach { group ->
            evaluateGroupStatistics(group).forEach { participant ->
                this.updateParticipant(participant)
            }
        }

        val participantsOrdered = _uiState.value.participants.sortedWith(
            compareByDescending<Participant> { it.groupStatistics!!.wins / it.groupStatistics.totalMatches }
                .thenByDescending{ it.groupStatistics!!.hitsScored }
                .thenBy { it.groupStatistics!!.hitsReceived }

        )
            .mapIndexed { index, participant ->
                participant.copy(playOffOrder = index + 1)
            }

        this.setParticipants(participantsOrdered)
    }

    private fun evaluateGroupStatistics(group: GroupResults): List<Participant> {
        val WINS = 0
        val ALL = 1
        val SCORED = 2
        val RECEIVED = 3

        val statistics = group.results.flatMap { listOf(it.key.first, it.key.second) }.distinct()
            .associateWith { mutableListOf(0f, 0f, 0f, 0f) }
            .toMap()

        group.results.forEach { (participants, results) ->
            val (p1, p2) = participants
            statistics[p1]!![ALL]++
            statistics[p1]!![SCORED] += results.first.toFloat()
            statistics[p1]!![RECEIVED] += results.second.toFloat()

            statistics[p2]!![ALL]++
            statistics[p2]!![SCORED] += results.second.toFloat()
            statistics[p2]!![RECEIVED] += results.first.toFloat()

            when {
                results.first == results.second -> {
                    statistics[p1]!![WINS] += 0.5f
                    statistics[p2]!![WINS] += 0.5f
                }

                results.first.toInt() < results.second.toInt() ->
                    statistics[p2]!![WINS]++

                else ->
                    statistics[p1]!![WINS]++
            }
        }

        return statistics.keys.map { it.copy(groupStatistics = GroupStatistics(statistics[it]!!)) }
    }
}