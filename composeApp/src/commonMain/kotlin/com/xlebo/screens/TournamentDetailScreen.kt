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
import com.xlebo.model.Participant
import com.xlebo.screens.dialog.NotImplementedDialog
import com.xlebo.screens.table.NewParticipantTableRow
import com.xlebo.screens.table.ParticipantTableRow
import com.xlebo.screens.table.TableHeader
import com.xlebo.utils.backButton
import com.xlebo.utils.defaultButton
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TournamentDetailScreen(
    navController: NavHostController,
    lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null,
) {
    var notImplementedDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    viewModel.wakeUpHemaRatings()

    if (notImplementedDialog) {
        NotImplementedDialog { notImplementedDialog = false }
    } else {
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
                    TableHeader(Participant.getHeaders(), Participant.getWeightsForPreview())
                }

                items(uiState.participants, key = { item -> item.order }) { participant ->
                    ParticipantTableRow(participant)
                }

                item {
                    NewParticipantTableRow(
                        Participant.getWeightsForPreview(),
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
                            onClick = { viewModel.fetchParticipantRanks() }
                        ) { Text("Fetch Ranks") }
                        Button(
                            modifier = Modifier.defaultButton(),
                            onClick = { notImplementedDialog = true }
                        ) { Text("Preview Groups") }
                    }
                }
            }
            if (lazyListScrollBar != null) {
                lazyListScrollBar(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), scrollState)
            }
        }
    }
}



