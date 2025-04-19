package com.xlebo

import org.vandeseer.easytable.settings.HorizontalAlignment
import org.vandeseer.easytable.settings.VerticalAlignment
import org.vandeseer.easytable.structure.cell.AbstractCell
import org.vandeseer.easytable.structure.cell.TextCell
import org.vandeseer.easytable.structure.cell.VerticalTextCell

fun createEmptySpacingCell(): AbstractCell = TextCell.builder().minHeight(50f).text("").build()

fun createEmptyCell(): AbstractCell = TextCell.builder().text("").borderWidth(1f).build()

fun createTextCell(text: String): AbstractCell =
    TextCell.builder().text(text).borderWidth(1f).verticalAlignment(VerticalAlignment.MIDDLE)
        .build()

fun createRotatedHeaderCell(text: String): AbstractCell =
    VerticalTextCell.builder().text(text).borderWidth(1f)
        .horizontalAlignment(HorizontalAlignment.CENTER).build()