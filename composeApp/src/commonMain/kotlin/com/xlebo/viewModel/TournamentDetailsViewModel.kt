package com.xlebo.viewModel

import androidx.lifecycle.ViewModel
import com.xlebo.model.Participant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TournamentDetailsViewModel : ViewModel() {
    private val _participants = MutableStateFlow(listOf<Participant>())
    val participants: StateFlow<List<Participant>> = _participants.asStateFlow()
}