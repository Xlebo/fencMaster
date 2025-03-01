package com.xlebo.screens.table

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun TableCell(
    text: String,
    focus: FocusRequester,
    weight: Float,
    isNumber: Boolean = false
) {
    var value by remember { mutableStateOf(text) }
    var startingValue: String? by remember { mutableStateOf(value) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = Modifier
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    value = startingValue!!
                    focus.freeFocus()
                    focusManager.clearFocus()
                }
                if (it.key == Key.Enter) {
                    value = value.trim()
                    startingValue = value
                    focus.freeFocus()
                    focusManager.clearFocus()
                }
                true
            }
            .border(1.dp, Color.Black)
            .padding(8.dp)
            .focusRequester(focus)
            .fillMaxWidth(weight)
        ,
        value = value,
        onValueChange = {
            if (isNumber) {
                val num = it.toIntOrNull()
                if (num != null || it.isEmpty()) {
                    value = it
                }
            } else {
                value = it
            }
            focus.captureFocus()
        },
        singleLine = true
    )
}