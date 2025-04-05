package com.xlebo.model

import com.xlebo.screens.table.groupsInProgress.Match
import kotlinx.serialization.Serializable

@Serializable
data class GroupResults(
    val id: Int,
    // allow V as a value
    val results: Map<Match, Pair<String, String>>,
    val locked: Boolean
)