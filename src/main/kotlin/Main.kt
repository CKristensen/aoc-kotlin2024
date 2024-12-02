package org.example

import org.example.days.Day
import org.example.days.day1.Day1
import org.example.days.day2.Day2

fun displayDay(day: Day) {
    println("================")
    println("Day${day.day}")
    print("Part 1: ")
    day.solvePart1().let(::println)
    print("Part 2: ")
    day.solvePart2().let(::println)
    println("================")
}

fun main() {
    displayDay(Day1())
    displayDay(Day2())
}

