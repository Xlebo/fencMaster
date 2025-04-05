package com.xlebo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
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
import com.xlebo.model.GroupResults
import com.xlebo.model.Participant
import com.xlebo.model.TournamentState
import com.xlebo.screens.dialog.SubmitDialog
import com.xlebo.screens.table.groupsPreview.GroupTableRow
import com.xlebo.utils.backButton
import com.xlebo.utils.defaultButton
import com.xlebo.utils.tournamentDetailTableCell
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupsPreviewScreen(
    navController: NavHostController,
    lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null,
) {
    var submitDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var selectedParticipant: Participant? by remember { mutableStateOf(null) }

    if (uiState.participants.all { it.group == null }) {
        viewModel.generateGroups()
    }

    fun Participant.getColor(): Color {
        if (uiState.highRank.isEmpty() || uiState.lowRank.isEmpty()) {
            return Color.White
        }
        return when {
            this == selectedParticipant -> Color.Blue
            rank!! < uiState.highRank.toInt() -> Color.Red
            rank > uiState.lowRank.toInt() -> Color.Green
            else -> Color.Yellow
        }
    }

    if (submitDialog) {
        SubmitDialog(onBackRequest = { submitDialog = false }, onContinueRequest = {
            viewModel.setGroupsResults(
                uiState.participants.groupBy { it.group }.map { group ->
                    val participants = group.value.sortedBy { it.order }
                    val results = participants.flatMapIndexed { index, first ->
                        participants.drop(index + 1)
                            .map { second -> (first to second) to ("" to "") }
                    }.toMap()
                    group.key!! to GroupResults(group.key!!, results, false)
                }.toMap()
            )

            viewModel.saveData(TournamentState.GROUPS_STARTED)
            navController.navigate(Screen.GroupsInProgress)
        })
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState, contentPadding = PaddingValues(15.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        modifier = Modifier.backButton().align(Alignment.CenterStart),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        onClick = {
                            navController.navigate(Screen.Home)
                            viewModel.saveData()
                            viewModel.reset()
                        }) { Text("Home") }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = uiState.name,
                        fontSize = 24.sp
                    )
                }
                Spacer(Modifier)
            }

            // Ranks Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "High Skill >", modifier = Modifier.width(100.dp)
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(100.dp).padding(10.dp).height(60.dp),
                        value = uiState.highRank,
                        onValueChange = {
                            val num = it.toIntOrNull()
                            if (num != null || it.isEmpty()) {
                                viewModel.setHighRank(it)
                            }
                        },
                        singleLine = true
                    )
                    Text("> Mid Skill >", modifier = Modifier.width(100.dp))
                    OutlinedTextField(
                        modifier = Modifier.width(100.dp).padding(10.dp).height(60.dp),
                        value = uiState.lowRank,
                        onValueChange = {
                            val num = it.toIntOrNull()
                            if (num != null || it.isEmpty()) {
                                viewModel.setLowRank(it)
                            }
                        },
                        singleLine = true
                    )
                    Text("> Low Skill", modifier = Modifier.width(100.dp))
                    Button(
                        modifier = Modifier.defaultButton(), onClick = {
                            viewModel.generateGroups()
                        }) { Text("Generate Groups") }
                }
            }

            // Group Count and Skill bracket amounts
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "High skill: ${
                            if (uiState.highRank.isEmpty()) 0
                            else uiState.participants.count { it.rank!! < uiState.highRank.toInt() }
                        }", color = Color.Red, modifier = Modifier.width(100.dp))
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        "Mid skill: ${
                            if (uiState.highRank.isEmpty() || uiState.lowRank.isEmpty()) 0
                            else uiState.participants.count { it.rank!! > uiState.highRank.toInt() && it.rank < uiState.lowRank.toInt() }
                        }", modifier = Modifier.width(100.dp), color = Color.Yellow)
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        "Low skill: ${
                            if (uiState.lowRank.isEmpty()) 0
                            else uiState.participants.count { it.rank!! > uiState.lowRank.toInt() }
                        }", modifier = Modifier.width(100.dp), color = Color.Green)
                    Text(
                        "Group Count:", modifier = Modifier.width(130.dp).padding(10.dp)
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp).padding(10.dp).height(60.dp),
                        value = uiState.groupCount,
                        onValueChange = {
                            val num = it.toIntOrNull()
                            if (it.length < 3 && (num != null || it.isEmpty())) {
                                viewModel.setGroupCount(it)
                            }
                        },
                        singleLine = true
                    )
                }
            }

            if (uiState.participants.all { it.group != null }) {
                uiState.participants.groupBy { it.group }.toList()
                    .map { group -> group.first to group.second.sortedBy { it.rank } }
                    .sortedBy { it.first }.forEach { group ->
                        item {
                            Row {
                                Text("Group ${group.first}")
                            }
                            Row {
                                Text(
                                    "Name",
                                    modifier = Modifier.tournamentDetailTableCell()
                                        .fillMaxWidth(.2f)
                                )
                                Text(
                                    "Club",
                                    modifier = Modifier.tournamentDetailTableCell()
                                        .fillMaxWidth(.3f)
                                )
                                Text(
                                    "Rank",
                                    modifier = Modifier.tournamentDetailTableCell()
                                        .fillMaxWidth(.2f)
                                )
                                Spacer(
                                    modifier = Modifier.fillMaxWidth().background(Color.White)
                                )
                            }
                        }
                        items(group.second) { participant ->
                            GroupTableRow(
                                participant,
                                listOf(.2f, .3f, .2f),
                                participant.getColor()
                            ) {
                                when (selectedParticipant) {
                                    null -> {
                                        selectedParticipant = participant
                                    }

                                    participant -> {
                                        selectedParticipant = null
                                    }

                                    else -> {
                                        val selectedGroup = selectedParticipant!!.group
                                        viewModel.updateParticipant(
                                            selectedParticipant!!.copy(
                                                group = participant.group
                                            )
                                        )
                                        viewModel.updateParticipant(participant.copy(group = selectedGroup))
                                        selectedParticipant = null
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.defaultButton(),
                        onClick = {
                            if (uiState.participants.all { it.group != null })
                                submitDialog = true
                        }
                    ) { Text("Submit") }
                }
            }

        }
        if (lazyListScrollBar != null) {
            lazyListScrollBar(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), scrollState)
        }
    }

}