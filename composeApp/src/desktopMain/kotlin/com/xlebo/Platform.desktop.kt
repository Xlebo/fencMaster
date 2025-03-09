package com.xlebo

import androidx.compose.ui.awt.ComposeWindow
import com.xlebo.model.Participant
import io.github.aakira.napier.Napier
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
            Napier.d("Selected csv: $it")
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
            .mapIndexed { index, row ->
                Participant(
                    index + 1,
                    row[headers[1]].trim().toIntOrNull(),
                    row[headers[2]].trim(),
                    row[headers[3]].trim(),
                    row[headers[4]].trim(),
                    row[headers[5]].trim(),
                    row[headers[6]].trim(),
                    null
                )
            }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()