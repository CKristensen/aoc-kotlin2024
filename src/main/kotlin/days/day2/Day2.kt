package org.example.days.day2
import org.example.days.Day
import java.io.File
import kotlin.math.abs

class Day2 : Day {

    override val day: Int = 2
    private fun getInput(): List<List<Int>> =
        File("src/main/resources/day2.txt")
            .readText()
            .split("\n")
            .map { it.split(" ")
                .map(String::toInt) }

    fun isAscending(list: List<Int>): Boolean = list.subList(0, list.size-1).mapIndexed() { index, i -> i < list[index+1] }.all { it }
    fun isDescending(list: List<Int>): Boolean = list.subList(0, list.size-1).mapIndexed() { index, i -> i > list[index+1] }.all { it }
    fun atLeastOneLevelDifference(list: List<Int>): Boolean = list.subList(0, list.size-1).mapIndexed() { index, i -> abs(i - list[index+1]) >= 1 }.all { it }
    fun atMostThreeLevelDifference(list: List<Int>): Boolean = list.subList(0, list.size-1).mapIndexed() { index, i -> abs(i - list[index+1]) <= 3 }.all { it }
    fun removeOne(list: List<Int>): List<List<Int>> = List(list.size) { index -> list.filterIndexed { index2, i2 -> index != index2 } }

    fun isSafe(list: List<Int>): Boolean = (isAscending(list) || isDescending(list)) && atLeastOneLevelDifference(list) && atMostThreeLevelDifference(list)
    fun isSafe2(list: List<Int>): Boolean = ((isAscending(list) || isDescending(list)) && atLeastOneLevelDifference(list) && atMostThreeLevelDifference(list)) || (removeOne(list).any(::isSafe))


    override fun solvePart1(): Int = getInput().count(::isSafe)

    override fun solvePart2(): Int = getInput().count(::isSafe2)
    }

