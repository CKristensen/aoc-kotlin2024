package days.day

import io.kotest.core.spec.style.FunSpec
import org.example.days.day10.Day10
import org.example.days.day10.getAdjacentPoints
import org.example.days.day10.getStartingPoints
import kotlin.test.assertEquals

class Day10Test : FunSpec({
    val day10 = Day10()

    test("Day 10, Part1") {
        val example = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
        """.trimIndent()
        //println(day10.map(example).getStartingPoints().size == example.filter { it == '0' }.length)
        day10.map(example).let { grid ->
        val tesPoint = grid.getStartingPoints().first()
        assertEquals(0, tesPoint.height)
        assertEquals(2, tesPoint.x)
        assertEquals(0, tesPoint.y)
        assertEquals(1, tesPoint.distanceTo(grid[1]))
        assertEquals(2, grid.getAdjacentPoints(tesPoint).size)
    }
    }

    test("Day 10, Part 2"){

        val example = """
                            9999909
                            9943219
                            9959929
                            9965439
                            1171141
                            1187651
                            1191111
                            """.trimIndent()

        assertEquals(3, day10.solvePart2(example))



        val example2 = """
            3390339
            3331398
            1112117
            6543456
            7651987
            8761111
            9871111
        """.trimIndent()
        assertEquals(13, day10.solvePart2(example2))

    }
})
