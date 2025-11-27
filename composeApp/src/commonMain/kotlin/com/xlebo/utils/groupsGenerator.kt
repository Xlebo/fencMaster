package com.xlebo.utils

import com.xlebo.model.Participant
import kotlin.random.Random

/**
 * Creates pairs of participants with constraints:
 * - Each pair of participants appears exactly once
 * - No participant appears in consecutive pairs if possible
 * - Matches are distributed evenly for each participant
 */
fun createPairings(participants: List<Participant>): List<Pair<Participant, Participant>> {
    // Generate all unique pairs
    val allPairs = generateUniquePairs(participants)

    // If we have too few participants, just shuffle and return
    if (participants.size <= 3) {
        return allPairs.shuffled()
    }

    // Try to find an ordering that satisfies our constraints
    return try {
        orderPairsWithConstraints(allPairs, participants)
    } catch (e: Exception) {
        // If ordering fails, shuffle the pairs
        allPairs.shuffled()
    }
}

/**
 * Generate all unique pairs from the list of participants
 */
fun generateUniquePairs(participants: List<Participant>): List<Pair<Participant, Participant>> {
    val pairs = mutableListOf<Pair<Participant, Participant>>()

    for (i in participants.indices) {
        for (j in i + 1 until participants.size) {
            pairs.add(Pair(participants[i], participants[j]))
        }
    }

    return pairs
}

/**
 * Order pairs so that no participant appears in consecutive pairs
 * and matches are distributed evenly for each participant
 */
fun orderPairsWithConstraints(
    pairs: List<Pair<Participant, Participant>>,
    participants: List<Participant>
): List<Pair<Participant, Participant>> {
    val result = mutableListOf<Pair<Participant, Participant>>()
    val remainingPairs = pairs.toMutableList()
    val participantMatches = participants.associateWith { 0 }.toMutableMap()

    // Start with a random pair
    var currentPair = remainingPairs.removeAt(Random.nextInt(remainingPairs.size))
    result.add(currentPair)

    // Update match counts
    participantMatches[currentPair.first] = participantMatches[currentPair.first]!! + 1
    participantMatches[currentPair.second] = participantMatches[currentPair.second]!! + 1

    // Set of participants from the last added pair
    var lastPairParticipants = setOf(currentPair.first, currentPair.second)

    // Continue until all pairs are used
    while (remainingPairs.isNotEmpty()) {
        // Find next pair that doesn't contain participants from the last pair
        // and prioritize participants with fewer matches
        val nextPairIndex = findNextPairIndex(remainingPairs, lastPairParticipants, participantMatches)

        if (nextPairIndex == -1) {
            // If no suitable pair found, find any pair that minimizes overlap
            val fallbackIndex = findFallbackPairIndex(remainingPairs, lastPairParticipants, participantMatches)
            currentPair = remainingPairs.removeAt(fallbackIndex)
        } else {
            currentPair = remainingPairs.removeAt(nextPairIndex)
        }

        result.add(currentPair)

        // Update match counts
        participantMatches[currentPair.first] = participantMatches[currentPair.first]!! + 1
        participantMatches[currentPair.second] = participantMatches[currentPair.second]!! + 1

        // Update last pair participants
        lastPairParticipants = setOf(currentPair.first, currentPair.second)
    }

    return result
}

/**
 * Find index of next pair that doesn't share participants with lastPairParticipants
 * and prioritizes participants with fewer matches
 */
fun findNextPairIndex(
    remainingPairs: List<Pair<Participant, Participant>>,
    lastPairParticipants: Set<Participant>,
    participantMatches: Map<Participant, Int>
): Int {
    var bestScore = Int.MAX_VALUE
    var bestIndex = -1

    remainingPairs.forEachIndexed { index, pair ->
        // Check if this pair doesn't share participants with the last pair
        if (pair.first !in lastPairParticipants && pair.second !in lastPairParticipants) {
            // Score is based on number of matches these participants already have
            val score = participantMatches[pair.first]!! + participantMatches[pair.second]!!

            if (score < bestScore) {
                bestScore = score
                bestIndex = index
            }
        }
    }

    return bestIndex
}

/**
 * Fallback pair selection when no ideal pair is available
 */
fun findFallbackPairIndex(
    remainingPairs: List<Pair<Participant, Participant>>,
    lastPairParticipants: Set<Participant>,
    participantMatches: Map<Participant, Int>
): Int {
    var minOverlap = 3 // Maximum possible overlap is 2
    var minMatchScore = Int.MAX_VALUE
    var bestIndex = 0

    remainingPairs.forEachIndexed { index, pair ->
        // Count how many participants are in both pairs
        val overlap = countOverlap(setOf(pair.first, pair.second), lastPairParticipants)

        // Score based on number of matches
        val matchScore = participantMatches[pair.first]!! + participantMatches[pair.second]!!

        // Prioritize pairs with less overlap, then with fewer matches
        if (overlap < minOverlap || (overlap == minOverlap && matchScore < minMatchScore)) {
            minOverlap = overlap
            minMatchScore = matchScore
            bestIndex = index
        }
    }

    return bestIndex
}

/**
 * Count how many elements are in both sets
 */
fun countOverlap(set1: Set<Participant>, set2: Set<Participant>): Int {
    return set1.count { it in set2 }
}
