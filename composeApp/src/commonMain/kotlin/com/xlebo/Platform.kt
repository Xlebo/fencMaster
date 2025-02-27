package com.xlebo

import com.xlebo.model.Participant

interface Platform {
    val name: String

    fun handleFileSelection(): String?

    fun handleParticipantsImport(file: String?): List<Participant>
}

expect fun getPlatform(): Platform