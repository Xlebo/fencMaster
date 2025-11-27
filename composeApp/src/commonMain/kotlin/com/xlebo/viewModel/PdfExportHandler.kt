package com.xlebo.viewModel

import com.xlebo.model.Participant

interface PdfExportHandler {
    fun createGroupsPdf(tournamentState: TournamentState)

    fun createPlayOffGraph(name: String, participants: List<Participant>, folder: String)

    fun createPlayOffOrder(name: String, participants: List<Participant>, folder: String)
}