package com.skyecodes.adventofcode2023

fun main() {
    Day21.test()
    Day21.run()
}

private object Day21 : SimpleDay() {
    override val exampleResultPart1 = 42
    override val exampleResultPart2 = 0
    override fun part1(input: List<String>): Int {
        val row = input.indexOfFirst { 'S' in it }
        val col = input[row].indexOf('S')
        var positions = setOf(Pos(row, col))
        repeat(64) {
            positions = positions.flatMap { (row, col) -> buildList {
                if (row > 0 && input[row - 1][col] != '#') add(Pos(row - 1, col))
                if (row < input.size - 1 && input[row + 1][col] != '#') add(Pos(row + 1, col))
                if (col > 0 && input[row][col - 1] != '#') add(Pos(row, col - 1))
                if (col < input[0].length - 1 && input[row][col + 1] != '#') add(Pos(row, col + 1))
            } }.toSet()
        }
        return positions.size
    }

    override fun part2(input: List<String>): Int {
        return 0
    }

    private data class Pos(val row: Int, val col: Int)
}