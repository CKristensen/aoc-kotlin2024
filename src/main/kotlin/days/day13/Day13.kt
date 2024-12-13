package org.example.days.day13

import org.example.days.Day

data class Button(val x: Long, val y: Long, val cost: Long)
fun buttonA(xa: Long, ya: Long) = Button(xa, ya, 3)
fun buttonB(xb: Long, yb: Long) = Button(xb, yb, 1)

fun Button.press(times: Long): Prize = Prize(x, y)*times

fun Prize.equal(other: Prize) = x == other.x && y == other.y

data class Prize(val x: Long, val y: Long)

operator fun Prize.times(times: Long) = Prize(x*times, y*times)

operator fun Prize.plus(other: Prize) = Prize(x+other.x, y+other.y)

fun Prize.maxTries(button: Button): Long = maxOf(x/button.x, y/button.y)

data class Game(val buttonA: Button, val buttonB: Button, val prize: Prize)

fun createButton(s: String): Button = s.split(", ").map { it.split("+")[1].toLong() }.let {
        (x, y) -> if (s.contains("A")) buttonA(x,y) else buttonB(x,y) }

fun createPrize(s: String): Prize = s.split(", ").map { it.split("=")[1].toLong() }.let {
        (x, y) -> Prize(x, y) }

fun Pair<Long, Long>.cost() = first*3 + second

class Day13 : Day {
    override val day: Int = 13


    val testString = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent()

    fun parseText(s: String): List<Game> = s.split("\n\n").map {
        gameText -> gameText.split("\n").let {
            Game(createButton(it[0]), createButton(it[1]), createPrize(it[2]))
        }
    }

    fun parseText2(s: String): List<Game> = s.split("\n\n").map {
            gameText -> gameText.split("\n").let {
        Game(createButton(it[0]), createButton(it[1]), createPrize(it[2])*10000000000000)
    }
    }

    fun solveGame(game: Game): List<Pair<Long, Long>> =
        kotlin.runCatching { buildList {
            for (i in game.prize.maxTries(game.buttonA) downTo 0) {
                for (j in game.prize.maxTries(game.buttonB) downTo 0) {
                    if (game.prize.equal(game.buttonA.press(i) + game.buttonB.press(j))) {
                        add(i to j)
                    }
                }
            }
        }
        }.getOrDefault(emptyList())

    fun solvePart1(s: String) = parseText(s).map { game -> runCatching { solveGame(game).minOf { it.cost() } }.getOrDefault(0) }.sum()
    fun solvePart2(s: String) = parseText2(s).map { game -> runCatching { solveGame(game).minOf { it.cost() } }.getOrDefault(0) }.sum()

    override fun solvePart1(): Any {
        return solvePart1(getFileText())
    }

    override fun solvePart2(): Any = solvePart2(getFileText())

}