package com.xlebo.screens

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xlebo.model.Participant
import com.xlebo.navigation.SimpleNavController
import eu.wewox.lazytable.LazyTable

@Composable
fun TournamentDetailScreen(
    navController: SimpleNavController,
    participants: List<Participant>,
) {
//    SystemFileSystem
    Column {
        Text("Bonjour")
    }
}