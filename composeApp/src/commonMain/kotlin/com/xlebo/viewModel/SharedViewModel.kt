package com.xlebo.viewModel

import androidx.lifecycle.ViewModel
import com.xlebo.model.Participant
import com.xlebo.networking.HemaRatingClient
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class SharedUiState(
    val name: String = "Nový turnaj",
    val filePath: String? = null,
    val participants: List<Participant> = listOf(),
    val category: Int = 1,
    val lowRank: Int = 3000,
    val highRank: Int = 500
)

class SharedViewModel(private val hemaRating: HemaRatingClient, private val coroutineScope: CoroutineScope) : ViewModel() {
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
        _uiState.update { current -> current.copy(name = name) }
    }

    fun setFilePath(path: String?) {
        _uiState.update { current -> current.copy(filePath = path) }
    }

    fun updateParticipant(participant: Participant) {
        val newList = _uiState.value.participants.toMutableList()
        newList[participant.order - 1] = participant
        _uiState.update { current -> current.copy(participants = newList) }
    }

    fun fetchParticipantRanks() {
        _uiState.value.participants.chunked(20).forEach {
            coroutineScope.launch {
                val ranks = hemaRating.fetchParticipantRanks(it, 1)
                Napier.i { "Fetched ranks: $ranks" }
                ranks.forEach { Napier.i { "$it" } }
                ranks.forEach { updated -> updateParticipant(updated) }
            }
        }
    }

    fun wakeUpHemaRatings() {
        coroutineScope.launch { hemaRating.wakeUp() }
    }
}