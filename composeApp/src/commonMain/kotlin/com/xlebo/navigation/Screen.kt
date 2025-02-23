package com.xlebo.navigation

import com.xlebo.FilePath

sealed interface Screen {
    data object Home : Screen
    data object Screen2 : Screen
    data class TournamentDetail(val file: FilePath?) : Screen
}