package com.xlebo.networking

import kotlinx.serialization.Serializable

@Serializable
data class FighterResponseDto(
    val exactMatches: List<FighterDto>?,
    val fuzzyMatches: List<FighterDto>?
)

@Serializable
data class FighterDto(
    val id: Int,
    val name: String,
    val ratings: List<Rating>?
)

@Serializable
data class Rating(
    val ratingCategoryId: Int,
    val weightedRating: Float,
    val rank: Int
)