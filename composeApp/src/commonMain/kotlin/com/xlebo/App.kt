package com.xlebo

import CreateTournament
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.xlebo.navigation.Screen
import com.xlebo.navigation.SimpleNavController
import com.xlebo.screens.HomeScreen
import com.xlebo.screens.Screen3

@Composable
fun App() {
    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val navController = remember { SimpleNavController(Screen.Home) }
            val currentScreen by navController.currentScreen.collectAsState()

            val platform = getPlatform()

            when (currentScreen) {
                Screen.Home -> HomeScreen(navController = navController)
                Screen.Screen2 -> CreateTournament(navController = navController, platform = platform)
                Screen.Screen3 -> Screen3(navController = navController)
            }
        }
    }
}