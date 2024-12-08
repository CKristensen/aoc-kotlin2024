package org.example.days.day7

import org.example.days.Day

class Day7: Day {
    override val day: Int = 7

    data class Calibration(
        val result: Long,
        val input: List<Long>
    )

    enum class Operation {
        ADD, MULTIPLY, CONCATENATE
    }

    fun generateOperations1(): List<List<Operation>> = listOf(listOf(Operation.ADD), listOf(Operation.MULTIPLY))
    fun generateOperations2(): List<List<Operation>> = listOf(listOf(Operation.ADD), listOf(Operation.MULTIPLY), listOf(Operation.CONCATENATE))

    fun generateOperationsN1(n: Int): List<List<Operation>> {
        if (n == 1) return generateOperations1()
        val previous = generateOperationsN1(n - 1)
        val result = mutableListOf<List<Operation>>()
        for (op in generateOperations1()) {
            for (prev in previous) {
                result.add(prev + op)
            }
        }
        return result
    }


    fun generateOperationsN2(n: Int): List<List<Operation>> {
        if (n == 1) return generateOperations2()
        val previous = generateOperationsN2(n - 1)
        val result = mutableListOf<List<Operation>>()
        for (op in generateOperations2()) {
            for (prev in previous) {
                result.add(prev + op)
            }
        }
        return result
    }
    fun doOperation(op: Operation, a: Long, b: Long): Long = when (op) {
        Operation.ADD -> a + b
        Operation.MULTIPLY -> a * b
        Operation.CONCATENATE -> "$a$b".toLong()
    }

    fun doOperations(ops: List<Operation>, input: List<Long>): Long {
        var result = input[0]
        for (i in 1 until input.size) {
            result = doOperation(ops[i - 1], result, input[i])
        }
        return result
    }

    fun calibrationPossible(calibration: Calibration, generate: (Int) -> List<List<Operation>>): Boolean {
        return generate(calibration.input.size)
            .map { doOperations(it, calibration.input) }
            .any{ it == calibration.result }
    }

    fun generateCalibrations(): List<Calibration> {
        val text = getFileText()
        //val text = """
        //    190: 10 19
        //    3267: 81 40 27
        //    83: 17 5
        //    156: 15 6
        //    7290: 6 8 6 15
        //    161011: 16 10 13
        //    192: 17 8 14
        //    21037: 9 7 18 13
        //    292: 11 6 16 20
        //""".trimIndent()
        return text.lines().map {
            val parts = it.split(": ")
            val result = parts[0].toLong()
            val input = parts[1].split(" ").map { it.toLong() }
            Calibration(result, input)
        }
    }

    override fun solvePart1(): Long {
        return generateCalibrations().filter { calibrationPossible(it){ n -> generateOperationsN1(n) } }.sumOf { it.result }
    }
    override fun solvePart2(): Long {
        return generateCalibrations().filter {calibrationPossible(it){ n -> generateOperationsN2(n) } }.sumOf { it.result }
    }

}