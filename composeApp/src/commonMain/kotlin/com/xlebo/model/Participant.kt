package com.xlebo.model

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val order: Int,
    var hrId: Int?,
    var firstName: String,
    var lastName: String,
    var club: String?,
    var nationality: String?,
    var lang: String?,
    var rank: Int?
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

        override fun getWeights(): List<Float> {
            val weights = listOf(.5f, 1f, 1.5f, 2f, 3f, 2f, 2f, 1.5f)
            check(weights.size == getHeaders().size) { "nech mi nejebe" }
            return weights
        }
    }
}