package com.xlebo.networking

import com.xlebo.model.Participant
import com.xlebo.utils.Constants
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class HemaRatingClient(private val httpClient: HttpClient, private val apiKey: String) {

    init {
        println("HemaRatingClient initialized, apikey: $apiKey")
    }

    // TODO: Make some notification on exception, or progress bar
    suspend fun fetchParticipantRanks(
        participants: List<Participant>,
        category: Int
    ): List<Participant> {
        if (apiKey.isEmpty()) {
            Napier.w("API KEY IS NOT SET")
        }
        try {
            val body = participants
                .filter { it.hrId != null } // remove later if better decision mechanism is given
                .map { it.firstName + ' ' + it.lastName }
            println("Fetching ratings for fellas: ")
            body.forEach { println(it) }
            val response: List<FighterResponseDto> = httpClient.post(Constants.HEMARATINGS_API) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Constants.HEMARATINGS_API
                    path("fighters/search")
                    parameters.append("includeRating", "true")
                }
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                headers {
//                append("x-functions-key", apiKey)
                }
                setBody(body)
            }.body()

            println("Got responses: ")
            response.forEach { println(it) }

            return participants.mapIndexed { index, participant ->
                if (participant.hrId == null) {
                    null
                } else {
                    val rank =
                        response[index].exactMatches?.find { it.id == participant.hrId }?.ratings?.find { it.ratingCategoryId == category }?.rank
                            ?: response[index].fuzzyMatches?.find { it.id == participant.hrId }?.ratings?.find { it.ratingCategoryId == category }?.rank

                    if (rank != null) {
                        participant.copy(rank = rank)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
        } catch (e: Exception) {
            Napier.w { e.stackTraceToString() }
            return listOf()
        }
    }
}