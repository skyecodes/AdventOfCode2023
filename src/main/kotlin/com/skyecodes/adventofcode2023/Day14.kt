package com.skyecodes.adventofcode2023

fun main() {
    Day14.test()
    Day14.run()
}

object Day14 : SimpleDay() {
    override val exampleResultPart1 = 136
    override val exampleResultPart2 = 64

    override fun part1(input: List<String>) = input.flip(Direction.NORTH).getLoad()

    override fun part2(input: List<String>): Int {
        val history = mutableListOf(input)
        val repeatedIndexes = mutableListOf<Int>()
        var matrix = input
        for (i in 0 ..< 1000000000) {
            matrix = Direction.entries.fold(matrix) { m, direction -> m.flip(direction) }
            if (matrix in history) {
                val index = history.indexOf(matrix)
                if (index in repeatedIndexes) break
                repeatedIndexes += index
            }
            history += matrix
        }
        return history[((1000000000 - repeatedIndexes[0]) % repeatedIndexes.size) + repeatedIndexes[0]].getLoad()
    }

    private fun List<String>.transpose(): List<String> = this[0].indices.map { i -> this.joinToString("") { it[i].toString() } }

    private fun List<String>.flip(direction: Direction): List<String> {
        val matrix = if (direction.isVertical) transpose() else this
        val flippedMatrix = matrix.map { col -> col.split('#').joinToString("#") { part -> part.toCharArray().let { if (direction.isReversed) it.sortedDescending() else it.sorted() }.joinToString("") } }
        return if (direction.isVertical) flippedMatrix.transpose() else flippedMatrix
    }

    private fun List<String>.getLoad() = transpose().sumOf { col ->  col.reversed().mapIndexed { i, c -> if (c == 'O') i + 1 else 0 }.sum() }

    private enum class Direction(val isVertical: Boolean, val isReversed: Boolean) {
        NORTH(true, true),
        WEST(false, true),
        SOUTH(true, false),
        EAST(false, false)
    }
}
