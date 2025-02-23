package com.xlebo

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"

    override fun handleFileSelection(): String {
        val fd = FileDialog(ComposeWindow())
        fd.directory = "C://"
        fd.isVisible = true
        val filename = fd.file
        println("Selected: $filename")
        return filename
    }
}

actual fun getPlatform(): Platform = JVMPlatform()