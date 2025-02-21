package com.xlebo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fencmaster.composeapp.generated.resources.Res
import fencmaster.composeapp.generated.resources.mec
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FencMaster",
        icon = painterResource(Res.drawable.mec)
    ) {
        App()
    }
}