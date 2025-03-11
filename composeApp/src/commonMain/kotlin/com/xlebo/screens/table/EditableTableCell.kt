package com.xlebo.screens.table

import androidx.compose.foundation.focusable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import com.xlebo.utils.tournamentDetailTableCell

@Composable
fun TableCell(
    text: String,
    modifier: Modifier,
    isNumber: Boolean = false,
    enabled: Boolean = true,
    updateField: (String) -> Unit
) {
    var startingValue: String by remember { mutableStateOf(text) }
    val focusManager = LocalFocusManager.current

    BasicTextField(modifier = modifier.tournamentDetailTableCell().focusable(enabled)
        .onKeyEvent {
            if (it.key == Key.Escape) {
                updateField(startingValue)
                focusManager.clearFocus()
            }
            if (it.key == Key.Enter) {
                updateField(text.trim())
                startingValue = text.trim()
                focusManager.clearFocus()
            }
            if (it.key == Key.Tab) {
                updateField(text.trim())
                startingValue = text.trim()
                if (it.isShiftPressed) {
                    focusManager.moveFocus(FocusDirection.Previous)
                } else {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            }
            true
        },
        value = text,
        onValueChange = {
            if (isNumber) {
                val num = it.toIntOrNull()
                if (num != null || it.isEmpty()) {
                    updateField(it)
                }
            } else {
                updateField(it)
            }
        },
        singleLine = true,
        enabled = enabled,
    )
}