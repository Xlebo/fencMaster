package com.xlebo.model

data class Participant(
    val hrId: Int?,
    val firstName: String,
    val lastName: String,
    val club: String?,
    val nationality: String?,
    val lang: String?,
    val rank: Int?
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