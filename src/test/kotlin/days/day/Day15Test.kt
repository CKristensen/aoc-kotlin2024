package days.day

import io.kotest.core.spec.style.FunSpec
import org.example.days.day15.Day15
import org.example.days.day15.GPS
import org.example.days.day15.getRobotLocation
import org.example.days.day15.toMapLan
import org.example.days.day15.visualizeMap
import kotlin.test.assertEquals

class Day15Test :FunSpec({
    context("Day 15") {
        test("GPS") {
            val s = """
                #######
                #...O..
                #......
            """.trimIndent()
            assertEquals(s.toMapLan().blocks.sumOf { it.GPS() }, 104)
        }
        test("move right") {

            val ss = """
                ##########
                #.O.O.OOO#
                #........#
                #OO......#
                #OO@.....#
                #O#.....O#
                #O.....OO#
                #O.....OO#
                #OO....OO#
                ##########
                
                >>>>>
            """.trimIndent()

            val ssA = """
                ##########
                #.O.O.OOO#
                #........#
                #OO......#
                #OO.....@#
                #O#.....O#
                #O.....OO#
                #O.....OO#
                #OO....OO#
                ##########
            """.trimIndent()


            val day15 = Day15()

            val sss = day15.getInput(ss).let { (map, directions) ->
                directions
                    .fold(map) { acc, direction -> acc.moveBlockDirection(acc.getRobotLocation(), direction) }
            }

            assertEquals(sss.visualizeMap().trimIndent(), ssA)



        }


        //    val ss = """
        //        ##########
        //        #.O.O.OOO#
        //        #........#
        //        #OO......#
        //        #OO@.....#
        //        #O#.....O#
        //        #O.....OO#
        //        #O.....OO#
        //        #OO....OO#
        //        ##########
        //
        //        >>>>>
        //    """.trimIndent()

        //    val ssA = """
        //        ##########
        //        #.O.O.OOO#
        //        #........#
        //        #OO......#
        //        #OO.....@#
        //        #O#.....O#
        //        #O.....OO#
        //        #O.....OO#
        //        #OO....OO#
        //        ##########
        //    """.trimIndent()

        //    val day15 = Day15()

        //    val sss = day15.getInput(ss).let { (map, directions) ->
        //        directions
        //            .fold(map) { acc, direction -> acc.moveBlockDirection(acc.getRobotLocation(), direction) }
        //    }

        //    assertEquals(sss.visualizeMap().trimIndent(), ssA)


        //    val ss1 = """
        //        ##########
        //        #.O.O.OOO#
        //        #........#
        //        #OO......#
        //        #OO@.....#
        //        #O#O....O#
        //        #O.....OO#
        //        #O.....OO#
        //        #OO....OO#
        //        ##########
        //
        //        vvvvvvvvv
        //    """.trimIndent()

        //    val ss1A = """
        //        ##########
        //        #.O.O.OOO#
        //        #........#
        //        #OO......#
        //        #OO......#
        //        #O#.....O#
        //        #O.....OO#
        //        #O.@...OO#
        //        #OOO...OO#
        //        ##########
        //    """.trimIndent()

        //    val sss1 = day15.getInput(ss1).let { (map, directions) ->
        //        directions
        //            .fold(map) { acc, direction -> acc.moveBlockDirection(acc.getRobotLocation(), direction) }
        //    }

        //    assertEquals(sss1.visualizeMap().trimIndent(), ss1A)

        //    val check1 = """
        //        ########
        //        #..O.O.#
        //        ##@.O..#
        //        #...O..#
        //        #.#.O..#
        //        #...O..#
        //        #......#
        //        ########

        //        <^^>>>vv<v>>v<<
        //    """.trimIndent()

        //    val sol1 = day15.solvePart1(check1)

        //    //assertEquals(sol1, 2028)

        //}
        test("Part 2") {

        }
    }
})
