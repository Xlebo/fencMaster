package com.xlebo.screens.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(title: String, text: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest ) {
                Text("Dismiss")
            }
        }
    )
}