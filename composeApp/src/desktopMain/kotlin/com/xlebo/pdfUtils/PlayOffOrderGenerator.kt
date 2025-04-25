package com.xlebo.pdfUtils

import com.xlebo.createEmptyCell
import com.xlebo.createEmptySpacingCell
import com.xlebo.createRotatedHeaderCell
import com.xlebo.createTextCell
import com.xlebo.model.Participant
import com.xlebo.screens.toPercents
import com.xlebo.viewModel.TournamentState
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import java.awt.Color
import java.io.File

object PlayOffOrderGenerator {
    val PADDING = 20f

    fun createPlayOffOrderPdf(name: String, participants: List<Participant>, path: String) {
        PDDocument().use { document ->
            val page = PDPage(PDRectangle.A4)
            PDPageContentStream(document, page).use { contentStream ->
                contentStream.beginText()
                contentStream.newLineAtOffset(PADDING, page.mediaBox.height - PADDING - 24)
                contentStream.setFont(
                    PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")),
                    24f
                )
                contentStream.showText("$name Post-Group Stage Order")
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

                document.save(File(path))
            }

        }
    }

    private fun createTable(participants: List<Participant>, document: PDDocument): Table {
        val tableBuilder = Table.builder()
            .addColumnsOfWidth(
                40f, //order
                120f, //firstName
                200f, //lastName
                60f, //winrate
                60f, //scored
                60f, //received
            )
            .padding(2f)
            .font(PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")))
            .fontSize(12)

        val header = Row.builder().height(20f).backgroundColor(Color.GRAY)
            .add(createEmptySpacingCell())
            .add(createTextCell("First Name"))
            .add(createTextCell("Last Name"))
            .add(createTextCell("Winrate"))
            .add(createTextCell("Scored"))
            .add(createTextCell("Received"))
        tableBuilder.addRow(header.build())

        participants.forEach { participant ->
            val row = Row.builder().height(20f)
                .add(createTextCell(participant.playOffOrder.toString()))
                .add(createTextCell(participant.firstName))
                .add(createTextCell(participant.lastName))
                .add(createTextCell((participant.groupStatistics!!.wins / participant.groupStatistics.totalMatches).toPercents()))
                .add(createTextCell(participant.groupStatistics.hitsScored.toInt().toString()))
                .add(createTextCell(participant.groupStatistics.hitsReceived.toInt().toString()))
            tableBuilder.addRow(row.build())
        }

        return tableBuilder.build()
    }
}