package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
enum class TournamentStatus {
    NEW,
    GROUPS_PREVIEW,
    GROUPS_STARTED,
    PLAYOFF
}