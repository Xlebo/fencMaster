package com.xlebo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xlebo.screens.table.participantsPreview.NewParticipantTableRow
import com.xlebo.screens.table.participantsPreview.ParticipantTableRow
import com.xlebo.screens.table.participantsPreview.TableHeader
import com.xlebo.utils.backButton
import com.xlebo.utils.defaultButton
import com.xlebo.utils.groupPreviewTableCell
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun PlayOffPreviewScreen(
    navController: NavHostController,
    lazyListScrollBar: @Composable ((Modifier, LazyListState) -> Unit)? = null,
) {
    var submitDialog by remember { mutableStateOf(false) }
    var alertDialog by remember { mutableStateOf(false) }

    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(15.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        modifier = Modifier.backButton().align(Alignment.CenterStart),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        onClick = {
                            navController.navigate(Screen.Home)
                            viewModel.saveData()
                            viewModel.reset()
                        }) { Text("Home") }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${uiState.name} PlayOff Preview",
                        fontSize = 24.sp
                    )
                }
                Spacer(Modifier)
            }

            item {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(modifier = Modifier.groupPreviewTableCell().width(50.dp), text = "Poradi")

                    Text(modifier = Modifier.groupPreviewTableCell().width(50.dp), text = "Win%")
                    Text(modifier = Modifier.groupPreviewTableCell().width(50.dp), text = "Points")
                    Text(modifier = Modifier.groupPreviewTableCell().width(50.dp), text = "Hits")

                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(200.dp),
                        text = "Priezvisko"
                    )
                    Text(modifier = Modifier.groupPreviewTableCell().width(150.dp), text = "Meno")
                    Text(modifier = Modifier.groupPreviewTableCell().width(80.dp), text = "Jazyk")
                }
            }

            items(uiState.participants) { participant ->
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(50.dp),
                        text = participant.playOffOrder.toString()
                    )

                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(50.dp),
                        text = (participant.groupStatistics!!.wins / participant.groupStatistics.totalMatches).toPercents()
                    )
                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(50.dp),
                        text = participant.groupStatistics.hitsScored.toInt().toString()
                    )
                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(50.dp),
                        text = participant.groupStatistics.hitsReceived.toInt().toString()
                    )

                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(200.dp),
                        text = participant.lastName,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(150.dp),
                        text = participant.firstName,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.groupPreviewTableCell().width(80.dp),
                        text = participant.lang ?: "",
                        maxLines = 1
                    )
                }
            }
        }
        if (lazyListScrollBar != null) {
            lazyListScrollBar(Modifier.align(Alignment.CenterEnd).fillMaxHeight(), scrollState)
        }
    }
}

fun Float.toPercents(): String {
    val new = (this * 1000).roundToInt()
    if (new % 10 == 0) {
        return (new / 10).toString()
    }
    return (new / 10f).toString()
}