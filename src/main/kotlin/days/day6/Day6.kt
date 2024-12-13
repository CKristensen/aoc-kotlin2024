package org.example.days.day6

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.example.days.Day

enum class Pixel {
    EMPTY,
    WALL,
    GUARD_UP,
    GUARD_DOWN,
    GUARD_LEFT,
    GUARD_RIGHT,
    GUARD_PATH,
}

fun Pixel.isGuard(): Boolean =
    when (this) {
        Pixel.GUARD_UP, Pixel.GUARD_DOWN, Pixel.GUARD_LEFT, Pixel.GUARD_RIGHT -> true
        else -> false
    }

fun Pixel.isEmpty(): Boolean = this == Pixel.EMPTY

fun getGuardPosition(board: List<List<Pixel>>): Position {
    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j].isGuard()) return i to j
        }
    }
    throw IllegalArgumentException("No guard found")
}

typealias Position = Pair<Int, Int>

fun Position.up(): Position = this.first - 1 to this.second

fun Position.down(): Position = this.first + 1 to this.second

fun Position.left(): Position = this.first to this.second - 1

fun Position.right(): Position = this.first to this.second + 1

class Day6 : Day {
    override val day: Int = 6

    class Board(var board: List<MutableList<Pixel>>) {
        val guardPath: MutableList<Pair<Int, Int>>
        val lastWallPositions: MutableList<Pair<Position, Position>>

        init {
            this.guardPath = mutableListOf(getGuardPosition(board))
            this.lastWallPositions = mutableListOf()
        }

        override fun toString(): String {
            return board.joinToString("\n") { it.joinToString("") }
        }

        fun nextPosition(currentPos: Position): Position {
            return when (this.board.get(currentPos)) {
                Pixel.GUARD_UP -> currentPos.up()
                Pixel.GUARD_DOWN -> currentPos.down()
                Pixel.GUARD_LEFT -> currentPos.left()
                Pixel.GUARD_RIGHT -> currentPos.right()
                else -> throw IllegalArgumentException("Invalid guard: ${board.get(currentPos)}")
            }
        }

        fun rotateGuard90degrees(guard: Pixel) =
            when (guard) {
                Pixel.GUARD_UP -> Pixel.GUARD_RIGHT
                Pixel.GUARD_RIGHT -> Pixel.GUARD_DOWN
                Pixel.GUARD_DOWN -> Pixel.GUARD_LEFT
                Pixel.GUARD_LEFT -> Pixel.GUARD_UP
                else -> throw IllegalArgumentException("Invalid guard: $guard")
            }

        fun isEmpty(position: Pair<Int, Int>): Boolean = position.let { (x, y) -> board.get(x to y) == Pixel.EMPTY }

        fun insideBoard(position: Pair<Int, Int>) = position.let { (x, y) -> x >= 0 && x < board.size && y >= 0 && y < board[0].size }

        fun isGuardPathALoop(): Boolean = (lastWallPositions.distinct().size < lastWallPositions.size)

        fun placeBlockInEachPath(path: List<Pair<Int, Int>>): Flow<Board> =
            flow {
                for (pixel in path.distinct()) {
                    val (i, j) = pixel
                    val newBoard = board.map { it.toMutableList() }
                    if (newBoard.get(i to j).isGuard())
                        {
                            continue
                        }
                    newBoard.set(i to j, Pixel.WALL)
                    emit(Board(newBoard))
                }
            }
    }

    fun createBoard(s: String): Board =
        Board(
            s.split("\n").map { line ->
                line.map { char ->
                    when (char) {
                        '.' -> Pixel.EMPTY
                        '#' -> Pixel.WALL
                        '^' -> Pixel.GUARD_UP
                        '>' -> Pixel.GUARD_RIGHT
                        'v' -> Pixel.GUARD_DOWN
                        '<' -> Pixel.GUARD_LEFT
                        else -> throw IllegalArgumentException("Invalid character: $char")
                    }
                }.toMutableList()
            },
        )

    fun solvePart1(s: String): Int =
        createBoard(s).let {
            moveBoardR(it).guardPath.distinct().size
        }

    override fun solvePart1(): Int = solvePart1(getFileText())

    val moveBoardRD =
        DeepRecursiveFunction<Pair<Board, Position>, Board> {
                (rboard, pos) ->
            val currentPosition = getGuardPosition(rboard.board)
            val nextPosition = rboard.nextPosition(currentPosition)
            if (
                rboard.insideBoard(nextPosition).not() ||
                (rboard.isGuardPathALoop())
            ) {
                rboard
            } else if (rboard.isEmpty(nextPosition).not() || (nextPosition == pos)) {
                rboard.lastWallPositions.add(currentPosition to nextPosition)
                rboard.board.set(currentPosition, rboard.rotateGuard90degrees(rboard.board.get(currentPosition)))
                callRecursive(rboard to pos)
            } else {
                rboard.board.set(nextPosition, rboard.board.get(currentPosition))
                rboard.board.set(currentPosition, Pixel.EMPTY)
                rboard.guardPath.add(nextPosition)
                callRecursive(rboard to pos)
            }
            rboard
        }

    val moveBoardR =
        DeepRecursiveFunction<Board, Board> {
                rboard ->
            val currentPosition = getGuardPosition(rboard.board)
            val nextPosition = rboard.nextPosition(currentPosition)
            if (
                rboard.insideBoard(nextPosition).not() ||
                (rboard.isGuardPathALoop())
            ) {
                rboard
            } else if (rboard.isEmpty(nextPosition).not()) {
                rboard.lastWallPositions.add(currentPosition to nextPosition)
                rboard.board.set(currentPosition, rboard.rotateGuard90degrees(rboard.board.get(currentPosition)))
                callRecursive(rboard)
            } else {
                rboard.board.set(nextPosition, rboard.board.get(currentPosition))
                rboard.board.set(currentPosition, Pixel.EMPTY)
                rboard.guardPath.add(nextPosition)
                callRecursive(rboard)
            }
            rboard
        }

    fun solvePart2Stupid(s: String): String {
        var count = 0

        runBlocking {
            val initPath = createBoard(s).let { moveBoardR(it).guardPath }
            createBoard(s)
                .placeBlockInEachPath(initPath)
                .filter {
                    moveBoardR(it).isGuardPathALoop()
                }
                .collect {
                    count++
                }
        }
        return count.toString()
    }

    fun solvePart2(s: String): Int =
        runBlocking {
            createBoard(s).let { moveBoardR(it).guardPath }.count { it: Position -> moveBoardRD(createBoard(s) to it).isGuardPathALoop() }
        }

    override fun solvePart2(): String = solvePart2(getFileText()).toString()
}

fun <E> List<MutableList<E>>.get(index: Pair<Int, Int>): E {
    val (i, j) = index
    return this[i][j]
}

fun <E> List<MutableList<E>>.set(
    index: Pair<Int, Int>,
    s: E,
) {
    val (i, j) = index
    this[i][j] = s
}
