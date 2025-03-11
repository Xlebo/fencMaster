package com.xlebo

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.xlebo.networking.HemaRatingClient
import com.xlebo.networking.createHttpClient
import fencmaster.composeapp.generated.resources.Res
import fencmaster.composeapp.generated.resources.mec
import io.ktor.client.engine.okhttp.OkHttp
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FencMaster",
        icon = painterResource(Res.drawable.mec)
    ) {

        App(
            hemaRatingClient = remember {
                HemaRatingClient(createHttpClient(OkHttp.create()), System.getenv("hr.apikey"))
            },
            lazyListScrollBar = { modifier, lazyListState ->
                getVerticalScrollbar(
                    modifier,
                    lazyListState
                )
            },
            persistenceHandler = TournamentLoader()
        )
    }
}

@Composable
fun getVerticalScrollbar(modifier: Modifier, state: LazyListState) {
    VerticalScrollbar(
        modifier = modifier.width(15.dp),
        adapter = rememberScrollbarAdapter(
            scrollState = state
        )
    )
}