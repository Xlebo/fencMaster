package com.xlebo

import com.xlebo.model.Participant
import com.xlebo.viewModel.PdfExportHandler
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
import kotlin.io.path.Path


/**
 * just storing useful links in the meantime
 * first plan is to create ready to print PDFs from templates, either .pdf or code templates
 * backup plan is using an excel document to create table and export it manually/programmatically to pdf
 * https://github.com/vandeseer/easytable
 */
class PdfGenerator : PdfExportHandler {
    private val PADDING = 20f
    private val workingDirectory = Path("${System.getProperty("user.home")}/fencMaster")

    override fun createGroupsPdf(tournamentState: TournamentState) {
        val folder = tournamentState.name.replace(" ", "_")
        PDDocument().use { document ->
            tournamentState.participants.groupBy { it.group }.forEach { (number, participants) ->
                val page = PDPage(PDRectangle.A4)
                document.addPage(page)
                addGroupPage(
                    number!!,
                    participants.sortedBy { it.order },
//                    tournamentState.matchOrders[number]!!,
                    document,
                    page
                )
            }
            document.save(File("$workingDirectory/$folder/${folder}_Groups.pdf"))
        }

    }

    private fun addGroupPage(
        groupNumber: Int,
        participants: List<Participant>,
//        order: List<Match>,
        document: PDDocument,
        page: PDPage
    ) {
        PDPageContentStream(document, page).use { contentStream ->
            TableDrawer.builder()
                .page(page)
                .contentStream(contentStream)
                .table(createTable(participants, document))
                .startX(PADDING)
                .startY(page.mediaBox.height - PADDING)
                .endY(PADDING)
                .build()
                .draw({ document }, { PDPage(PDRectangle.A4) }, PADDING)
        }
    }

    private fun createTable(participants: List<Participant>, document: PDDocument): Table {
        val tableBuilder = Table.builder()
            .addColumnsOfWidth(40f, 75f, 90f)
            .addColumnsOfWidth(*(FloatArray(participants.size) { 50f }))
            .padding(2f)
            //FIXME: The time bomb
            //https://issues.apache.org/jira/browse/PDFBOX-3504
            //I'll probably have to have the font inside the project?
            .font(PDType0Font.load(document, File("c:/windows/fonts/arial.ttf")))

        val header = Row.builder().height(90f)
            .add(createEmptySpacingCell())
            .add(createEmptySpacingCell())
            .add(createEmptySpacingCell())
        participants.forEach { participant ->
            header.add(createRotatedHeaderCell(participant.lastName))
        }
        tableBuilder.addRow(header.build())

        participants.forEach { participant ->
            val row = Row.builder().height(30f)
                .add(createTextCell(participant.lang ?: ""))
                .add(createTextCell(participant.firstName))
                .add(createTextCell(participant.lastName))

            participants.forEach { _ -> row.add(createEmptyCell()) }
            tableBuilder.addRow(row.build())
        }

        return tableBuilder.build()
    }

}