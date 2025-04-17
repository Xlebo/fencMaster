package com.xlebo.model

import kotlinx.serialization.Serializable

/**
 * since kotlin pair is not serializable -_-
 */
@Serializable
data class StrPair(
    val first: String,
    val second: String,
)

@Serializable
data class ParticipantPair(
    val first: Participant,
    val second: Participant
)
