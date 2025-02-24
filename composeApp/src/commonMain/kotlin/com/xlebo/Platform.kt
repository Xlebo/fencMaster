package com.xlebo

import com.xlebo.model.Participant

interface Platform {
    val name: String

    fun handleFileSelection(): FilePath?

    fun handleParticipantsImport(file: FilePath): List<Participant>
}

expect fun getPlatform(): Platform