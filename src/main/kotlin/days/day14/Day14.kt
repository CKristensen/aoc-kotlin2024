package org.example.days.day14

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.example.days.Day


typealias MapSize = Pair<Int, Int>

data class Position(val x: Int, val y: Int)
data class Velocity(val x: Int, val y: Int)
data class Guard(val position: Position, val velocity: Velocity)

enum class Quadrant {
    I, II, III, IV, MIDDLE
}

fun noGuardOverlapping(guards: List<Guard>): Boolean = guards.map { it.position.x to it.position.y}.distinct().size == guards.size

fun makeMap(map: MapSize, guards: List<Guard>): String =
    (map.second downTo  0).map{ y ->
        (0 .. map.first).map { x ->
            when (guards.count { g -> g.position.x == x && g.position.y == y }) {
                0 -> " "
                else -> "#"
            }
        }.let { line -> line.also { println(it) } }
    }.let { lines -> lines.joinToString { "\n" }}


fun walk(guard: Guard, time: Int, map: MapSize): Position {
    var x = (guard.position.x + (guard.velocity.x * time)) % map.first
    var y = (guard.position.y + (guard.velocity.y * time)) % map.second

    if(x<0) {
        x += map.first
    }
    if(y<0) {
        y += map.second
    }
    return Position(x, y)
}

fun walkG(guard: Guard, time: Int, map: MapSize): Guard {
    var x = (guard.position.x + (guard.velocity.x * time)) % map.first
    var y = (guard.position.y + (guard.velocity.y * time)) % map.second

    if(x<0) {
        x += map.first
    }
    if(y<0) {
        y += map.second
    }
    return Guard(Position(x, y), Velocity(guard.velocity.x, guard.velocity.y))
}

fun quadrant(position: Position, map: MapSize): Quadrant {
    return when {
        position.x < map.first / 2 && position.y < map.second / 2 -> Quadrant.I
        position.x > map.first / 2 && position.y < map.second / 2 -> Quadrant.II
        position.x > map.first / 2 && position.y > map.second / 2 -> Quadrant.III
        position.x < map.first / 2 && position.y > map.second / 2 -> Quadrant.IV
        else -> Quadrant.MIDDLE
    }
}

fun safetyFactor(mapQuadrant: Map<Quadrant, List<Position>>): Int {
    val q1 = mapQuadrant[Quadrant.I]?.size ?: return 0
    val q2 = mapQuadrant[Quadrant.II]?.size ?: return 0
    val q3 = mapQuadrant[Quadrant.III]?.size ?: return 0
    val q4 = mapQuadrant[Quadrant.IV]?.size ?: return 0

    return q1 * q2 * q3 * q4
}

class Day14: Day {
    override val day: Int = 14

    fun parseInput(input: String): List<Guard> {
        return input.split("\n").map {
            val (x, y, vx, vy) = it.split(" ").flatMap { it.split("=")[1].split(",") }
            Guard(Position(x.toInt(), y.toInt()), Velocity(vx.toInt(), vy.toInt()))
        }
    }


    fun solvePart1(input: String, mapSize: MapSize, seconds: Int): Int =
        parseInput(getFileText()).map { guard ->
            walk(guard, seconds, mapSize)
        }.groupBy { quadrant(it, mapSize) }.let {
            safetyFactor(it)
        }

    fun infiniteWalk(guards: List<Guard>, map: MapSize) =
        flow {
            var currentState = guards
            var seconds = 0
            while (true){
                currentState = currentState.map { guard -> walkG(guard, 1, map) }
                seconds++
                if(noGuardOverlapping(currentState)){
                    // println(seconds)
                    // emit(currentState)
                    emit(seconds)
                }
                // if(seconds%1000 == 0){
                //     println(seconds)
                // }
            }
        }

    override fun solvePart1(): Any = solvePart1(getFileText(), Pair(101, 103), 100)

    override fun solvePart2(): Int = runBlocking { infiniteWalk(parseInput(getFileText()), 101 to 103).take(1).first()}
}





