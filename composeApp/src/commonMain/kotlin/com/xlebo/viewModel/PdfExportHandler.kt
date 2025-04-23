package com.xlebo.viewModel

import com.xlebo.model.Participant

interface PdfExportHandler {
    fun createGroupsPdf(tournamentState: TournamentState)

    fun createPlayOff(name: String, participants: List<Participant>, folder: String)
}