package com.xlebo

import android.os.Build
import com.xlebo.model.Participant

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override fun handleFileSelection(): String {
        TODO("Not yet implemented")
    }

    override fun handleParticipantsImport(file: String?): List<Participant> {
        TODO("Not yet implemented")
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
