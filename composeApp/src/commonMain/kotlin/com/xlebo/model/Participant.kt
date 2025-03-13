package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupStatistics(
    val placement: Int,
    val wins: Int,
    val loses: Int,
    val hitsScored: Int,
    val hitsReceived: Int
)

@Serializable
data class Participant(
    val order: Int,
    val hrId: Int?,
    val firstName: String,
    val lastName: String,
    val club: String?,
    val nationality: String?,
    val lang: String?,
    val rank: Int?,
    val disabled: Boolean = false,
    val group: Int? = null,
    val groupStatistics: GroupStatistics? = null,
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