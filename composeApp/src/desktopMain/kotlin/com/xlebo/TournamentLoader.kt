package com.xlebo

import com.xlebo.viewModel.PersistenceHandler
import com.xlebo.viewModel.TournamentState
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class TournamentLoader : PersistenceHandler {
    private val workingDirectory = Path("${System.getProperty("user.home")}/fencMaster")
    val json = Json {
        prettyPrint = true
        allowStructuredMapKeys = true
    }

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

    override fun saveTournamentState(uiState: TournamentState) {
        val dirName = uiState.name.replace(' ', '_')

        val tournamentDir = getTournamentDirectory(dirName)

        val metadataFile = File(tournamentDir, "metadata.json")
        if (!metadataFile.exists()) {
            Napier.i { "Creating metadata file ${metadataFile.absolutePath}" }
            metadataFile.createNewFile()
        }

        metadataFile.writeText(json.encodeToString(TournamentState.serializer(), uiState))
    }

    override fun loadTournamentState(fileName: String): TournamentState {
        val metadataFile = File("$workingDirectory/$fileName", "metadata.json")

        return json.decodeFromString(TournamentState.serializer(), metadataFile.readText())
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