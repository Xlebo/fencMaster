package com.xlebo.screens.table.groupsInProgress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant
import com.xlebo.utils.createPairs
import com.xlebo.utils.tournamentDetailTableCell

typealias Match = Pair<Participant, Participant>
typealias ResultsMap = MutableMap<Match, Pair<String, String>>

@Composable
fun GroupInProgressTable(
    participants: List<Participant>, maxVal: Int, onSave: (ResultsMap) -> Unit
) {
    val results = remember {
        participants.flatMapIndexed { index, first ->
            participants.drop(index + 1).map { second -> (first to second) to ("" to "") }
        }.toMutableStateMap()
    }

    val matchOrder = createPairs(results.keys)

    Row {
        Text("Group ${participants[0].group}")
    }

    Row {
        Column(modifier = Modifier.fillMaxWidth(.5f)) {
            Row(modifier = Modifier) {
                Spacer(modifier = Modifier.width(378.dp))
                participants.forEach {
                    Box(
                        modifier = Modifier.height(120.dp).width(50.dp).border(1.dp, Color.Black)
                            .rotate(-90f), contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = it.lastName,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            modifier = Modifier.offset(x = (-30).dp)
                        )
                    }
                }
            }

            participants.forEachIndexed { iFirst, p ->
                Row(Modifier.height(40.dp)) {
                    Text(
                        p.lang ?: "",
                        modifier = Modifier.tournamentDetailTableCell().width(80.dp),
                        maxLines = 1
                    )
                    Text(
                        p.firstName,
                        modifier = Modifier.tournamentDetailTableCell().width(100.dp),
                        maxLines = 1
                    )
                    Text(
                        p.lastName,
                        modifier = Modifier.tournamentDetailTableCell().width(150.dp),
                        maxLines = 1
                    )

//            Text(
//                p.club ?: "",
//                modifier = Modifier.tournamentDetailTableCell().width(100.dp),
//                maxLines = 1
//            )

                    participants.forEachIndexed { iSecond, p2 ->
                        when {
                            iFirst == iSecond -> EmptyCell()
                            iFirst < iSecond -> {
                                Row(
                                    modifier = Modifier.fillMaxHeight().width(50.dp)
                                        .border(1.dp, Color.Black),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ScoreTextField(
                                        value = results[p to p2]?.first ?: "",
                                        maxVal = maxVal,
                                    ) { results[p to p2] = it to results[p to p2]!!.second }
                                    Text(":", modifier = Modifier.width(4.dp))
                                    ScoreTextField(
                                        value = results[p to p2]?.second ?: "", maxVal = maxVal
                                    ) { results[p to p2] = results[p to p2]!!.first to it }
                                }
                            }

                            iFirst > iSecond -> {
                                Row(
                                    modifier = Modifier.width(50.dp).fillMaxHeight()
                                        .border(1.dp, Color.Black),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ScoreTextField(
                                        value = results[p2 to p]?.second ?: "", maxVal = maxVal
                                    ) { results[p2 to p] = results[p2 to p]!!.first to it }
                                    Text(":", modifier = Modifier.width(4.dp))
                                    ScoreTextField(
                                        value = results[p2 to p]?.first ?: "",
                                        maxVal = maxVal,
                                    ) { results[p2 to p] = it to results[p2 to p]!!.second }
                                }
                            }
                        }
                    }
                }
            }
        }

        Column {
            Spacer(modifier = Modifier.height(120.dp))
            matchOrder.filterIndexed { index, _ -> index <= matchOrder.size / 2 }.forEach {
                Text(
                    "${it.first.lastName} vs. ${it.second.lastName}"
                )
            }
        }

        Spacer(Modifier.width(20.dp))

        Column {
            Spacer(modifier = Modifier.height(120.dp))
            matchOrder.filterIndexed { index, _ -> index > matchOrder.size / 2 }.forEach {
                Text(
                    "${it.first.lastName} vs. ${it.second.lastName}"
                )
            }
        }
    }
}

@Composable
fun EmptyCell() = Text(
    "",
    Modifier.background(Color.DarkGray).border(1.dp, Color.Black).background(Color.DarkGray)
        .width(50.dp).fillMaxHeight()
)

@Composable
fun ScoreTextField(
    value: String, maxVal: Int, update: (String) -> Unit
) = BasicTextField(
    modifier = Modifier.width(23.dp).fillMaxHeight().background(Color.LightGray)
        .padding(horizontal = 1.dp, vertical = 5.dp),
    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    value = value,
    onValueChange = {
        val num = it.toIntOrNull()
        if (num != null && num <= maxVal) {
            update(it)
        }
        if (it.uppercase() == "V" || it.isEmpty()) {
            update(it.uppercase())
        }

    },
    singleLine = true,
)
