package days.day

import io.kotest.core.spec.style.FunSpec
import org.example.days.day12.Day12
import kotlin.test.assertEquals

class Day12Test : FunSpec({
    val day12 = Day12()
    test("Day 12, Part 1"){
        val example = """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
        """.trimIndent()
        assertEquals(1930, day12.solvePart1(example))




        assertEquals(1206, day12.solvePart2(example))
    }

})

