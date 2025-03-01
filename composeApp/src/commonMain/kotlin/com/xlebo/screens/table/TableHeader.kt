package com.xlebo.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableHeader(values: List<String>, weights: List<Float>) {
    check(values.size == weights.size) { "no cap" }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
        ,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        values.forEachIndexed { index, header ->
            BasicTextField(
                onValueChange = {},
                value = header,
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .fillMaxWidth(weights[index])
                    .clickable(enabled = false) {},
                singleLine = true
            )
        }
    }
}