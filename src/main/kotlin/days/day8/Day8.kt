package org.example.days.day8

import org.example.days.Day

class Day8: Day {

    override val day: Int = 8

    data class Location(
        val x: Int,
        val y: Int,
        val value: Char
    )

    fun Location.antenna(): Boolean = value != '.'

    fun mapSize(s: String): Pair<Int, Int> = s.split('\n').let { it[0].length to it.size }

    fun digestString(s: String): List<Location> = buildList { s.split('\n').mapIndexed {
            y, row -> row.mapIndexed { x, value -> add(Location(x, y, value)) }
        } }

    fun antennaTypes(input: List<Location>): Set<Char> = buildSet { input.filter { it.antenna() }.map { add(it.value) } }

    fun antennaLocations(input: List<Location>, antennaType: Char): List<Location> = input.filter { it.antenna() && it.value == antennaType }

    fun antiNodesPart1(a: Location, b: Location): List<Location> = buildList {
        val xDistance = b.x - a.x
        val yDistance = b.y - a.y
        add(Location(a.x - xDistance, a.y - yDistance, '#'))
        add(Location(b.x + xDistance, b.y + yDistance, '#'))
    }


    fun antiNodesPart2(a: Location, b: Location): List<Location> = buildList {
        val xDistance = b.x - a.x
        val yDistance = b.y - a.y
        for(i in 0 until 50) {
            add(Location(a.x - (xDistance * i), a.y - (yDistance * i), '#'))
            add(Location(b.x + (xDistance * i), b.y + (yDistance * i), '#'))
        }
    }

    fun locationInMap(location: Location, mapSize: Pair<Int, Int>): Boolean = location.x in 0 until mapSize.first && location.y in 0 until mapSize.second

    fun run(input: String, calculateAntiNodes: (Location, Location) -> List<Location>) =
        buildSet {
            val mapSize = mapSize(input)
            val locations = digestString(input)
            for (antennaType in antennaTypes(locations)) {
                val antennaLocations = antennaLocations(locations, antennaType)
                for (a in antennaLocations) {
                    for (b in antennaLocations) {
                        if (a != b) {
                            for (antiNode in calculateAntiNodes(a, b))
                                if (locationInMap(antiNode, mapSize)) {
                                    add(antiNode)
                                }
                        }
                        }
                    }
                }
        }

    override fun solvePart1(): Any {
        return run(getFileText()){ a, b -> antiNodesPart1(a,b) }.count()
    }

    override fun solvePart2(): Any {
        return run(getFileText()){ a, b -> antiNodesPart2(a,b) }.count()
    }

}