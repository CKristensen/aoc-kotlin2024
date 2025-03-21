package org.example.days.day15

import org.example.days.Day


enum class BlockType {
    WALL, EMPTY, ROBOT, BOX
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun Char.toDirection(): Direction {
    return when (this) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        else -> throw IllegalArgumentException("Invalid direction")
    }
}

class Block(val type: BlockType, val x: Int, val y: Int)

typealias MapSize = Pair<Int, Int>

fun Pair<Int, Int>.move1(direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.LEFT -> first - 1 to second
        Direction.RIGHT -> first + 1 to second
        Direction.UP -> first to second - 1
        Direction.DOWN -> first to second + 1
    }
}


fun Pair<Float, Float>.move2(direction: Direction): Pair<Float, Float> =
    when (direction) {
        Direction.LEFT -> first.toFloat() - 0.5f to second
        Direction.RIGHT -> first.toFloat() + 0.5f to second
        Direction.UP -> first to second.toFloat() - 0.5f
        Direction.DOWN -> first to second.toFloat() + 0.5f
    }

class MapLan(val size: MapSize, val blocks: List<Block>) {
    fun getBlock(x: Int, y: Int): Block? {
        return blocks.find { it.x == x && it.y == y }
    }

    fun getBlock(pair: Pair<Int, Int>): Block? {
        return getBlock(pair.first, pair.second)
    }

    fun canMoveTo(x: Int, y: Int, direction: Direction): Boolean {
        when(getBlock((x to y).move1(direction))?.type) {
            BlockType.EMPTY -> return true
            BlockType.WALL -> return false
            BlockType.BOX -> {
                val (boxX, boxY) = (x to y).move1(direction)
                return canMoveTo(boxX, boxY, direction)
            }
            null -> return false
            else -> return true
        }
    }

    fun moveBlockDirection(b: Pair<Int, Int>, direction: Direction): MapLan {
        val (x, y) = b
        val blockToMove = blocks.find {
            it.x == x && it.y == y
        } ?: throw IllegalArgumentException("No robot found")
        val (newX, newY) = (x to y).move1(direction)
        if (canMoveTo(x, y, direction)) {
            val nBlocks = if (getBlock(newX, newY)?.type == BlockType.BOX) {
                moveBlockDirection(newX to newY, direction).blocks
            }
            else {
                blocks
            }

            val newBlocks = nBlocks.map {
                if (it.x == x && it.y == y) {
                    Block(BlockType.EMPTY, it.x, it.y)
                } else if (it.x == newX && it.y == newY) {
                    Block(blockToMove.type, it.x, it.y)
                } else {
                    it
                }
            }
            return MapLan(size, newBlocks)
        } else {
            return this
        }
    }
}

fun Char.toBlockType(): BlockType {
    return when (this) {
        '#' -> BlockType.WALL
        '.' -> BlockType.EMPTY
        '@' -> BlockType.ROBOT
        'O', '0' -> BlockType.BOX
        else -> throw IllegalArgumentException("Invalid block type")
    }
}

fun Char.isDirection(): Boolean {
    return this in listOf('^', 'v', '<', '>')
}


fun Block.GPS(): Int =if(BlockType.BOX != type) 0 else y*100 + x


fun String.toMapLan(): MapLan = this.split("\n").let {
    val size = MapSize(it[0].length, it.size)
    val blocks = mutableListOf<Block>()
    it.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            blocks.add(Block(c.toBlockType(), x, y))
        }
    }
    MapLan(size, blocks)
}

fun MapLan.getRobotLocation(): Pair<Int, Int> {
    return blocks.find { it.type == BlockType.ROBOT }?.let {
        it.x to it.y
    } ?: throw IllegalArgumentException("No robot found")
}

fun MapLan.visualizeMap(): String = buildString {
    for (y in 0 until size.second) {
        for (x in 0 until size.first) {
            append(blocks.find { it.x == x && it.y == y }?.let {
                when (it.type) {
                    BlockType.WALL -> '#'
                    BlockType.EMPTY -> '.'
                    BlockType.ROBOT -> '@'
                    BlockType.BOX -> 'O'
                }
            } ?: ' ')
        }
        append("\n")
    }
}

class Day15 : Day {

    fun getInput(s: String): Pair<MapLan, List<Direction>> = s.split("\n\n").let { strings ->
        assert(strings.size == 2)
        strings[0].toMapLan() to strings[1].filter { it.isDirection() }.map { it.toDirection() }
    }

    fun solvePart1(s: String) =
    getInput(s).let { (map, directions) ->
        directions
            .fold(map) { acc, direction -> acc.moveBlockDirection(acc.getRobotLocation(), direction) }
            .blocks
            .sumOf { it.GPS() }
    }

    override fun solvePart1(): Any = solvePart1(getFileText())

    override fun solvePart2(): Any {
        return 0
    }

    override val day: Int = 15
}

