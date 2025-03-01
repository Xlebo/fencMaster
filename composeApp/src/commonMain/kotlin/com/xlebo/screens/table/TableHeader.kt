package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableHeader(values: List<String>, weights: List<Float>, spaceForRemoveButton: Boolean = true) {
    check(values.size == weights.size) { "no cap" }
    Row {
        values.forEachIndexed { index, header ->
            BasicTextField(
                modifier = Modifier
                    .weight(weights[index])
                    .background(Color.Yellow)
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable(enabled = false) {}
                ,
                onValueChange = {},
                value = header,
                singleLine = true
            )
        }
        if (spaceForRemoveButton) {
            BasicTextField(
                modifier = Modifier.fillMaxHeight().width(25.dp).background(Color.Transparent),
                value = "",
                onValueChange = {},
                enabled = false
            )
        }
    }
}