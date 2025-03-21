package org.example.days

import java.io.File

interface Day {
    fun solvePart1(): Any

    fun solvePart2(): Any

    val day: Int

    fun getFileText(): String = File("src/main/resources/day$day.txt").readText()

    fun displayDay() {
        println("================")
        println("Day$day")
        println("Part 1: ")
        solvePart1().let(::println)
        println("Part 2: ")
        solvePart2().let(::println)
        println("================")
    }
}