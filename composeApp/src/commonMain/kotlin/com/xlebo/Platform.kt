package com.xlebo

interface Platform {
    val name: String

    fun handleFileSelection(): String
}

expect fun getPlatform(): Platform