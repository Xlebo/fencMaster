package com.xlebo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.xlebo.navigation.SimpleNavController

@Composable
fun Screen3(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: SimpleNavController,
){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text( text = "Screen 3", fontSize = 24.sp  )

            Button(
                onClick = { navController.navigateBack() }
            ){ Text("Go Back") }

        }
}