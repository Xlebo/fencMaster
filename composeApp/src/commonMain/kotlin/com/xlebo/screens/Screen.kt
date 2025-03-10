package com.xlebo.screens

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object CreateTournament : Screen

    @Serializable
    data object TournamentDetail : Screen

    @Serializable
    data object GroupsPreview : Screen

    @Serializable
    data object GroupsInProgress : Screen

    @Serializable
    data object PlayOffInProgress : Screen
}