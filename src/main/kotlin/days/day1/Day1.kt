package org.example.days.day1
import org.example.days.Day
import kotlin.math.abs

class Day1 : Day {
    private fun getInput(): Pair<List<Int>, List<Int>> =
        getFileText()
            .split("\n")
            .map { lineStringWith2Integers ->
                lineStringWith2Integers.split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
            }
            .let { listOfListsWith2Integers ->
                Pair(
                    listOfListsWith2Integers.map { integers -> integers.first() },
                    listOfListsWith2Integers.map { integers -> integers.last() },
                )
            }

    override val day: Int = 1

    override fun solvePart1(): Int =
        getInput().let { pair ->
            pair
                .first
                .sorted()
                .zip(pair.second.sorted())
                .sumOf { (first, second) ->
                    abs(first - second)
                }
        }

    override fun solvePart2(): Int =
        getInput().let { pair ->
            pair.first.sumOf { n -> n * pair.second.count { it == n } }
        }
}
