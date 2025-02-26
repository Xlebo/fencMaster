package com.xlebo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xlebo.modifierUtils.defaultButton
import com.xlebo.navigation.Screen
import com.xlebo.navigation.SimpleNavController

@Composable
fun HomeScreen(
    navController: SimpleNavController,
) {
    Column {
        Text(
            text = "FencMaster",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Spacer(Modifier.padding(20.dp))
                Button(
                    modifier = Modifier.defaultButton().align(Alignment.CenterHorizontally),
                    onClick = { navController.navigateTo(Screen.CrateTournament) }
                ) { Text("Založ Turnaj", fontSize = 15.sp) }
            }
            Divider(modifier = Modifier.fillMaxHeight().width(1.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Otvor existujúci turnaj:",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.Start).padding(10.dp)
                )
            }
        }
    }
}