package com.xlebo.screens.table.groupsInProgress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xlebo.model.Participant
import com.xlebo.utils.defaultButton
import com.xlebo.utils.tournamentDetailTableCell
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

typealias Match = Pair<Participant, Participant>

@Composable
fun GroupInProgressTable(
    groupNo: Int,
    maxVal: Int
) {

    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val participants = uiState.participants.filter { it.group == groupNo }.sortedBy { it.order }

    val groupResults = uiState.groupsResults[groupNo]!!
    val isLocked = mutableStateOf(groupResults.locked)

    if (!viewModel.uiState.value.matchOrders.containsKey(groupNo))
        viewModel.generateGroupOrder(groupResults.results.keys.toList(), groupNo)
    val matchesOrder = viewModel.uiState.value.matchOrders[groupNo]!!

    Row {
        Text("Group $groupNo")
    }

    Row {
        Column {
            // 90° rotated header with names
            Row(modifier = Modifier) {
                Spacer(modifier = Modifier.width(298.dp))
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

            // actual table rows
            participants.forEachIndexed { iFirst, p ->
                Row(Modifier.height(40.dp)) {

                    // player info cells of row
                    Text(
                        p.lang ?: "",
                        modifier = Modifier.tournamentDetailTableCell().width(50.dp),
                        maxLines = 1
                    )
                    Text(
                        p.firstName,
                        modifier = Modifier.tournamentDetailTableCell().width(80.dp),
                        maxLines = 1
                    )
                    Text(
                        p.lastName,
                        modifier = Modifier.tournamentDetailTableCell().width(120.dp),
                        maxLines = 1
                    )

//            Text(
//                p.club ?: "",
//                modifier = Modifier.tournamentDetailTableCell().width(100.dp),
//                maxLines = 1
//            )

                    // table data
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
                                        locked = isLocked,
                                        value = groupResults.results[p to p2]?.first ?: "",
                                        maxVal = maxVal,
                                    ) {
                                        viewModel.updateResultForGroup(
                                            groupNo,
                                            p to p2,
                                            it to groupResults.results[p to p2]!!.second
                                        )
                                    }
                                    Text(":", modifier = Modifier.width(4.dp))
                                    ScoreTextField(
                                        locked = isLocked,
                                        value = groupResults.results[p to p2]?.second ?: "",
                                        maxVal = maxVal
                                    ) {
                                        viewModel.updateResultForGroup(
                                            groupNo,
                                            p to p2,
                                            groupResults.results[p to p2]!!.first to it
                                        )
                                    }
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
                                        locked = isLocked,
                                        value = groupResults.results[p2 to p]?.second ?: "",
                                        maxVal = maxVal
                                    ) {
                                        viewModel.updateResultForGroup(
                                            groupNo,
                                            p2 to p,
                                            groupResults.results[p2 to p]!!.first to it
                                        )
                                    }
                                    Text(":", modifier = Modifier.width(4.dp))
                                    ScoreTextField(
                                        locked = isLocked,
                                        value = groupResults.results[p2 to p]?.first ?: "",
                                        maxVal = maxVal,
                                    ) {
                                        viewModel.updateResultForGroup(
                                            groupNo,
                                            p2 to p,
                                            it to groupResults.results[p2 to p]!!.second
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.width(20.dp))

        Column {
            Spacer(modifier = Modifier.height(120.dp))
            matchesOrder.filterIndexed { index, _ -> index <= matchesOrder.size / 2 }.forEach {
                Text(
                    "${it.first.lastName} ${it.first.firstName.first()}. - " +
                            "${it.second.lastName} ${it.second.firstName.first()}."
                )
            }
        }

        Spacer(Modifier.width(20.dp))

        Column {
            Spacer(modifier = Modifier.height(120.dp))
            matchesOrder.filterIndexed { index, _ -> index > matchesOrder.size / 2 }.forEach {
                Text(
                    "${it.first.lastName} ${it.first.firstName.first()}. - " +
                            "${it.second.lastName} ${it.second.firstName.first()}."
                )
            }
        }
    }
    Row {
        if (isLocked.value) {
            Button(
                modifier = Modifier.defaultButton(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = {
                    viewModel.updateGroupResults(groupResults.copy(locked = false))
                }
            ) { Text("Unlock Table") }
        } else {
            Button(
                modifier = Modifier.defaultButton(),
                onClick = {
                    viewModel.updateGroupResults(groupResults.copy(locked = true))
                },
            ) { Text("Lock Table") }
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
    locked: MutableState<Boolean>, value: String, maxVal: Int, update: (String) -> Unit
) = BasicTextField(
    modifier = Modifier.width(23.dp).fillMaxHeight()
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
    enabled = !locked.value
)
