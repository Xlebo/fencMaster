package com.xlebo

interface Platform {
    val name: String

    fun handleFileSelection(): FilePath
}

expect fun getPlatform(): Platform