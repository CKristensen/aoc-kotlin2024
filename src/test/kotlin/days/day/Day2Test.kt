package days.day

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.days.day2.Day2

class Day2Test : FunSpec({
    val day2 = Day2()

    test("Day 2, aux functions") {
        day2.isAscending(listOf(1, 2, 3, 4)) shouldBe true
        day2.isAscending(listOf(1, 2, 3, 2)) shouldBe false
        day2.isDescending(listOf(4, 3, 2, 1)) shouldBe true
        day2.isDescending(listOf(4, 3, 2, 4)) shouldBe false
        day2.atMostThreeLevelDifference(listOf(1, 2, 3, 4)) shouldBe true
        day2.atLeastOneLevelDifference(listOf(1, 2, 3, 4)) shouldBe true
        day2.atMostThreeLevelDifference(listOf(1, 2, 3, 5)) shouldBe true
        day2.atLeastOneLevelDifference(listOf(1, 2, 3, 5)) shouldBe true
        day2.atLeastOneLevelDifference(listOf(1, 1, 3, 5)) shouldBe false
        day2.atMostThreeLevelDifference(listOf(1, 2, 3, 7)) shouldBe false
        day2.sequenceOfListsRemovingOneElement(listOf(1, 2, 3)).toList() shouldBe listOf(listOf(2, 3), listOf(1, 3), listOf(1, 2))
    }

    test("Day 2, Part 1") {
        day2.isSafe(listOf(7, 6, 4, 2, 1)) shouldBe true
        day2.isSafe(listOf(1, 2, 7, 8, 9)) shouldBe false
        day2.isSafe(listOf(9, 7, 6, 2, 1)) shouldBe false
        day2.isSafe(listOf(1, 3, 2, 4, 5)) shouldBe false
        day2.isSafe(listOf(8, 6, 4, 4, 1)) shouldBe false
        day2.isSafe(listOf(1, 3, 6, 7, 9)) shouldBe true
    }

    test("Day 2, Part 2") {
        day2.isSafe2(listOf(7, 6, 4, 2, 1)) shouldBe true
        day2.isSafe2(listOf(1, 2, 7, 8, 9)) shouldBe false
        day2.isSafe2(listOf(9, 7, 6, 2, 1)) shouldBe false
        day2.isSafe2(listOf(1, 3, 2, 4, 5)) shouldBe true
        day2.isSafe2(listOf(8, 6, 4, 4, 1)) shouldBe true
        day2.isSafe2(listOf(1, 3, 6, 7, 9)) shouldBe true
    }
})
