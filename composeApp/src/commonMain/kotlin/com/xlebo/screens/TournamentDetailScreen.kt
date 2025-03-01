package com.xlebo.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.xlebo.model.Participant
import com.xlebo.screens.table.ParticipantTable
import com.xlebo.screens.table.TableRow
import com.xlebo.viewModel.TournamentDetailsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TournamentDetailScreen(
    navController: NavHostController,
    participants: List<Participant>,
) {
    val viewModel = TournamentDetailsViewModel()

    ParticipantTable(participants)

}



