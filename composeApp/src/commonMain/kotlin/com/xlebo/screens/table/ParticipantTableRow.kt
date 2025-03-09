package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ParticipantTableRow(participant: Participant) {
    val viewModel: SharedViewModel = koinViewModel()
    fun defaultColor(): Color {
        if (participant.disabled) {
            return Color.Red
        }
        return if (participant.order % 2 == 1) {
            Color.White
        } else {
            Color.LightGray
        }
    }

    var color by remember { mutableStateOf( defaultColor()) }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .background(color)
            .onFocusEvent {
                color = if (participant.disabled) {
                    Color.Red
                } else {
                    if (it.hasFocus) {
                        Color.Cyan
                    } else {
                        defaultColor()
                    }
                }
            }
            .fillMaxWidth()
    ) {
        val weights = Participant.getWeightsForPreview()

        TableCell(
            participant.order.toString(),
            Modifier.weight(weights[0]).background(Color.Gray),
            enabled = false
        ) {}
        TableCell(
            participant.hrId?.toString() ?: "", Modifier.weight(weights[1]), true,
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(hrId = new.toIntOrNull())) }
        TableCell(
            participant.firstName,
            Modifier.weight(weights[2]),
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(firstName = new)) }
        TableCell(
            participant.lastName,
            Modifier.weight(weights[3]),
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(lastName = new)) }
        TableCell(
            participant.club ?: "",
            Modifier.weight(weights[4]),
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(club = new)) }
        TableCell(
            participant.nationality ?: "",
            Modifier.weight(weights[5]),
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(nationality = new)) }
        TableCell(
            participant.lang ?: "",
            Modifier.weight(weights[6]),
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(lang = new)) }
        TableCell(
            participant.rank?.toString() ?: "",
            Modifier.weight(weights[7]),
            true,
            enabled = !participant.disabled
        ) { new -> viewModel.updateParticipant(participant.copy(rank = new.toIntOrNull())) }

        if (participant.disabled) {
            OutlinedButton(
                modifier = Modifier.size(25.dp).align(Alignment.CenterVertically).focusable(false),
                onClick = {
                    viewModel.updateParticipant(participant.copy(disabled = false))
                },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Green),
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Enable participant",
                    tint = Color.Green
                )
            }
        } else {
            OutlinedButton(
                modifier = Modifier.size(25.dp).align(Alignment.CenterVertically).focusable(false),
                onClick = {
                    color = Color.Red
                    viewModel.updateParticipant(participant.copy(disabled = true))
                    focusManager.clearFocus()
                },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Disable participant",
                    tint = Color.Red
                )
            }
        }
    }
}