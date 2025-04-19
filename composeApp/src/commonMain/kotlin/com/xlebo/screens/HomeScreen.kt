package com.xlebo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xlebo.model.TournamentStatus
import com.xlebo.utils.defaultButton
import com.xlebo.viewModel.PersistenceHandler
import com.xlebo.viewModel.SharedViewModel
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    persistenceHandler: PersistenceHandler
) {
    val viewModel: SharedViewModel = koinViewModel()

    Column {
        Text(
            text = "FencMaster",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Spacer(Modifier.padding(20.dp))
                Button(
                    modifier = Modifier.defaultButton().align(Alignment.CenterHorizontally),
                    onClick = { navController.navigate(Screen.CreateTournament) }
                ) { Text("Založ Turnaj", fontSize = 15.sp) }
            }
            Divider(modifier = Modifier.fillMaxHeight().width(1.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                item {
                    Text(
                        text = "Otvor existujúci turnaj:",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                items(persistenceHandler.getExistingTournaments()) { tournament ->
                    TextButton(
                        onClick = {
                            viewModel.loadTournamentState(tournament)
                            navController.navigate(
                                when (viewModel.uiState.value.tournamentState) {
                                    TournamentStatus.NEW -> {
                                        Napier.w { "Trying to navigate to NEW state tournament: $tournament" }
                                        Screen.CreateTournament
                                    }
                                    TournamentStatus.GROUPS_PREVIEW -> Screen.GroupsPreview
                                    TournamentStatus.GROUPS_STARTED -> Screen.GroupsInProgress
                                    TournamentStatus.PLAYOFF -> Screen.PlayOffPreview
                                }
                            )
                        },
                    ) { Text(tournament) }
                }
            }
        }
    }
}