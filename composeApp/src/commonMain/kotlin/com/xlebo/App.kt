package com.xlebo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.xlebo.networking.HemaRatingClient
import com.xlebo.screens.CreateTournament
import com.xlebo.screens.GroupsInProgressScreen
import com.xlebo.screens.GroupsPreviewScreen
import com.xlebo.screens.HomeScreen
import com.xlebo.screens.PlayOffPreviewScreen
import com.xlebo.screens.Screen
import com.xlebo.screens.TournamentDetailScreen
import com.xlebo.viewModel.PersistenceHandler
import com.xlebo.viewModel.SharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
fun App(
    hemaRatingClient: HemaRatingClient,
    persistenceHandler: PersistenceHandler,
    lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch { hemaRatingClient.wakeUp() }


    KoinApplication(
        application = {
            modules(
                module {
                    single { SharedViewModel(hemaRatingClient, coroutineScope, persistenceHandler) }
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
                        HomeScreen(navController = navigationController, persistenceHandler = persistenceHandler)
                    }

                    composable<Screen.CreateTournament> {
                        CreateTournament(
                            navController = navigationController,
                            platform = platform,
                        )
                    }
                    composable<Screen.TournamentDetail> {
                        TournamentDetailScreen(
                            navController = navigationController,
                            lazyListScrollBar = lazyListScrollBar,
                        )
                    }

                    composable<Screen.GroupsPreview> {
                        GroupsPreviewScreen(
                            navController = navigationController,
                            lazyListScrollBar = lazyListScrollBar
                        )
                    }

                    composable<Screen.GroupsInProgress> {
                        GroupsInProgressScreen(
                            navController = navigationController,
                            lazyListScrollBar = lazyListScrollBar
                        )
                    }

                    composable<Screen.PlayOffPreview> {
                        PlayOffPreviewScreen(
                            navigationController,
                            lazyListScrollBar
                        )
                    }
                }
            }
        }
    }
}