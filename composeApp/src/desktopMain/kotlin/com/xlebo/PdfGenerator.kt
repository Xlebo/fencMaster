package com.xlebo

import com.xlebo.model.Participant
import com.xlebo.pdfUtils.GroupsGenerator
import com.xlebo.pdfUtils.PlayOffGenerator
import com.xlebo.viewModel.PdfExportHandler
import com.xlebo.viewModel.TournamentState
import kotlin.io.path.Path

data class PlayOffMatch(val participant1: Participant?, val participant2: Participant?)


/**
 * just storing useful links in the meantime
 * first plan is to create ready to print PDFs from templates, either .pdf or code templates
 * backup plan is using an excel document to create table and export it manually/programmatically to pdf
 * https://github.com/vandeseer/easytable
 */
class PdfGenerator : PdfExportHandler {
    private val workingDirectory = Path("${System.getProperty("user.home")}/fencMaster")

    override fun createGroupsPdf(tournamentState: TournamentState) {
        val folder = tournamentState.name.replace(" ", "_")
        GroupsGenerator.createGroupsPdf(tournamentState, "$workingDirectory/$folder/${folder}_Groups.pdf")
    }

    override fun createPlayOff(name: String, participants: List<Participant>, folder: String) {
        PlayOffGenerator.generatePlayoffBracket(name, participants, "$workingDirectory/$folder/${folder}_PlayOff.pdf")
    }

}