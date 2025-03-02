package com.xlebo.viewModel

import androidx.lifecycle.ViewModel
import com.xlebo.model.Participant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

@Serializable
data class SharedUiState(
    val name: String = "Nový Turnaj",
    val filePath: String? = null,
    val participants: List<Participant> = listOf()
)

class SharedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState = _uiState.asStateFlow()

    init {
        println("Created new instance of shared view model with $uiState")
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
}