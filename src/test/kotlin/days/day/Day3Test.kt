package days.day

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.days.day3.Day3

class Day3Test : FunSpec({

    val day3 = Day3()

    test("Day 3, Part 2") {
        val s = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

        day3.solvePart2(s) shouldBe 48
    }
})
