package com.xlebo.screens

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Navigation : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object CreateTournament : Screen

    @Serializable
    data object TournamentDetail : Screen
}

sealed interface NestedNavigation {
    @Serializable
    data object CreateTournament : NestedNavigation
}