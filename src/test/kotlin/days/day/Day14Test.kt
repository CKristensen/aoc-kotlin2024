package days.day

import io.kotest.core.spec.style.FunSpec
import org.example.days.day14.*
import kotlin.test.assertEquals

class Day14Test :FunSpec({
    context("Day 14") {
        test("Part 1") {
            val s = """
                p=0,4 v=3,-3
                p=6,3 v=-1,-3
                p=10,3 v=-1,2
                p=2,0 v=2,-1
                p=0,0 v=1,3
                p=3,0 v=-2,-2
                p=7,6 v=-1,-3
                p=3,0 v=-1,-2
                p=9,3 v=2,3
                p=7,3 v=-1,2
                p=2,4 v=2,-3
                p=9,5 v=-3,-3
            """.trimIndent()
           val day14 = Day14()

            val guard1 = Guard(Position(2, 4), Velocity(2, -3))
            assertEquals(walk(guard1, 5, Pair(11, 7)), Position(1, 3))
            //assertEquals(day14.solvePart1(s, Pair(11, 7), 10) , 12)
        }
        test("Part 2") {
            val guardsOverlapping = listOf(Guard(Position(1,1), Velocity(1,2)), Guard(Position(1,1), Velocity(1,2)))
            val guardsOverlappingNot = listOf(Guard(Position(2,1), Velocity(1,2)), Guard(Position(1,1), Velocity(1,2)))
            assertEquals(noGuardOverlapping(guardsOverlapping), false)
            assertEquals(noGuardOverlapping(guardsOverlappingNot), true)
        }
    }
})