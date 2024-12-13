package org.example.days.day9

import org.example.days.Day
import java.util.*

data class Bit(
    val value: Long?,
)

fun Bit.asStr(): String = (value ?: ".").toString()

fun List<Bit>.asStr(): String = joinToString("") { it.asStr() }

fun Bit.repeat(n: Int): List<Bit> = List(n) { this }

fun Char.asInt(): Int = this.toString().toInt()

fun List<Bit>.bitsToQueue(): Queue<Bit> = LinkedList(this)

fun List<Bit>.getResult(): Long = this.mapIndexed { index, bit -> bit.value?.let { it * index } ?: 0 }.sum()

fun List<Block>.toListOfBits(): List<Bit> =
    this.flatMap {
        it.value?.let { value -> Bit(value).repeat(it.size) } ?: Bit(null).repeat(it.size)
    }

data class Block(
    val size: Int,
    val filled: Int,
    val value: Long?,
) {
    fun emptied(): Block {
        return Block(this.size, this.size, null)
    }
}

fun List<Bit>.toBlocks(): List<Block> =
    this.fold(listOf<Block>()) { acc, bit ->
        if (acc.isEmpty()) {
            acc + Block(1, 1, bit.value)
        } else if ((acc.last().value ?: -1) != (bit.value ?: -1)) {
            acc + Block(1, 1, bit.value)
        } else {
            acc.dropLast(1) + Block(acc.last().size + 1, acc.last().filled + 1, bit.value)
        }
    }

fun Block.fitsInBlock(block: Block): Boolean = this.size <= block.size && block.isEmpty()

fun Block.isEmpty(): Boolean = this.value == null

fun Block.insertBlock(block: Block): Block =
    if (this.size >= block.size && this.isEmpty()) {
        Block(
            maxOf(this.size, block.size),
            block.filled,
            block.value,
        )
    } else {
        throw IllegalArgumentException("Block does not fit in this block")
    }

fun Block.splitEmptyBlock(): List<Block> =
    if (this.isEmpty()) {
        listOf(this)
    } else if (this.filled == this.size) {
        listOf(this)
    } else {
        listOf(Block(this.filled, this.filled, this.value), Block(this.size - this.filled, this.size - this.filled, null))
    }

fun List<Block>.mergeEmptyBlocks(): List<Block> =
    this.fold(listOf<Block>()) { acc, block ->
        if (acc.isEmpty()) {
            acc + block
        } else if (acc.last().isEmpty() && block.isEmpty()) {
            acc.dropLast(1) + Block(acc.last().size + block.size, acc.last().size + block.size, null)
        } else {
            acc + block
        }
    }

fun List<Block>.switchBlocks(
    a: Int,
    b: Int,
): List<Block> {
    return this.mapIndexed { index, block ->
        when (index) {
            a -> block.insertBlock(this[b])
            b -> this[b].emptied()
            else -> block
        }
    }.mergeEmptyBlocks().flatMap { it.splitEmptyBlock() }
}

fun Block.asStr(): String = (this.value ?: ".").toString().repeat(this.filled) + ".".repeat(this.size - this.filled)

fun List<Block>.asString(): String = this.joinToString("") { it.asStr() }

fun List<Block>.toQueue(): Queue<Block> = LinkedList(this)

fun List<Block>.findFirstEmptyBlockThatFits(
    block: Block,
    maxIndex: Int,
): Int =
    runCatching {
        this.take(maxIndex).indexOfFirst { block.fitsInBlock(it) }
    }.getOrDefault(-1)

class Day9 : Day {
    override val day: Int = 9

    fun finalState(input: String): Boolean = Regex("""\d+\.+""").matches(input)

    fun parseInputToBits(input: String): List<Bit> =
        input.foldIndexed(listOf<Bit>() to 0.toLong()) {
                index, acc, c ->
            if (index % 2 == 1) {
                acc.first + Bit(null).repeat(c.asInt()) to acc.second
            } else {
                acc.first + Bit(acc.second).repeat(c.asInt()) to acc.second + 1
            }
        }.first

    fun parseInputToBlocks(input: String): List<Block> = parseInputToBits(input).toBlocks().mergeEmptyBlocks()

    fun defragPart1(input: List<Bit>): List<Bit> =
        input.count { it.value != null }.let { totalNumbers ->
            input.filter { it.value != null }.reversed().bitsToQueue().let { lastQueue ->
                input.take(totalNumbers).mapIndexed { index, bit ->
                    if (bit.value == null) {
                        lastQueue.poll()
                    } else {
                        bit
                    }
                }
            }
        }

    fun findBlockIndex(
        block: Block,
        list: List<Block>,
    ): Int = list.indexOfFirst { it == block }

    fun defragPart2(input: List<Block>): List<Block> =
        input.filter { !it.isEmpty() }.reversed().let { queue ->
            queue.fold(input) { acc, block ->
                findBlockIndex(block, acc).let {
                        blockIndex ->
                    acc.findFirstEmptyBlockThatFits(block, blockIndex).let { emptyIndex ->
                        if (emptyIndex != -1 && (emptyIndex < blockIndex)) {
                            acc.switchBlocks(emptyIndex, blockIndex)
                        } else {
                            acc
                        }
                    }
                }
            }
        }

    override fun solvePart1(): Any {
        val degraf = defragPart1(parseInputToBits(getFileText()))
        return degraf.getResult()
    }

    override fun solvePart2(): Any {
        val degraf = defragPart2(parseInputToBlocks(getFileText()))
        return degraf.toListOfBits().getResult()
    }
}
