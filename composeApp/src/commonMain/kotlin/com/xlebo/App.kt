package com.xlebo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.xlebo.screens.CreateTournament
import com.xlebo.screens.HomeScreen
import com.xlebo.screens.NestedNavigation
import com.xlebo.screens.Screen
import com.xlebo.screens.TournamentDetailScreen
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
fun App(lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null) {
    KoinApplication(
        application = {
            modules(
                module {
                    single { SharedViewModel() }
                }
            )
        }
    ) {
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
                    composable<Screen.Home> {
                        HomeScreen(navController = navigationController)
                    }

                    navigation<NestedNavigation.CreateTournament>(
                        startDestination = Screen.CreateTournament
                    ) {
                        composable<Screen.CreateTournament> {
                            CreateTournament(
                                navController = navigationController,
                                platform = platform,
                            )
                        }
                        composable<Screen.TournamentDetail> {
                            TournamentDetailScreen(
                                navController = navigationController,
                                lazyListScrollBar,
                            )
                        }
                    }
                }
            }
        }
    }
}