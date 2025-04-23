package com.xlebo.pdfUtils

import com.xlebo.model.Participant
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import java.io.File
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.min
import kotlin.math.pow

object PlayOffGenerator {
    private val pageWidth = PDRectangle.A4.height // Swapped height and width for landscape
    private val pageHeight = PDRectangle.A4.width
    private val margin = 20f

    // Reduced size by 30-40%
    private val nameLineLength = 100f

    private val maxMatchesPerPage = 8 // Maximum number of first-round matches per page
    private val maxRoundsPerPage = 5 // Show 4 rounds per page

    /**
     * Generates a playoff bracket PDF based on the list of participants.
     * Handles any number of participants by adding preliminary rounds if needed.
     *
     * @param participants List of tournament participants
     * @param outputPath Path where the PDF file will be saved
     */
    fun generatePlayoffBracket(name: String, participants: List<Participant>, outputPath: String) {
        // Sort participants by their playOffOrder
        val sortedParticipants = participants.sortedBy { it.playOffOrder }
        val participantCount = participants.size

        // Get the previous power of 2 (to use as base for bracket)
        val previousPowerOfTwo = if (participantCount <= 2) participantCount else {
            2.0.pow(floor(log2(participantCount.toDouble()))).toInt()
        }

        // Calculate how many matches we need in preliminary round
        // We need to accommodate (participantCount - previousPowerOfTwo) participants
        // Each preliminary match produces 1 winner, so we need that many matches
        val preliminaryMatchesCount = if (participantCount > previousPowerOfTwo) {
            participantCount - previousPowerOfTwo
        } else {
            0
        }

        val hasPreliminaries = preliminaryMatchesCount > 0

        // For bracket creation, we need a certain number of slots in the first proper round
        // This equals previousPowerOfTwo - preliminaryMatchesCount + preliminaryMatchesCount (winners)
        val firstRoundSlots = previousPowerOfTwo

        // Create the seeded arrangement with or without preliminary rounds
        val bracket = createBracketWithPreliminaryRounds(sortedParticipants, preliminaryMatchesCount)

        // Calculate how many pages we need
        val firstRoundMatches = firstRoundSlots / 2
        val totalPages = (firstRoundMatches + maxMatchesPerPage - 1) / maxMatchesPerPage

        PDDocument().use { document ->
            val font = PDType0Font.load(document, File("c:/windows/fonts/arial.ttf"))

            // Generate bracket pages
            for (pageIndex in 0 until totalPages) {
                val page = PDPage(PDRectangle(pageWidth, pageHeight)) // Use landscape orientation
                document.addPage(page)

                PDPageContentStream(document, page).use { contentStream ->
                    // Draw the title
                    contentStream.beginText()
                    contentStream.setFont(font, 16f)
                    contentStream.newLineAtOffset(600f, pageHeight - margin)
                    contentStream.showText("$name PlayOff - Page ${pageIndex + 1} of $totalPages")
                    contentStream.endText()

                    // Calculate which matches go on this page
                    val startMatchIndex = pageIndex * maxMatchesPerPage
                    val endMatchIndex = min((pageIndex + 1) * maxMatchesPerPage, firstRoundMatches)

                    // Draw the brackets for this page's matches
                    drawBracketsForPage(
                        contentStream,
                        bracket,
                        log2(firstRoundSlots.toDouble()).toInt(),
                        font,
                        startMatchIndex,
                        endMatchIndex,
                        hasPreliminaries
                    )
                }
            }

            document.save(outputPath)
        }
    }

    /**
     * Creates a bracket structure including preliminary rounds if necessary.
     * Returns a data structure that represents the entire bracket.
     */
    private fun createBracketWithPreliminaryRounds(
        sortedParticipants: List<Participant>,
        preliminaryMatchesCount: Int
    ): BracketStructure {
        val participantCount = sortedParticipants.size
        val mainBracketSize = participantCount - preliminaryMatchesCount

        // Generate perfect seeding indices for the main bracket (power-of-2)
        val seedIndices = generateSeededIndices(mainBracketSize)

        // Data structure to hold our bracket information
        val bracketStructure = BracketStructure(mainBracketSize)

        // Handle the case with preliminary matches
        if (preliminaryMatchesCount > 0) {
            // We need to determine which positions in the main bracket will have preliminary matches
            // These should be the weakest seeds that need to compete for spots

            // First, assign the top seeds directly to the main bracket
            val directQualifierCount = mainBracketSize - preliminaryMatchesCount
            for (i in 0 until mainBracketSize) {
                if (seedIndices[i] < directQualifierCount)
                    bracketStructure.setParticipant(i, sortedParticipants[seedIndices[i]])
            }

            // Then, assign the remaining participants to preliminary matches
            // These participants will compete for the remaining spots in the main bracket
            var prelimParticipantIndex = directQualifierCount

            for (i in 1 .. preliminaryMatchesCount) {
                val mainBracketPos = seedIndices.indexOf(mainBracketSize - preliminaryMatchesCount + i - 1)

                // Create preliminary match between two participants
                val participant1 = if (prelimParticipantIndex < participantCount)
                    sortedParticipants[prelimParticipantIndex++] else null

                val participant2 = if (prelimParticipantIndex < participantCount)
                    sortedParticipants[prelimParticipantIndex++] else null

                // Add the preliminary match at this position in the main bracket
                if (participant1 != null && participant2 != null) {
                    bracketStructure.addPreliminaryMatch(
                        mainBracketPos,
                        participant1,
                        participant2
                    )
                }
            }
        } else {
            // No preliminary matches needed, just fill in the bracket normally
            for (i in seedIndices.indices) {
                if (i < sortedParticipants.size) {
                    bracketStructure.setParticipant(seedIndices[i], sortedParticipants[i])
                }
            }
        }

        return bracketStructure
    }

    /**
     * Generates the indices for a standard tournament seeding.
     * For an 8-player tournament, this creates the following order:
     * [0, 7, 3, 4, 1, 6, 2, 5] which corresponds to the seeding order
     * [1, 8, 4, 5, 2, 7, 3, 6]
     */
    private fun generateSeededIndices(size: Int): List<Int> {
        val stack = ArrayDeque<List<Int>>()
        stack.add(listOf(0))

        var currentSize = 1
        while (currentSize * 2 <= size) {
            currentSize *= 2
            val previous = stack.last()
            val next = mutableListOf<Int>()
            for (index in previous) {
                next.add(index)
                next.add(currentSize - 1 - index)
            }
            stack.add(next)
        }

        return stack.last()
    }

    /**
     * Draws the tournament brackets for a specific page of matches
     */
    private fun drawBracketsForPage(
        contentStream: PDPageContentStream,
        bracket: BracketStructure,
        totalRounds: Int,
        font: PDType0Font,
        startMatchIndex: Int,
        endMatchIndex: Int,
        hasPreliminaries: Boolean
    ) {
        // Calculate how many matches on this page
        val matchesOnPage = endMatchIndex - startMatchIndex

        // Calculate vertical spacing based on matches on this page
        val verticalSpacing = (pageHeight - 2 * margin) / (matchesOnPage + 0.5f)

        // Draw preliminary round first if needed
        if (hasPreliminaries) {
            for (i in 0 until matchesOnPage) {
                val matchPos = startMatchIndex + i
                val topPos = matchPos * 2
                val bottomPos = matchPos * 2 + 1

                val yPos = pageHeight - margin - (i + 0.5f) * verticalSpacing
                val yTop = yPos + verticalSpacing/4
                val yBottom = yPos - verticalSpacing/4

                // If either position has a preliminary match, draw it
                if (bracket.hasPreliminaryMatch(topPos)) {
                    drawPreliminaryMatch(
                        contentStream,
                        margin,
                        yBottom,
                        bracket.getPreliminaryMatch(topPos)!!,
                        font
                    )
                }

                if (bracket.hasPreliminaryMatch(bottomPos)) {
                    drawPreliminaryMatch(
                        contentStream,
                        margin,
                        yTop,
                        bracket.getPreliminaryMatch(bottomPos)!!,
                        font
                    )
                }
            }
        }

        // For each round, draw the brackets
        for (round in 0 until min(totalRounds, maxRoundsPerPage)) {
            val matchesInRound = matchesOnPage / (1 shl round)
            if (matchesInRound <= 0) break

            // X position is shifted right by nameLineLength if we have preliminaries
            val xOffset = if (hasPreliminaries) nameLineLength else 0f
            val xStart = margin + xOffset + round * nameLineLength
            val matchSpacingForRound = verticalSpacing * (1 shl round)

            for (i in 0 until matchesInRound) {
                val yPos = pageHeight - margin - (i + 0.5f) * matchSpacingForRound

                if (round == 0) {
                    // First round - draw participant names
                    val topPos = (startMatchIndex + i) * 2
                    val bottomPos = (startMatchIndex + i) * 2 + 1

                    // Draw participant names and lines
                    val yTop = yPos + matchSpacingForRound/4
                    val yBottom = yPos - matchSpacingForRound/4

                    // Top participant
                    val topParticipant = bracket.getParticipant(topPos)
                    if (topParticipant != null) {
                        drawParticipantName(contentStream, xStart, yBottom, topParticipant, font)
                    } else if (bracket.hasPreliminaryMatch(topPos)) {
                        drawWinnerPlaceholder(contentStream, xStart, yBottom, font)
                    }

                    // Bottom participant
                    val bottomParticipant = bracket.getParticipant(bottomPos)
                    if (bottomParticipant != null) {
                        drawParticipantName(contentStream, xStart, yTop, bottomParticipant, font)
                    } else if (bracket.hasPreliminaryMatch(bottomPos)) {
                        drawWinnerPlaceholder(contentStream, xStart, yTop, font)
                    }

                    // Draw connector from first round to second round
                    drawConnector(
                        contentStream,
                        xStart,                      // X start
                        xStart + nameLineLength * 2, // X end
                        yBottom - 3,                 // Y top line
                        yTop - 3,                    // Y bottom line
                        yPos - 3                     // Y center point
                    )
                } else {
                    // For subsequent rounds
                    val nextRoundY = yPos - 3
                    val prevRoundTopY = yPos - matchSpacingForRound/4 - 3
                    val prevRoundBottomY = yPos + matchSpacingForRound/4 - 3

                    // Draw connector for this round
                    drawConnector(
                        contentStream,
                        xStart,                      // X start
                        xStart + nameLineLength * 2, // X end
                        prevRoundTopY,               // Y top incoming
                        prevRoundBottomY,            // Y bottom incoming
                        nextRoundY                   // Y center point for this round
                    )
                }
            }
        }
    }

    /**
     * Draws a preliminary round match
     */
    private fun drawPreliminaryMatch(
        contentStream: PDPageContentStream,
        x: Float,
        y: Float,
        match: PreliminaryMatch,
        font: PDType0Font
    ) {
        // Draw top participant
        if (match.participant1 != null) {
            drawParticipantName(contentStream, x, y + 10, match.participant1, font)
        }

        // Draw bottom participant
        if (match.participant2 != null) {
            drawParticipantName(contentStream, x, y - 10, match.participant2, font)
        }

        // Draw connector to main bracket
        contentStream.setLineWidth(1f)

        // Vertical line connecting the two participants
        contentStream.moveTo(x + nameLineLength, y + 7)
        contentStream.lineTo(x + nameLineLength, y - 13)
        contentStream.stroke()
    }

    /**
     * Draws a placeholder for the winner of a preliminary match
     */
    private fun drawWinnerPlaceholder(
        contentStream: PDPageContentStream,
        x: Float,
        y: Float,
        font: PDType0Font
    ) {
        // Draw the placeholder text
        contentStream.beginText()
        contentStream.setFont(font, 10f)
        contentStream.newLineAtOffset(x + 5, y)
//        contentStream.showText("Winner of prelim.")
        contentStream.endText()

        // Draw underline
        contentStream.setLineWidth(0.5f)
        contentStream.moveTo(x, y - 3)
        contentStream.lineTo(x + nameLineLength, y - 3)
        contentStream.stroke()
    }

    /**
     * Draws a connector in the proper bracket shape
     */
    private fun drawConnector(
        contentStream: PDPageContentStream,
        xStart: Float,
        xEnd: Float,
        yTop: Float,
        yBottom: Float,
        yCenter: Float
    ) {
        contentStream.setLineWidth(1f)

        // Draw vertical line connecting the two matches
        contentStream.moveTo(xStart + (xEnd - xStart) / 2, yTop)
        contentStream.lineTo(xStart + (xEnd - xStart) / 2, yBottom)
        contentStream.stroke()

        // Draw horizontal line to next round
        contentStream.moveTo(xStart + (xEnd - xStart) / 2, yCenter)
        contentStream.lineTo(xEnd, yCenter)
        contentStream.stroke()
    }

    /**
     * Draws a participant name with an underline
     */
    private fun drawParticipantName(
        contentStream: PDPageContentStream,
        x: Float,
        y: Float,
        participant: Participant,
        font: PDType0Font
    ) {
        // Draw the name text
        contentStream.beginText()
        contentStream.setFont(font, 10f)
        contentStream.newLineAtOffset(x + 5, y)
        contentStream.showText("${participant.playOffOrder}. ${participant.lastName}")
        contentStream.endText()

        // Draw underline - making sure it has correct y position
        contentStream.setLineWidth(0.5f)
        contentStream.moveTo(x, y - 3)
        contentStream.lineTo(x + nameLineLength, y - 3)
        contentStream.stroke()
    }

    /**
     * Data class to represent a preliminary match
     */
    data class PreliminaryMatch(
        val position: Int,
        val participant1: Participant?,
        val participant2: Participant?
    )

    /**
     * Class to hold the bracket structure with participants and preliminary matches
     */
    class BracketStructure(size: Int) {
        private val participants = arrayOfNulls<Participant>(size)
        private val preliminaryMatches = mutableMapOf<Int, PreliminaryMatch>()

        fun setParticipant(position: Int, participant: Participant) {
            participants[position] = participant
        }

        fun getParticipant(position: Int): Participant? {
            return if (position < participants.size) participants[position] else null
        }

        fun addPreliminaryMatch(
            position: Int,
            participant1: Participant?,
            participant2: Participant?
        ) {
            preliminaryMatches[position] = PreliminaryMatch(position, participant1, participant2)
        }

        fun hasPreliminaryMatch(position: Int): Boolean {
            return preliminaryMatches.containsKey(position)
        }

        fun getPreliminaryMatch(position: Int): PreliminaryMatch? {
            return preliminaryMatches[position]
        }
    }
}