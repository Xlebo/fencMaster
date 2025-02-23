package com.xlebo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xlebo.FilePath
import com.xlebo.navigation.SimpleNavController
import eu.wewox.lazytable.LazyTable
import kotlinx.io.files.Path

@Composable
fun TournamentDetailScreen(
    navController: SimpleNavController,
    file: FilePath,
) {
    SystemFileSystem
    File("${it.path}/${it.name}")
    LazyTable(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}