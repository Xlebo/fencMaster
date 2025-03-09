package com.xlebo.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val BUTTON_PADDING = 10.dp

fun Modifier.defaultButton() = this.size(width = 200.dp, height = 60.dp).padding(BUTTON_PADDING)

fun Modifier.backButton() = this.size(width = 100.dp, height = 60.dp).padding(BUTTON_PADDING)