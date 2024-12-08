package org.example.days.day2
import org.example.days.Day
import kotlin.math.abs

class Day2 : Day {
    private fun getInput(): List<List<Int>> =
        getFileText()
            .split("\n")
            .map {
                it.split(" ")
                    .map(String::toInt)
            }

    fun isAscending(list: List<Int>): Boolean = list.zipWithNext().all { (i, i2) -> i < i2 }

    fun isDescending(list: List<Int>): Boolean = list.zipWithNext().all { (i, i2) -> i > i2 }

    fun atLeastOneLevelDifference(list: List<Int>): Boolean = list.zipWithNext().all { (i, i2) -> abs(i - i2) >= 1 }

    fun atMostThreeLevelDifference(list: List<Int>): Boolean = list.zipWithNext().all { (i, i2) -> abs(i - i2) <= 3 }

    fun sequenceOfListsRemovingOneElement(list: List<Int>): Sequence<List<Int>> =
        list.asSequence().mapIndexed { index, _ -> list.filterIndexed { index2, _ -> index != index2 } }

    fun isSafe(list: List<Int>): Boolean =
        (
            isAscending(
                list,
            ) || isDescending(list)
        ) && atLeastOneLevelDifference(list) && atMostThreeLevelDifference(list)

    fun isSafe2(list: List<Int>): Boolean = isSafe(list) || (sequenceOfListsRemovingOneElement(list).any(::isSafe))

    override val day: Int = 2

    override fun solvePart1(): Int = getInput().count(::isSafe)

    override fun solvePart2(): Int = getInput().count(::isSafe2)
}
