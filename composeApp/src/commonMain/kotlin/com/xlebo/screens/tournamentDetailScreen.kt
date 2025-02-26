package com.xlebo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant
import com.xlebo.navigation.SimpleNavController

@Composable
fun TournamentDetailScreen(
    navController: SimpleNavController,
    participants: List<Participant>,
) {
    LazyColumn {
        items(participants) { participant ->
            EditableText(participant.firstName)
        }
    }
}

@Composable
fun EditableText(text: String) {
    var editable by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(text) }
    var startingContent: String? by remember { mutableStateOf(null) }
    var color by remember { mutableStateOf(Color.Cyan) }
    val focus = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .focusRequester(focus)
            .onFocusChanged {
                color = if (it.hasFocus) {
                    Color.Green
                } else {
                    Color.Cyan
                }
            }
            .fillMaxWidth().height(50.dp)
            .background(color)
    ) {
        if (editable) {
            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.getOrNull(it.lastIndex)?.isWhitespace() == true &&
                        it.getOrNull(it.lastIndex) != ' '
                    ) {
                        editable = false
                    } else {
                        value = it
                    }
                },
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Escape) {
                        value = startingContent!!
                        startingContent = null
                        editable = false
                    }
                    true
                }
            )
            LaunchedEffect(Unit) {
                startingContent = value
                focus.requestFocus()
            }
        } else {
            Text(
                modifier = Modifier.clickable { editable = true },
                text = value
            )
        }
    }
}

