package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    var hrId: Int?,
    var firstName: String,
    var lastName: String,
    var club: String?,
    var nationality: String?,
    var lang: String?,
    var rank: Int?
) {

    companion object : CsvFile {
        override fun getHeaders(): List<String> {
            return listOf(
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