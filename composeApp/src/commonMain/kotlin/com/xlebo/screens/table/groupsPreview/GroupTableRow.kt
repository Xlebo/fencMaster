package com.xlebo.screens.table.groupsPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xlebo.model.Participant
import com.xlebo.utils.groupPreviewTableCell

@Composable
fun GroupTableRow(
    participant: Participant,
    weights: List<Float>,
    color: Color,
    onSwap: (Participant) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color)
            .border(1.dp, Color.Black)
    ) {
        Text(
            "${participant.firstName} ${participant.lastName}",
            modifier = Modifier.groupPreviewTableCell().fillMaxWidth(weights[0]),
            maxLines = 1,
            fontSize = 12.sp
        )
        Text(
            "${participant.club}",
            modifier = Modifier.groupPreviewTableCell().fillMaxWidth(weights[1]),
            maxLines = 1,
            fontSize = 12.sp
        )
        Text(
            "${participant.rank}", modifier = Modifier.groupPreviewTableCell().fillMaxWidth(weights[2]),
            fontSize = 12.sp
        )
        OutlinedButton(
            modifier = Modifier.size(30.dp).align(Alignment.CenterVertically).padding(3.dp),
            onClick = {
                onSwap(participant)
            },
            shape = CircleShape,
            contentPadding = PaddingValues(5.dp),
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Swap participants in group",
                tint = Color.Blue
            )
        }
    }

}