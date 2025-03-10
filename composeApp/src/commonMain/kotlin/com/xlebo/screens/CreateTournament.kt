package com.xlebo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xlebo.Platform
import com.xlebo.utils.backButton
import com.xlebo.utils.defaultButton
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateTournament(
    navController: NavHostController,
    platform: Platform,
) {
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var csvPath: String? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Nový Turnaj",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.padding(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Názov Turnaja", modifier = Modifier.padding(10.dp).weight(2f))
            OutlinedTextField(
                modifier = Modifier.weight(4f),
                value = uiState.name,
                onValueChange = { viewModel.setName(it) },
                singleLine = true
            )

            Spacer(Modifier.weight(1f))

            Text("Kategória", modifier = Modifier.padding(10.dp).weight(2f))
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = uiState.category,
                onValueChange = {
                    val num = it.toIntOrNull()
                    if (num != null || it.isEmpty()) {
                        viewModel.setCategory(it)
                    }
                }
            )

            Spacer(Modifier.weight(2f))
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    csvPath = platform.handleFileSelection()
                    viewModel.setParticipants(platform.handleParticipantsImport(csvPath))
                }
            ) {
                Text("Nahraj ucastnikov")
            }
            Spacer(Modifier.width(15.dp))
            Text(csvPath ?: "No file selected")
        }


        Row {
            Button(
                modifier = Modifier.backButton(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = { navController.popBackStack() }
            ) { Text("Back") }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.defaultButton(),
                onClick = {
                    navController.navigate(
                        Screen.TournamentDetail
                    )
                }
            ) { Text("Založiť turnaj") }
        }
    }
}