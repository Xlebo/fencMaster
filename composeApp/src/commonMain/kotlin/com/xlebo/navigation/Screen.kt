package com.xlebo.navigation

import com.xlebo.model.Participant

sealed interface Screen {
    data object Home : Screen
    data object CrateTournament : Screen
    data class TournamentDetail(val participants: List<Participant>) : Screen
}