package com.xlebo.utils

import com.xlebo.model.Participant
import com.xlebo.screens.table.groupsInProgress.Match

fun createPairs(matches: Set<Match>): List<Match> {
    if (matches.size < 3) return matches.toList().shuffled()

    val result = mutableListOf<Pair<Participant, Participant>>()
    val remainingPairs = matches.toMutableList()

    result.add(remainingPairs.removeAt(0))

    while (remainingPairs.isNotEmpty()) {
        val lastPair = result.last()
        val lastParticipants = setOf(lastPair.first, lastPair.second)

        val nextPairIndex = remainingPairs.indexOfFirst { pair ->
            setOf(pair.first, pair.second).intersect(lastParticipants).isEmpty()
        }

        if (nextPairIndex != -1) {
            result.add(remainingPairs.removeAt(nextPairIndex))
        } else {
            if (remainingPairs.size > 1) {
                var bestSequence = emptyList<Pair<Participant, Participant>>()
                var bestScore = Int.MIN_VALUE

                for (i in remainingPairs.indices) {
                    val testSequence = mutableListOf<Pair<Participant, Participant>>()
                    val testRemaining = remainingPairs.toMutableList()
                    val firstPair = testRemaining.removeAt(i)
                    testSequence.add(firstPair)

                    var score =
                        if (setOf(firstPair.first, firstPair.second).intersect(lastParticipants)
                                .isEmpty()
                        ) 1 else 0

                    while (testRemaining.isNotEmpty()) {
                        val testLastPair = testSequence.last()
                        val testLastParticipants =
                            setOf(testLastPair.first, testLastPair.second)

                        val testNextPairIndex = testRemaining.indexOfFirst { pair ->
                            setOf(pair.first, pair.second).intersect(testLastParticipants)
                                .isEmpty()
                        }

                        if (testNextPairIndex != -1) {
                            testSequence.add(testRemaining.removeAt(testNextPairIndex))
                            score++
                        } else if (testRemaining.isNotEmpty()) {
                            testSequence.add(testRemaining.removeFirst())
                        }
                    }

                    if (score > bestScore) {
                        bestScore = score
                        bestSequence = testSequence
                    }
                }

                if (bestSequence.isNotEmpty()) {
                    result.addAll(bestSequence)
                    remainingPairs.clear()
                } else {
                    result.add(remainingPairs.removeAt(0))
                }
            } else {
                result.add(remainingPairs.removeAt(0))
            }
        }
    }

    if (!isValidOrdering(result)) {
        return matches.shuffled()
    }

    return result
}

private fun isValidOrdering(pairs: List<Pair<Participant, Participant>>): Boolean {
    if (pairs.size <= 1) return true

    for (i in 0 until pairs.size - 1) {
        val currentPair = setOf(pairs[i].first, pairs[i].second)
        val nextPair = setOf(pairs[i + 1].first, pairs[i + 1].second)

        if (currentPair.intersect(nextPair).isNotEmpty()) {
            return false
        }
    }

    return true
}


data class GroupingResult(
    val numberOfGroups: Int,
    val groupSizes: List<Int>,
    val totalMatches: Int
) {
    override fun toString(): String {
        return """
            |Optimal Grouping:
            |Number of groups: $numberOfGroups
            |Group sizes: ${groupSizes.joinToString(", ")}
            |Total matches: $totalMatches
            |Average matches per group: ${totalMatches.toDouble() / numberOfGroups}
        """.trimMargin()
    }
}

fun findOptimalGroups(totalParticipants: Int, minGroupSize: Int = 5): GroupingResult {
    require(totalParticipants >= minGroupSize) { "Total participants must be at least the minimum group size" }

    val possibleSolutions = mutableListOf<GroupingResult>()

    val maxGroups = totalParticipants / minGroupSize

    for (numGroups in 1..maxGroups) {
        val avgGroupSize = totalParticipants / numGroups
        val remainder = totalParticipants % numGroups

        val groupSizes = List(numGroups) { i ->
            if (i < remainder) avgGroupSize + 1 else avgGroupSize
        }

        if (groupSizes.all { it >= minGroupSize }) {
            val totalMatches = groupSizes.sumOf { groupSize -> groupSize * (groupSize - 1) / 2 }

            possibleSolutions.add(GroupingResult(
                numberOfGroups = numGroups,
                groupSizes = groupSizes,
                totalMatches = totalMatches
            ))
        }
    }

    return possibleSolutions.minByOrNull { it.totalMatches }
        ?: throw IllegalStateException("No valid grouping found")
}