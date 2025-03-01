package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import com.xlebo.model.Participant

@Composable
fun TableRow(participant: Participant, focus: FocusRequester) {
    val defaultColor = if (participant.order % 2 == 1) {
        Color.White
    } else {
        Color.LightGray
    }
    var color by remember { mutableStateOf(defaultColor) }

    Row(
        modifier = Modifier
            .onFocusChanged {
                color = if (it.hasFocus) {
                    Color.Cyan
                } else {
                    defaultColor
                }
            }
            .fillMaxWidth()
            .background(color)
            .focusRequester(focus)
    ) {
        val weights = Participant.getWeights()
        TableCell(participant.order.toString(), focus, Modifier.weight(weights[0]).background(Color.Gray), enabled = false)
        TableCell(participant.hrId?.toString() ?: "nemá", focus, Modifier.weight(weights[1]), true)
        TableCell(participant.firstName, focus, Modifier.weight(weights[2]))
        TableCell(participant.lastName, focus, Modifier.weight(weights[3]))
        TableCell(participant.club ?: "", focus, Modifier.weight(weights[4]))
        TableCell(participant.nationality ?: "", focus, Modifier.weight(weights[5]))
        TableCell(participant.lang ?: "", focus, Modifier.weight(weights[6]))
        TableCell(participant.rank?.toString() ?: "pleb", focus, Modifier.weight(weights[7]), true)
    }
}