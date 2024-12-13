package org.example.days.day10

import org.example.days.Day

data class Point(val height: Int, val x: Int, val y: Int) {
    fun distanceTo(other: Point): Int = Math.abs(x - other.x) + Math.abs(y - other.y)
    fun isPathTo(other: Point): Boolean = (height - other.height == 1) && (distanceTo(other) == 1)
}


typealias Grid = List<Point>

fun Grid.getStartingPoints(): List<Point> = this.filter { p -> p.height == 0 }

fun Grid.getAdjacentPoints(p: Point): List<Point> = this.filter { it.isPathTo(p) }

fun getGrid(s: String): Grid = s.split("\n").mapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Point(c.toString().toInt(), x, y)
    }
}.flatten()


class Day10 : Day {
    override val day: Int = 10

    fun map(s: String): Grid = getGrid(s)

    fun Grid.goClimb(p: Point): List<Point> =
        DeepRecursiveFunction<Point, List<Point>> { pp ->
            if(pp.height == 9) {
                return@DeepRecursiveFunction listOf(pp)
            }
            getAdjacentPoints(pp).flatMap { callRecursive(it) }
        }(p)

    fun solvePart1(s: String): Int =
        map(s).let { grid -> grid.getStartingPoints().map { point -> point to grid.goClimb(point).toSet() } }
            .sumOf { it.second.size }

    fun solvePart2(s: String): Int =
        map(s).let { grid -> grid.getStartingPoints().map { point -> point to grid.goClimb(point) } }
            .sumOf { it.second.size }


    override fun solvePart1(): Int = solvePart1(getFileText())
    override fun solvePart2(): Int = solvePart2(getFileText())
}

