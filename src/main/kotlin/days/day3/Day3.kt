package org.example.days.day3

import org.example.days.Day

class Day3 : Day {
    fun getInput(): String = getFileText()

    fun getValidPart1(): Sequence<String> {
        val regex = Regex("""mul\(\d+,\d+\)""")
        return regex.findAll(getInput()).map { it -> it.value }
    }

    fun mul(s: String): Int {
        val n1 = s.split(',').first().drop(4).toInt()
        val n2 = s.split(',').last().dropLast(1).toInt()
        return n1 * n2
    }

    fun solvePart2(s: String = getInput()): Int {
        val regex = Regex("""mul\(\d+,\d+\)|do\(\)|don't\(\)""")
        var enabled = true

        return regex.findAll(s).map { it.value }.fold(0) {
                acc: Int, pattern: String ->
            when (pattern) {
                "do()" -> {
                    enabled = true
                    acc
                }
                "don't()" -> {
                    enabled = false
                    acc
                }
                else ->
                    if (enabled) {
                        acc + mul((pattern))
                    } else {
                        acc
                    }
            }
        }
    }

    fun solvePart2Another(s: String = getInput()) =
        Regex(
            """mul\(\d+,\d+\)|do\(\)|don't\(\)""",
        ).findAll(s).takeWhile { it.value != "don't" }

    override val day: Int = 3

    override fun solvePart1(): Any = getValidPart1().sumOf(::mul)

    override fun solvePart2(): Any = solvePart2()
}
