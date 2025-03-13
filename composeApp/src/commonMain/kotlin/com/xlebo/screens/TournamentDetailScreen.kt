package com.xlebo.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xlebo.model.TournamentState
import com.xlebo.screens.dialog.SubmitDialog
import com.xlebo.screens.table.participantsPreview.TableHeader
import com.xlebo.screens.table.participantsPreview.NewParticipantTableRow
import com.xlebo.screens.table.participantsPreview.ParticipantTableRow
import com.xlebo.utils.backButton
import com.xlebo.utils.defaultButton
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

//TODO: Progress bar on fetching fighters ranks
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TournamentDetailScreen(
    navController: NavHostController,
    lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null,
) {
    var submitDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    // used to keep width ratios of header and rows
    val weights = listOf(.5f, 1f, 1.5f, 2f, 3f, 2f, 2f, 1.5f)

    if (submitDialog) {
        SubmitDialog(
            onBackRequest = { submitDialog = false },
            onContinueRequest = {
                viewModel.setParticipants(
                    participants = uiState.participants
                    .filter { !it.disabled }
                    .map { if (it.rank == null) it.copy(rank = 99999) else it })
                viewModel.saveData(TournamentState.GROUPS_PREVIEW)
                navController.navigate(Screen.GroupsPreview)
            }
        )
    }
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(15.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = uiState.name,
                        fontSize = 24.sp
                    )
                }
                Spacer(Modifier.padding(20.dp))
            }

            stickyHeader {
                val headers = listOf(
                    "Pocet",
                    "HR ID",
                    "Jmeno",
                    "Prijmeni",
                    "Klub",
                    "Narodnost",
                    "Jazyk",
                    "HR Rank"
                )
                TableHeader(headers, weights)
            }

            items(uiState.participants, key = { item -> item.order }) { participant ->
                ParticipantTableRow(participant, weights)
            }

            item {
                NewParticipantTableRow(
                    weights,
                    uiState.participants.size + 1,
                ) { participant ->
                    viewModel.addParticipant(participant)
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.backButton(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        onClick = { navController.popBackStack() }
                    ) { Text("Back") }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.defaultButton(),
                        onClick = { viewModel.fetchParticipantRanks(uiState.category.toInt()) }
                    ) { Text("Fetch Ranks") }
                    Button(
                        modifier = Modifier.defaultButton(),
                        onClick = { submitDialog = true }
                    ) { Text("Submit") }
                }
            }
        }
        if (lazyListScrollBar != null) {
            lazyListScrollBar(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), scrollState)
        }
    }
//    }
}



