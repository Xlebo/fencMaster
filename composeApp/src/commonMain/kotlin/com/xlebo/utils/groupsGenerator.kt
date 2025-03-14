package com.xlebo.utils

import com.xlebo.model.Participant
import com.xlebo.screens.table.groupsInProgress.Match

/**
 * Stolen from mr. Claude.ai
 */
fun generateGroupOrder(matches: List<Match>): List<Match> {
    if (matches.size <= 1) return matches

    val remainingMatches = matches.toMutableList()
    val result = mutableListOf<Match>()

    result.add(remainingMatches.removeAt(0))

    var lastParticipants = setOf(result.last().first, result.last().second)

    while (remainingMatches.isNotEmpty()) {
        val nextMatchIndex = remainingMatches.indexOfFirst { match ->
            val currentParticipants = setOf(match.first, match.second)
            currentParticipants.intersect(lastParticipants).isEmpty()
        }

        if (nextMatchIndex != -1) {
            val nextMatch = remainingMatches.removeAt(nextMatchIndex)
            result.add(nextMatch)
            lastParticipants = setOf(nextMatch.first, nextMatch.second)
        } else {
            // No suitable match found, use backtracking to find a better solution
            // We'll restart with a different initial match
            // If we've tried all possible starting matches, we'll fall back to shuffling
            if (result.size == 1 && remainingMatches.isEmpty()) {
                return matches.shuffled()
            }

            // Start over with a different match
            remainingMatches.addAll(result)
            remainingMatches.shuffle()
            result.clear()
            result.add(remainingMatches.removeAt(0))
            lastParticipants = setOf(result.last().first, result.last().second)
        }
    }

    // Validate the result - no consecutive matches should share participants
    for (i in 0 until result.size - 1) {
        val currentParticipants = setOf(result[i].first, result[i].second)
        val nextParticipants = setOf(result[i + 1].first, result[i + 1].second)

        if (currentParticipants.intersect(nextParticipants).isNotEmpty()) {
            // If validation fails, we might not have a possible solution
            // Fall back to shuffling and hoping for the best
            return matches.shuffled()
        }
    }

    return result
}

/**
 * A more advanced implementation using graph theory concepts.
 * Treats the problem as finding a path in a graph where:
 * - Vertices are matches
 * - Edges exist between matches that don't share participants
 *
 * This approach has better performance for larger groups.
 */
fun generateGroupOrderOptimized(matches: List<Match>): List<Match> {
    if (matches.size <= 1) return matches

    // Build an adjacency list representation of the graph
    val adjacencyList = buildAdjacencyList(matches)

    // Try to find a valid path through all matches
    for (startIndex in matches.indices) {
        val path = findPath(startIndex, matches, adjacencyList)
        if (path.size == matches.size) {
            return path
        }
    }

    // If we couldn't find a complete path, try a greedy approach
    val greedyResult = generateGroupOrder(matches)

    // If the greedy approach found a valid solution, return it
    if (isValidOrdering(greedyResult)) {
        return greedyResult
    }

    // Otherwise, just shuffle and hope for the best
    return matches.shuffled()
}

/**
 * Builds an adjacency list where each match is connected to all other
 * matches that don't share any participants.
 */
private fun buildAdjacencyList(matches: List<Pair<Participant, Participant>>): List<List<Int>> {
    val adjacencyList = List(matches.size) { mutableListOf<Int>() }

    for (i in matches.indices) {
        val participantsI = setOf(matches[i].first, matches[i].second)

        for (j in matches.indices) {
            if (i == j) continue

            val participantsJ = setOf(matches[j].first, matches[j].second)

            // If matches don't share participants, add an edge
            if (participantsI.intersect(participantsJ).isEmpty()) {
                adjacencyList[i].add(j)
            }
        }
    }

    return adjacencyList
}

/**
 * Uses depth-first search to find a path through the graph that visits
 * all matches without consecutive matches sharing participants.
 */
private fun findPath(
    startIndex: Int,
    matches: List<Pair<Participant, Participant>>,
    adjacencyList: List<List<Int>>
): List<Pair<Participant, Participant>> {
    val visited = BooleanArray(matches.size) { false }
    val path = mutableListOf<Pair<Participant, Participant>>()

    fun dfs(currentIndex: Int): Boolean {
        visited[currentIndex] = true
        path.add(matches[currentIndex])

        // If we've visited all matches, we're done
        if (path.size == matches.size) {
            return true
        }

        // Try all neighboring matches
        for (nextIndex in adjacencyList[currentIndex]) {
            if (!visited[nextIndex]) {
                if (dfs(nextIndex)) {
                    return true
                }
            }
        }

        // If we couldn't complete the path from here, backtrack
        visited[currentIndex] = false
        path.removeAt(path.size - 1)
        return false
    }

    dfs(startIndex)
    return path
}

/**
 * Checks if a match ordering is valid (no consecutive matches share participants).
 */
private fun isValidOrdering(matches: List<Pair<Participant, Participant>>): Boolean {
    if (matches.size <= 1) return true

    for (i in 0 until matches.size - 1) {
        val currentParticipants = setOf(matches[i].first, matches[i].second)
        val nextParticipants = setOf(matches[i + 1].first, matches[i + 1].second)

        if (currentParticipants.intersect(nextParticipants).isNotEmpty()) {
            return false
        }
    }

    return true
}