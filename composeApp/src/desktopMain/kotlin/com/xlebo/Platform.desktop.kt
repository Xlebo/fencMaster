package com.xlebo

import androidx.compose.ui.awt.ComposeWindow
import com.xlebo.model.Participant
import net.codinux.csv.reader.CsvReader
import net.codinux.csv.reader.read
import java.awt.FileDialog
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"

    override fun handleFileSelection(): FilePath? {
        val dialog = FileDialog(ComposeWindow())
        dialog.file = "*.csv"
        dialog.directory = "C://"
        dialog.isVisible = true
        val filename = dialog.file
        return if (filename != null) {
            FilePath(
                path = dialog.directory,
                name = filename
            ).also {
                println("Selected: ${it.path} / ${it.name}")
            }
        } else {
            null
        }
    }

    override fun handleParticipantsImport(file: FilePath): List<Participant> {
        val csv = File(file.path + file.name)
        val headers = Participant.getHeaders()
        return CsvReader(hasHeaderRow = true)
            .read(csv)
            .map { row ->
                Participant(
                    row[headers[0]].toIntOrNull(),
                    row[headers[1]],
                    row[headers[2]],
                    row[headers[3]],
                    row[headers[4]],
                    row[headers[5]],
                    null
                )
            }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()