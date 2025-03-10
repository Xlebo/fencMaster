package com.xlebo.screens

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.xlebo.screens.dialog.SubmitDialog
import com.xlebo.viewModel.SharedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupsPreviewScreen(
    navController: NavHostController,
    lazyListScrollBar: (@Composable (Modifier, LazyListState) -> Unit)? = null,
) {
    var submitDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val viewModel: SharedViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (submitDialog) {
        SubmitDialog(
            onBackRequest = { submitDialog = false },
            onContinueRequest = {
                // persist state
                // next screen
            }
        )
    } else {


    }
}