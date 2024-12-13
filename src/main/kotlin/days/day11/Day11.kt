package org.example.days.day11

import org.example.days.Day
import kotlin.math.pow


fun Long.splitNumber(n: Int): List<Long> = listOf(this / 10.0.pow(n).toLong(), this % 10.0.pow(n).toLong())

fun Long.rule3(): List<Long> = listOf(this * 2024)

fun Long.blink(): List<Long> = when {
    this == 0L -> listOf(1)
    this < 10 -> this.rule3()
    this < 100 -> this.splitNumber(1)
    this < 1000 -> this.rule3()
    this < 10000 -> this.splitNumber(2)
    this < 100000 -> this.rule3()
    this < 1000000 -> this.splitNumber(3)
    this < 10000000 -> this.rule3()
    this < 100000000 -> this.splitNumber(4)
    this < 1000000000 -> this.rule3()
    this < 10000000000 -> this.splitNumber(5)
    this < 100000000000 -> this.rule3()
    this < 1000000000000 -> this.splitNumber(6)
    this < 10000000000000 -> this.rule3()
    this < 100000000000000 -> this.splitNumber(7)
    else -> throw IllegalArgumentException("Number too big")
}

class Day11 : Day {
    override val day: Int = 11

    private val memo: MutableMap<Long, List<Long>> = mutableMapOf(0.toLong() to listOf(1))

    private val memoN: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

    fun getInput(s: String): List<Long> = s.split(" ").map { it.toLong() }

    fun blinkMemo(input: Long): List<Long> = memo.getOrPut(input) { input.blink() }

    fun blinkNMemo(input: Long, n: Int): Long = if (n == 0) 1 else memoN.getOrPut(input to n) {
        blinkMemo(input).fold(0) { acc, it -> acc + blinkNMemo(it, n - 1) }
    }

    override fun solvePart1(): Long = getInput(getFileText()).fold(0) { acc, i -> acc + blinkNMemo(i, 25) }
    override fun solvePart2(): Long = getInput(getFileText()).fold(0) { acc, i -> acc + blinkNMemo(i, 75) }
    }