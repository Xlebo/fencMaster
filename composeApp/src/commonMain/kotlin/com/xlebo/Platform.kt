package com.xlebo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform