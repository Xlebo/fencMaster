package com.xlebo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.xlebo.screens.table.groupsInProgress.GroupInProgressTable
import com.xlebo.utils.backButton
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupsInProgressScreen(
    navController: NavHostController,
    lazyListScrollBar: @Composable ((Modifier, LazyListState) -> Unit)? = null,
) {
    var submitDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (submitDialog) {
        SubmitDialog(onBackRequest = { submitDialog = false }, onContinueRequest = {
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

            items(
                uiState.participants.groupBy { it.group }.toList()
                .map { group -> group.first to group.second.sortedBy { it.rank } }
                .sortedBy { it.first })
            { group ->
                GroupInProgressTable(group.second, uiState.groupMaxPoints.toInt()) { }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        if (lazyListScrollBar != null) {
            lazyListScrollBar(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), scrollState)
        }
    }
}