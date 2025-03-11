package com.xlebo.screens.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun SubmitDialog(onContinueRequest: () -> Unit, onBackRequest: () -> Unit) {
    AlertDialog(
        title = { Text("Do you wish to proceed?") },
        text = { Text("You won't be able to return to the previous screen after submitting this action.") },
        onDismissRequest = onBackRequest,
        dismissButton = {
            TextButton(onClick = onBackRequest) {
                Text("Back")
            }
        },
        confirmButton = {
            TextButton(onClick = onContinueRequest) {
                Text("Continue")
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.fillMaxWidth(.5f)
    )
}