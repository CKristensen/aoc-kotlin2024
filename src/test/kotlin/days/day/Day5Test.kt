package days.day

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.days.day4.Day4
import org.example.days.day5.Day5

class Day5Test : FunSpec({

    val day5 = Day5()

    test("Day 4, Part 1") {
        val testString =
            """
                47|53
                97|13
                97|61
                97|47
                75|29
                61|13
                75|53
                29|13
                97|29
                53|29
                61|53
                97|53
                61|29
                47|13
                75|47
                97|75
                47|61
                75|61
                47|29
                75|13
                53|13
                
                75,47,61,53,29
                97,61,53,29,13
                75,29,13
                75,97,47,61,53
                61,13,29
                97,13,75,29,47
            """.trimIndent()

        day5.solvePart1(testString).shouldBe(143)
        day5.getMiddleNumber(listOf(1, 2, 3, 4, 5)).shouldBe(3)
    }
})
