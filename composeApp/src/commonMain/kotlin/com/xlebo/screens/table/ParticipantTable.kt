package com.xlebo.screens.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
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
import com.xlebo.model.Participant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParticipantTable(participants: List<Participant>) {
    val focus = remember { FocusRequester() }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(15.dp)
    ) {
//        stickyHeader {
        item {
            TableHeader(Participant.getHeaders(), Participant.getWeights())
        }
//        }

        items(participants) { participant ->
            TableRow(participant, focus)
        }
    }
}