package com.xlebo.screens

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object CrateTournament : Screen

    @Serializable
    data class TournamentDetail(val name: String, val file: String?) : Screen
}