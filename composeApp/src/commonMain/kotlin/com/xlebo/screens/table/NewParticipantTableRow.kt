package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant

@Composable
fun NewParticipantTableRow(
    weights: List<Float>,
    order: Int,
    onAdd: (Participant) -> Unit
) {
    val hrId by remember { mutableStateOf("") }
    val firstName by remember { mutableStateOf("") }
    val lastName by remember { mutableStateOf("") }
    val club by remember { mutableStateOf("") }
    val nationality by remember { mutableStateOf("") }
    val lang by remember { mutableStateOf("") }
    val rank by remember { mutableStateOf("") }

    fun checkFields(): Boolean {
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                nationality.isNotEmpty() &&
                lang.isNotEmpty()
    }

    Row(
        modifier = Modifier.background(Color.Green)
    ) {
        TableCell(order.toString(), Modifier.weight(weights[0]), enabled = false)
        TableCell(hrId, Modifier.weight(weights[1]))
        TableCell(firstName, Modifier.weight(weights[2]))
        TableCell(lastName, Modifier.weight(weights[3]))
        TableCell(club, Modifier.weight(weights[4]))
        TableCell(nationality, Modifier.weight(weights[5]))
        TableCell(lang, Modifier.weight(weights[6]))
        TableCell(rank, Modifier.weight(weights[7]))
        OutlinedButton(
            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically),
            onClick = {
//                if (checkFields())
                    onAdd(Participant(order, hrId.toIntOrNull(), firstName, lastName, club, nationality, lang, rank.toIntOrNull()))
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
    }
}