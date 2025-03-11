package com.xlebo.screens.table.participantsPreview

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant
import com.xlebo.screens.table.TableCell

@Composable
fun NewParticipantTableRow(
    weights: List<Float>,
    order: Int,
    onAdd: (Participant) -> Unit
) {
    var hrId by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var lang by remember { mutableStateOf("") }
    var rank by remember { mutableStateOf("") }

    fun checkFields(): Boolean {
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                nationality.isNotEmpty() &&
                lang.isNotEmpty()
    }

    Row(
        modifier = Modifier.background(Color.Green)
    ) {
        TableCell(order.toString(), Modifier.weight(weights[0]), enabled = false) {}
        TableCell(hrId, Modifier.weight(weights[1]), isNumber = true) { hrId = it }
        TableCell(firstName, Modifier.weight(weights[2])) { firstName = it}
        TableCell(lastName, Modifier.weight(weights[3])) { lastName = it }
        TableCell(club, Modifier.weight(weights[4])) { club = it }
        TableCell(nationality, Modifier.weight(weights[5])) { nationality = it }
        TableCell(lang, Modifier.weight(weights[6])) { lang = it }
        TableCell(rank, Modifier.weight(weights[7]), isNumber = true) { rank = it }
        OutlinedButton(
            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically),
            onClick = {
                if (checkFields())
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