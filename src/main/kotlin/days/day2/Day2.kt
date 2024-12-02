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

    private fun allInOrder(
        list: List<Int>,
        compareFunction: (Int, Int) -> Boolean,
    ): Boolean =
        list.subList(
            0,
            list.size - 1,
        ).mapIndexed { index, i -> compareFunction(i, list[index + 1]) }.all {
            it
        }

    fun isAscending(list: List<Int>): Boolean = allInOrder(list) { i, i2 -> i < i2 }

    fun isDescending(list: List<Int>): Boolean = allInOrder(list) { i, i2 -> i > i2 }

    fun atLeastOneLevelDifference(list: List<Int>): Boolean = allInOrder(list) { i, i2 -> abs(i - i2) >= 1 }

    fun atMostThreeLevelDifference(list: List<Int>): Boolean = allInOrder(list) { i, i2 -> abs(i - i2) <= 3 }

    fun removeOne(list: List<Int>): List<List<Int>> = List(list.size) { index -> list.filterIndexed { index2, i2 -> index != index2 } }

    fun isSafe(list: List<Int>): Boolean =
        (
            isAscending(
                list,
            ) || isDescending(list)
        ) && atLeastOneLevelDifference(list) && atMostThreeLevelDifference(list)

    fun isSafe2(list: List<Int>): Boolean =
        (
            (
                isAscending(
                    list,
                ) || isDescending(list)
            ) && atLeastOneLevelDifference(list) && atMostThreeLevelDifference(list)
        ) || (removeOne(list).any(::isSafe))

    override val day: Int = 2

    override fun solvePart1(): Int = getInput().count(::isSafe)

    override fun solvePart2(): Int = getInput().count(::isSafe2)
}
