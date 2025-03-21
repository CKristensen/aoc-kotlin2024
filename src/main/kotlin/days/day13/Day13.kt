package org.example.days.day13

import org.example.days.Day
import java.math.BigInteger

data class Button(val x: Long, val y: Long, val cost: Long)
fun buttonA(xa: Long, ya: Long) = Button(xa, ya, 3)
fun buttonB(xb: Long, yb: Long) = Button(xb, yb, 1)

fun Button.press(times: Long): Prize = Prize(x, y)*times

fun Prize.equal(other: Prize) = x == other.x && y == other.y

data class Prize(val x: Long, val y: Long)

operator fun Prize.times(times: Long) = Prize(x*times, y*times)

operator fun Prize.plus(other: Prize) = Prize(x+other.x, y+other.y)

fun Prize.lessThan(other: Prize) = x < other.x || y < other.y
fun Prize.moreThan(other: Prize) = x > other.x || y > other.y

fun Prize.maxTries(button: Button): Long = minOf(x/button.x, y/button.y)

data class Game(val buttonA: Button, val buttonB: Button, val prize: Prize)

fun createButton(s: String): Button = s.split(", ").map { it.split("+")[1].toLong() }.let {
        (x, y) -> if (s.contains("A")) buttonA(x,y) else buttonB(x,y) }

fun createPrize(s: String): Prize = s.split(", ").map { it.split("=")[1].toLong() }.let {
        (x, y) -> Prize(x, y) }

fun createPrize2(s: String): Prize = createPrize(s).let { (x, y) -> Prize(x+10000000000000, y+10000000000000) }

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

    fun Game.gcd() = listOf(prize.x, prize.y, buttonA.x, buttonA.y, buttonB.x, buttonB.y).map { it.toBigInteger() }.reduce { acc, bigInteger -> acc.gcd(bigInteger) }.toLong()

    fun Game.applyGcd(): Game {
        val gcd = gcd()
        if (gcd == 1L) return this
        return Game(
            Button(buttonA.x/gcd, buttonA.y/gcd, buttonA.cost),
            Button(buttonB.x/gcd, buttonB.y/gcd, buttonB.cost),
            Prize(prize.x/gcd, prize.y/gcd)
        )
    }

    fun parseText(s: String): List<Game> = s.split("\n\n").map {
        gameText -> gameText.split("\n").let {
            Game(createButton(it[0]), createButton(it[1]), createPrize(it[2]))
        }
    }

    fun parseText2(s: String): List<Game> = s.split("\n\n").map {
            gameText -> gameText.split("\n").let {
        Game(createButton(it[0]), createButton(it[1]), createPrize2(it[2]))
    }
    }

    //
    // A = (p_x*b_y - prize_y*b_x) / (a_x*b_y - a_y*b_x)
    // B = (a_x*p_y - a_y*p_x) / (a_x*b_y - a_y*b_x)
    fun cramersRule(game: Game): Pair<Long, Long> {
        val px = game.prize.x
        val py = game.prize.y
        val ax = game.buttonA.x
        val ay = game.buttonA.y
        val bx = game.buttonB.x
        val by = game.buttonB.y
        val A = (px*by - py*bx) / (ax*by - ay*bx)
        val B = (ax*py - ay*px) / (ax*by - ay*bx)
        if(game.prize.equal(game.buttonA.press(A) + game.buttonB.press(B))) {
            return A to B
        }
        return 0L to 0L
    }



    fun solveGame2(game: Game): Pair<Long, Long> {

        val gameGcd = game.applyGcd()

        val buttonBMaxTries = gameGcd.prize.maxTries(gameGcd.buttonB)
        val buttonAMaxTries = gameGcd.prize.maxTries(gameGcd.buttonA)
        fun buttonBMaxResult(buttonBTries: Long) = gameGcd.buttonA.press(gameGcd.prize.maxTries(gameGcd.buttonA)) + gameGcd.buttonB.press(buttonBTries)
        fun buttonAMinTries(buttonBTries: Long) = maxOf(gameGcd.buttonB.press(-buttonBTries).maxTries(gameGcd.buttonA), 0)

        for (paB in buttonBMaxTries downTo 0) {
            val maxResult = buttonBMaxResult(paB)
            if (gameGcd.prize.moreThan(maxResult)) {
                break
            }
            for (pbA in buttonAMinTries(paB)..buttonAMaxTries) {
                val result = gameGcd.buttonA.press(pbA) + gameGcd.buttonB.press(paB)
                if (gameGcd.prize.lessThan(result)) {
                    break
                }
                if (gameGcd.prize.equal(result)) {
                    return pbA to paB
                }
            }
            }
            return 0L to 0L
        }

    fun solvePart1(s: String) =
        parseText(s).sumOf { game -> runCatching { cramersRule(game).cost() }.getOrDefault(0) }

    fun solvePart2(s: String) =
        parseText2(s).sumOf { game -> runCatching { cramersRule(game).cost() }.getOrDefault(0) }

    override fun solvePart1(): Any {
        return solvePart1(getFileText())
    }

    override fun solvePart2(): Any = solvePart2(getFileText())

}