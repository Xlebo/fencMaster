package com.xlebo

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"

    override fun handleFileSelection(): FilePath {
        val dialog: FileDialog = FileDialog(ComposeWindow())
        dialog.directory = "C://"
        dialog.isVisible = true
        val filename = dialog.file
        return FilePath(
            path = dialog.directory,
            name = filename
        ).also {
            println("Selected: ${it.path} / ${it.name}")
        }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()