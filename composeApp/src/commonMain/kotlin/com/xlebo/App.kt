package com.xlebo

import CreateTournament
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.xlebo.model.Participant
import com.xlebo.navigation.Screen
import com.xlebo.navigation.SimpleNavController
import com.xlebo.screens.HomeScreen
import com.xlebo.screens.TournamentDetailScreen

@Composable
fun App() {
    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
//            val navController = remember { SimpleNavController(Screen.Home) }
            val navController = remember {
                SimpleNavController(
                    Screen.TournamentDetail(
                        listOf(
                            Participant(
                                69,
                                "Meno",
                                "Priezvisko",
                                "Mile High Club",
                                "MAĎAR",
                                "Ugrofínsky",
                                420
                            )
                        )
                    )
                )
            }
            val currentScreen by navController.currentScreen.collectAsState()

            val platform = getPlatform()

            when (currentScreen) {
                is Screen.Home -> HomeScreen(navController = navController)
                is Screen.CrateTournament -> CreateTournament(
                    navController = navController,
                    platform = platform
                )

                is Screen.TournamentDetail -> {
                    val participants = (currentScreen as Screen.TournamentDetail).participants
                    TournamentDetailScreen(
                        navController = navController,
                        participants = participants
                    )
                }
            }
        }
    }
}