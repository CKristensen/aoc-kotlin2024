package org.example.days.day5
import org.example.days.Day
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

class Day5: Day {
    override val day: Int = 5

    fun getRules(s: String): List<Pair<Int, Int>> = s.split("\n").filter { it.contains("|") }.map {
        val (a, b) = it.split("|").map { it.trim().toInt() }
        a to b
    }

   fun getLines(s: String): List<List<Int>> =
       s.split("\n").filter { it.contains("|").not() && it.isEmpty().not() }.map { it.split(",").map { it.toInt() } }


    fun doesLineObeyRules(line: List<Int>, rules: List<Pair<Int, Int>>): Boolean = rules.any { (a, b) ->
        line.indexOf(a) > line.indexOf(b) && line.indexOf(a) != -1 && line.indexOf(b) != -1
    }.not()

    fun getMiddleNumber(line: List<Int>): Int = line[floor(line.size.toDouble() / 2).toInt()]

    fun correctlyOrderAWrongLine(line: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        val (a, b) = runCatching {  rules.first { (a, b) ->
            line.indexOf(a) > line.indexOf(b) && line.indexOf(a) != -1 && line.indexOf(b) != -1
        } }.getOrNull() ?: return line
        val aIndex = line.indexOf(a)
        val bIndex = line.indexOf(b)
        val newLine = line.toMutableList()
        newLine[aIndex] = b
        newLine[bIndex] = a
        if(doesLineObeyRules(newLine, rules)) return newLine
        else return correctlyOrderAWrongLine(newLine.toList(), rules)
    }

    fun solvePart1(s: String) = s.let { fileText ->
        val rules = getRules(fileText)
        val lines = getLines(fileText)
        lines.filter { doesLineObeyRules(it, rules) }.sumOf { getMiddleNumber(it) }
    }

    override fun solvePart1(): Int = solvePart1(getFileText())


    fun solvePart2(s: String) = s.let { fileText ->
        val rules = getRules(fileText)
        val lines = getLines(fileText)
        lines.filter { doesLineObeyRules(it, rules).not() }
            .map { correctlyOrderAWrongLine(it, rules) }
            .sumOf { getMiddleNumber(correctlyOrderAWrongLine(it, rules)) }
    }

    override fun solvePart2(): Int = solvePart2(getFileText())
}
