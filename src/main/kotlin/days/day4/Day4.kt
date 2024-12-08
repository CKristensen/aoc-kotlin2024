package org.example.days.day4

import org.example.days.Day

class Day4 : Day {
    override val day: Int = 4

    fun getVerticalLines(s: String) = s.split("\n")

    fun getHorizontalLines(s: String): List<String> {
        val v = getVerticalLines(s)
        val leng = v.size
        val h = mutableListOf<String>()
        for (i in 0 until leng) {
            var line = ""
            for (j in 0 until v.size) {
                line += v[j][i]
            }
            h.add(line)
        }
        return h.toList()
    }

    fun getDiagonalPairs(s: String): List<List<Pair<Int, Int>>> {
        val height = s.split("\n").size
        val length = s.split("\n")[0].length
        val pairs = mutableListOf<List<Pair<Int, Int>>>()
        var pairlist = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until length) {
            var x = i
            var y = 0
            pairlist = mutableListOf()
            while (x < length && y < height) {
                pairlist.add(Pair(x, y))
                x++
                y++
            }
            pairs.add(pairlist)
        }
        for (i in 1 until height) {
            var x = 0
            var y = i
            pairlist = mutableListOf()
            while (x < length && y < height) {
                pairlist.add(Pair(x, y))
                x++
                y++
            }
            pairs.add(pairlist)
        }
        return pairs.toSet().toList()
    }

    fun getDiagonalLines(s: String): List<String> {
        val pairs = getDiagonalPairs(s)
        val lines = mutableListOf<String>()
        for (pairlist in pairs) {
            var line = ""
            for (pair in pairlist) {
                line += s.split("\n")[pair.second][pair.first]
            }
            lines.add(line)
        }
        return lines.toList()
    }

    fun removeIrreleventLesster(s: List<String>): List<String> = s.map { it.filter { "XMAS".contains(it) } }

    fun countXmas(s: List<String>): Int = s.map { Regex("""XMAS""").findAll(it).count() }.sum()

    fun getAllCombinationsPart1(s: String) =
        listOf(
            getVerticalLines(s),
            getVerticalLines(s).map { it.reversed() },
            getHorizontalLines(s),
            getHorizontalLines(s).map { it.reversed() },
            getDiagonalLines(s),
            getDiagonalLines(s).map { it.reversed() },
            getDiagonalLines(s.split("\n").joinToString("\n") { it.reversed() }),
            getDiagonalLines(s.split("\n").joinToString("\n") { it.reversed() }).map { it.reversed() },
        )

    fun solvePart1(s: String): Int = getAllCombinationsPart1(s).map { removeIrreleventLesster(it) }.sumOf { countXmas(it) } ?: 0

    override fun solvePart1(): Any = solvePart1(getFileText())

    private fun getAll3by3Blocks(s: String): List<List<Pair<Int, Int>>> {
        val height = s.split("\n").size
        val length = s.split("\n")[0].length
        val result = mutableListOf<List<Pair<Int, Int>>>()
        for (i in 0 until length) {
            for (j in 0 until height) {
                if (i + 2 < length && j + 2 < height) {
                    result.add(
                        listOf(
                            Pair(
                                i,
                                j,
                            ),
                            Pair(
                                i + 1,
                                j,
                            ),
                            Pair(
                                i + 2,
                                j,
                            ),
                            Pair(
                                i,
                                j + 1,
                            ),
                            Pair(i + 1, j + 1), Pair(i + 2, j + 1), Pair(i, j + 2), Pair(i + 1, j + 2), Pair(i + 2, j + 2),
                        ),
                    )
                }
            }
        }
        return result.filter { it.all { it.first < length && it.second < height } }
    }

    fun isItXmasBlock(
        s: String,
        blockCoordinates: List<Pair<Int, Int>>,
    ): Boolean =
        s.split("\n").let {
                matrix ->
            (
                (
                    blockCoordinates[0].let { matrix[it.first][it.second] == 'M' } &&
                        blockCoordinates[4].let { matrix[it.first][it.second] == 'A' } &&
                        blockCoordinates[8].let { matrix[it.first][it.second] == 'S' }
                ) ||
                    (
                        blockCoordinates[0].let { matrix[it.first][it.second] == 'S' } &&
                            blockCoordinates[4].let { matrix[it.first][it.second] == 'A' } &&
                            blockCoordinates[8].let { matrix[it.first][it.second] == 'M' }
                    )

            ) &&
                (
                    (
                        blockCoordinates[2].let { matrix[it.first][it.second] == 'M' } &&
                            blockCoordinates[4].let { matrix[it.first][it.second] == 'A' } &&
                            blockCoordinates[6].let { matrix[it.first][it.second] == 'S' }
                    ) || (

                        blockCoordinates[2].let { matrix[it.first][it.second] == 'S' } &&
                            blockCoordinates[4].let { matrix[it.first][it.second] == 'A' } &&
                            blockCoordinates[6].let { matrix[it.first][it.second] == 'M' }

                    )
                )
        }

    fun solvePart2(s: String) = getAll3by3Blocks(s).count { isItXmasBlock(s, it) }

    override fun solvePart2(): Any = solvePart2(getFileText())
}
