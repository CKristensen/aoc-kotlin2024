package days.day

import io.kotest.core.spec.style.FunSpec
import org.example.days.day9.Day9
import org.example.days.day9.asString
import org.example.days.day9.getResult
import org.example.days.day9.toListOfBits
import kotlin.test.assertEquals

class Day9Test : FunSpec({
    val day9 = Day9()

    test("Day 2, Part2") {
        val initialString = "2333133121414131402"
        val finalString = "00992111777.44.333....5555.6666.....8888.."
        println(day9.parseInputToBlocks(initialString).asString())

        assertEquals(finalString, day9.defragPart2(day9.parseInputToBlocks(initialString)).asString())
        assertEquals(2858, day9.defragPart2(day9.parseInputToBlocks(initialString)).toListOfBits().getResult())
    }
})
