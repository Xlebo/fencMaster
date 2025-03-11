package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
enum class TournamentState {
    NEW,
    GROUPS_PREVIEW,
    GROUPS_STARTED,
    PLAYOFF
}