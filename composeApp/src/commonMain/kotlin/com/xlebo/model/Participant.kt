package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupStatistics(
    val wins: Float,
    val totalMatches: Float,
    val hitsScored: Float,
    val hitsReceived: Float
) {
    constructor(stats: List<Float>) : this(stats[0], stats[1], stats[2], stats[3])
}

@Serializable
data class Participant(
    // registration order
    val order: Int,
    val hrId: Int?,
    val firstName: String,
    val lastName: String,
    val club: String?,
    val nationality: String?,
    val lang: String?,
    // hr rank, fetched
    val rank: Int?,
    val disabled: Boolean = false,
    val group: Int? = null,
    val groupStatistics: GroupStatistics? = null,
    val playOffOrder: Int? = null
) {
    companion object : TableValue {
        override fun getHeaders(): List<String> {
            return listOf(
                "Pocet",
                "HR ID",
                "Jmeno",
                "Prijmeni",
                "Klub",
                "Narodnost",
                "Jazyk",
                "HR Rank"
            )
        }
    }
}