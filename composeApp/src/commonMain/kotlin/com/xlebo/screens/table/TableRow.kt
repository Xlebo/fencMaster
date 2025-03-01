package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
//        horizontalArrangement = Arrangement.SpaceEvenly,
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
        TableCell(participant.hrId?.toString() ?: "nemá", focus, weights[0],true)
        TableCell(participant.firstName, focus, weights[1])
        TableCell(participant.lastName, focus, weights[2])
        TableCell(participant.club ?: "", focus, weights[3])
        TableCell(participant.nationality ?: "", focus, weights[4])
        TableCell(participant.lang ?: "", focus, weights[5])
        TableCell(participant.rank?.toString() ?: "pleb", focus, weights[6], true)
    }
}