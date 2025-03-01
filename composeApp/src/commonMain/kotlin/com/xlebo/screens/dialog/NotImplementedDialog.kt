package com.xlebo.screens.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NotImplementedDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = { Text("Not Implemented Yet") },
        text = { Text("Sucks, huh?") },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest ) {
                Text("Dismiss")
            }
        }
    )
}