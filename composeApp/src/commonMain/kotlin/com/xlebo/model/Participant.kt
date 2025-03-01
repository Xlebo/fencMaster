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

    companion object : TableValue {
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

        override fun getWeights(): List<Float> {
            val weights = listOf(.1f, .1f, .2f, .2f, .1f, .2f, .1f)
            check(weights.size == getHeaders().size) { "nech mi nejebe" }
            return weights
        }
    }
}