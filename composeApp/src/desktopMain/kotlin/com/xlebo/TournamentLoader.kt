package com.xlebo

import com.xlebo.viewModel.PersistenceHandler
import com.xlebo.viewModel.SharedUiState
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import java.io.File
import java.text.Normalizer
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class TournamentLoader : PersistenceHandler {
    private val workingDirectory = Path("${System.getProperty("user.home")}/fencMaster")

    init {
        Napier.i { "Initialized TournamentLoader at ${workingDirectory.absolutePathString()}" }
    }

    override fun getExistingTournaments(): List<String> {
        return if (workingDirectory.exists()) {
            workingDirectory.listDirectoryEntries().map { it.name }
        } else {
            listOf()
        }
    }

    override fun saveTournamentState(uiState: SharedUiState) {
        val dirName = Normalizer.normalize(uiState.name, Normalizer.Form.NFD)
            .lowercase(Locale.getDefault())
            .replace(' ', '_')

        val tournamentDir = getTournamentDirectory(dirName)

        val metadataFile = File(tournamentDir, "metadata.json")
        if (!metadataFile.exists()) {
            Napier.i { "Creating metadata file ${metadataFile.absolutePath}" }
            metadataFile.createNewFile()
        }

        metadataFile.writeText(Json.encodeToString(SharedUiState.serializer(), uiState))
    }

    override fun loadTournamentState(fileName: String): SharedUiState {
        val metadataFile = File("$workingDirectory/$fileName", "metadata.json")

        return Json.decodeFromString(SharedUiState.serializer(), metadataFile.readText())
    }

    private fun getTournamentDirectory(name: String): File {
        val dir = File("$workingDirectory/$name")
        if (!dir.exists()) {
            Napier.i { "Creating directory: ${dir.absolutePath}" }
            dir.mkdirs()
        }
        return dir
    }
}