package com.xlebo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                modifier = Modifier.fillMaxHeight().width(200.dp)
            ) {
                Spacer(Modifier.padding(20.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { navController.navigateTo(Screen.Screen2) }
                ) { Text("Založ Turnaj", fontSize = 15.sp) }
            }

            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
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