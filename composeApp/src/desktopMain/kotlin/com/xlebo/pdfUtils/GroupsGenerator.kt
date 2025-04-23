package com.xlebo.pdfUtils

import com.xlebo.createEmptyCell
import com.xlebo.createEmptySpacingCell
import com.xlebo.createRotatedHeaderCell
import com.xlebo.createTextCell
import com.xlebo.model.Participant
import com.xlebo.screens.table.groupsInProgress.Match
import com.xlebo.viewModel.TournamentState
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import java.io.File

object GroupsGenerator {
    val PADDING = 20f

    fun createGroupsPdf(tournamentState: TournamentState, path: String) {
        PDDocument().use { document ->
            tournamentState.participants.groupBy { it.group }
                .map { it.key to it.value }
                .sortedBy { it.first }
                .forEach { (number, participants) ->
                    val page = PDPage(PDRectangle.A4)
                    document.addPage(page)
                    addGroupPage(
                        number!!,
                        participants.sortedBy { it.order },
                        tournamentState.matchOrders[number]!!,
                        document,
                        page
                    )
                }
            document.save(File(path))
        }

    }

    private fun addGroupPage(
        groupNumber: Int,
        participants: List<Participant>,
        order: List<Match>,
        document: PDDocument,
        page: PDPage
    ) {
        PDPageContentStream(document, page).use { contentStream ->
            contentStream.beginText()
            contentStream.newLineAtOffset(PADDING, page.mediaBox.height - PADDING - 24)
            contentStream.setFont(
                PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")),
                24f
            )
            contentStream.showText("Group $groupNumber")
            contentStream.endText()

            TableDrawer.builder()
                .page(page)
                .contentStream(contentStream)
                .table(createTable(participants, document))
                .startX(PADDING)
                .startY(page.mediaBox.height - PADDING)
                .endY(PADDING)
                .build()
                .draw({ document }, { PDPage(PDRectangle.A4) }, PADDING)

            var tableHeight = PADDING + 90 + 30 * participants.size + 30
            order.forEach {
                contentStream.beginText()
                contentStream.setFont(
                    PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")),
                    12f
                )
                contentStream.newLineAtOffset(PADDING, page.mediaBox.height - tableHeight)
                contentStream.showText(
                    "${it.first.lastName} ${it.first.firstName.first()}. - " +
                            "${it.second.lastName} ${it.second.firstName.first()}."
                )
                tableHeight += 20
                contentStream.endText()
            }
        }
    }

    private fun createTable(participants: List<Participant>, document: PDDocument): Table {
        val tableBuilder = Table.builder()
            .addColumnsOfWidth(75f, 90f)
            .addColumnsOfWidth(*(FloatArray(participants.size) { 45f }))
            .addColumnsOfWidth(80f)
            .padding(2f)
            //FIXME: The time bomb
            //https://issues.apache.org/jira/browse/PDFBOX-3504
            //I'll probably have to have the font inside the project?
            .font(PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")))

        val header = Row.builder().height(90f)
            .add(createEmptySpacingCell())
            .add(createEmptySpacingCell())
        participants.forEach { participant ->
            header.add(createRotatedHeaderCell(participant.lastName))
        }
        header.add(createEmptySpacingCell())
        tableBuilder.addRow(header.build())

        participants.forEach { participant ->
            val row = Row.builder().height(30f)
                .add(createTextCell(participant.firstName))
                .add(createTextCell(participant.lastName))
            participants.forEach { _ -> row.add(createEmptyCell()) }
            row.add(createEmptyCell())
            tableBuilder.addRow(row.build())
        }

        return tableBuilder.build()
    }

}