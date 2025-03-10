package com.xlebo

import androidx.compose.ui.awt.ComposeWindow
import com.xlebo.model.Participant
import net.codinux.csv.reader.CsvReader
import net.codinux.csv.reader.read
import java.awt.FileDialog
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"

    override fun handleFileSelection(): String? {
        val dialog = FileDialog(ComposeWindow())
        dialog.file = "*.csv"
        dialog.directory = "C://"
        dialog.isVisible = true
        val filename = dialog.file
        return if (filename != null) {
            dialog.directory + filename
        } else {
            null
        }.also {
            println("Selected: $it")
        }
    }

    override fun handleParticipantsImport(file: String?): List<Participant> {
        if (file == null) {
            return listOf()
        }
        val csv = File(file)
        val headers = Participant.getHeaders()
        return CsvReader(hasHeaderRow = true)
            .read(csv)
            .map { row ->
                Participant(
                    row[headers[0]].toInt(),
                    row[headers[1]].toIntOrNull(),
                    row[headers[2]],
                    row[headers[3]],
                    row[headers[4]],
                    row[headers[5]],
                    row[headers[6]],
                    null
                )
            }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()