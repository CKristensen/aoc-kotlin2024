package days.day

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.days.day4.Day4

class Day4Test : FunSpec({

    val day4 = Day4()

    test("Day 4, Part 1") {
        val testString =
            """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """.trimIndent()

        day4.solvePart1(testString).shouldBe(18)
    }
})
