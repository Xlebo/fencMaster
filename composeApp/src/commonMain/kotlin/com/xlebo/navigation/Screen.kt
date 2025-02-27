package com.xlebo.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object CrateTournament : Screen

    @Serializable
    data class TournamentDetail(val file: String?) : Screen
}