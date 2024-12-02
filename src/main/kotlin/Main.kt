package org.example

import org.example.days.Day
import org.example.days.day1.Day1
import org.example.days.day2.Day2

fun main() = listOf(Day1(), Day2()).forEach(Day::displayDay)
