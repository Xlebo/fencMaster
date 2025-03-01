package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    var color by remember { mutableStateOf(Color.Cyan) }

    Row(
        modifier = Modifier
            .onFocusChanged {
                color = if (it.hasFocus) {
                    Color.Green
                } else {
                    Color.Cyan
                }
            }
            .fillMaxWidth()
            .background(color)
            .focusRequester(focus)
    ) {
        val weights = Participant.getWeights()
        TableCell(participant.hrId?.toString() ?: "nemá", focus, Modifier.weight(weights[0]), true)
        TableCell(participant.firstName, focus, Modifier.weight(weights[1]))
        TableCell(participant.lastName, focus, Modifier.weight(weights[2]))
        TableCell(participant.club ?: "", focus, Modifier.weight(weights[3]))
        TableCell(participant.nationality ?: "", focus, Modifier.weight(weights[4]))
        TableCell(participant.lang ?: "", focus, Modifier.weight(weights[5]))
        TableCell(participant.rank?.toString() ?: "pleb", focus, Modifier.weight(weights[6]), true)
    }
}