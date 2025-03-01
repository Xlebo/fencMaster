package com.xlebo

import CreateTournament
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.xlebo.navigation.Screen
import com.xlebo.screens.HomeScreen
import com.xlebo.screens.TournamentDetailScreen

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val platform = getPlatform()
            val navigationController = rememberNavController()
            NavHost(
                navController = navigationController,
                startDestination = Screen.Home,
            ) {
                composable<Screen.Home> { HomeScreen(navController = navigationController) }
                composable<Screen.CrateTournament> { CreateTournament(
                    navController = navigationController,
                    platform = platform
                ) }
                composable<Screen.TournamentDetail> {
                    val detail: Screen.TournamentDetail = it.toRoute()
                    TournamentDetailScreen(
                        navController = navigationController,
                        participants = platform.handleParticipantsImport(detail.file)
                    )
                }
            }
//            val navController = remember { SimpleNavController(Screen.Home) }
//            val navController = remember {
//                SimpleNavController(
//                    Screen.TournamentDetail(
//                        listOf(
//                            Participant(
//                                69,
//                                "Meno",
//                                "Priezvisko",
//                                "Mile High Club",
//                                "MAĎAR",
//                                "Ugrofínsky",
//                                420
//                            )
//                        )
//                    )
//                )
//            }
        }
    }
}