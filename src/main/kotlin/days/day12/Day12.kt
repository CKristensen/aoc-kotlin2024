package org.example.days.day12

import org.example.days.Day
import kotlin.math.abs

typealias Grid = List<FPoint>

data class FPoint(val flower: Char, val x: Int, val y: Int){
    fun isAdjacentTo(other: FPoint): Boolean = (abs(x - other.x) + abs(y - other.y) == 1)
}

typealias Region = Set<FPoint>

fun Region.isUpperRightOuterCorner(fp: FPoint) = this.none { it.x == fp.x + 1 && it.y == fp.y } && this.none { it.x == fp.x && it.y == fp.y + 1 }
fun Region.isUpperLeftOuterCorner(fp: FPoint) = this.none { it.x == fp.x - 1 && it.y == fp.y } && this.none { it.x == fp.x && it.y == fp.y + 1 }
fun Region.isLowerRightOuterCorner(fp: FPoint) = this.none { it.x == fp.x + 1 && it.y == fp.y } && this.none { it.x == fp.x && it.y == fp.y - 1 }
fun Region.isLowerLeftOuterCorner(fp: FPoint) = this.none { it.x == fp.x - 1 && it.y == fp.y } && this.none { it.x == fp.x && it.y == fp.y - 1 }
fun Region.isUpperRightInnerCorner(fp: FPoint) = this.none { it.x == fp.x + 1 && it.y == fp.y +1 } && this.any { it.x == fp.x + 1 && it.y == fp.y } && this.any { it.x == fp.x && it.y == fp.y + 1 }
fun Region.isUpperLeftInnerCorner(fp: FPoint) = this.none { it.x == fp.x - 1 && it.y == fp.y +1 } && this.any { it.x == fp.x - 1 && it.y == fp.y } && this.any { it.x == fp.x && it.y == fp.y + 1 }
fun Region.isLowerRightInnerCorner(fp: FPoint) = this.none { it.x == fp.x + 1 && it.y == fp.y -1 } && this.any { it.x == fp.x + 1 && it.y == fp.y } && this.any { it.x == fp.x && it.y == fp.y - 1 }
fun Region.isLowerLeftInnerCorner(fp: FPoint) = this.none { it.x == fp.x - 1 && it.y == fp.y -1 } && this.any { it.x == fp.x - 1 && it.y == fp.y } && this.any { it.x == fp.x && it.y == fp.y - 1 }
fun Region.countEdges(): Int =
    count { isUpperRightOuterCorner(it) } + count { isUpperLeftOuterCorner(it) } + count { isLowerRightOuterCorner(it) } + count { isLowerLeftOuterCorner(it) } +
        count { isUpperRightInnerCorner(it) } + count { isUpperLeftInnerCorner(it) } + count { isLowerRightInnerCorner(it) } + count { isLowerLeftInnerCorner(it) }

fun Region.sides(): Int = this.countEdges() // spacesUntilEmpty(this to 1).let {2*it*it-4*it+6}

fun spaces(region: Region): Set<FPoint> =
    buildSet {
        for (i in region.minOf { it.x }..region.maxOf { it.x }) {
            for (j in region.minOf { it.y }..region.maxOf { it.y }) {
                if (!region.any { it.x == i && it.y == j }) add(FPoint(' ', i, j))
            }
        }
    }

fun Region.isAdjacentTo(other: Region): Boolean = this.any { p -> other.any { p.flower == it.flower && (p.isAdjacentTo(it)) } }

fun Grid.maxY(): Int = this.maxOf { it.y }

fun Grid.maxX(): Int = this.maxOf { it.x }

fun Grid.internalPerimeter(p: FPoint): Int = this.filter { it.isAdjacentTo(p) && it.flower != p.flower }.size

fun Grid.edgePerimeter(p: FPoint): Int = listOf(this.maxX() == p.x, this.maxY() == p.y,  p.x == 0, p.y == 0).count { it }

fun Grid.perimeter(p: FPoint): Int = this.internalPerimeter(p) + this.edgePerimeter(p)

fun getGrid(s: String): Grid = s.split("\n").mapIndexed { y, line ->
    line.mapIndexed { x, c ->
        FPoint(c, x, y)
    }
}.flatten()

fun Grid.getPrice(r: Region): Int = r.map { this.perimeter(it) }.sum() * r.size

fun getPric2(r: Region): Int = r.sides() * r.size

val mergeRecursive: DeepRecursiveFunction<List<Region>, List<Region>> =  DeepRecursiveFunction{ initRegions ->
    initRegions.fold(emptyList<Region>()) { acc, region -> addRegionToMatchingRegionOrAddNew(region, acc) }
        .let { regionss ->
        when(regionss.size){
            initRegions.size -> regionss
            else -> callRecursive(regionss)
        }
    }
}

fun addRegionToMatchingRegionOrAddNew(region: Region, regions: List<Region>): List<Region> =
    if(region.any {
            r -> regions.any { it.any { it.flower == r.flower && it.isAdjacentTo(r) } }
        }) regions.map { r -> if(r.isAdjacentTo(region)) r.union(region) else r }
    else regions + listOf(region)

class Day12: Day {
    override val day: Int = 12

    fun solvePart1(s: String): Int =
        getGrid(s)
            .let { grid ->
                mergeRecursive(grid.map { setOf(it) }).sumOf { grid.getPrice(it) }
                }

    override fun solvePart1(): Int = solvePart1(getFileText())

    fun solvePart2(s: String): Int =
        getGrid(s)
            .let { grid ->
                mergeRecursive(grid.map { setOf(it) }).sumOf { getPric2(it) }
            }

    override fun solvePart2(): Int {
        return solvePart2(getFileText())
    }

}