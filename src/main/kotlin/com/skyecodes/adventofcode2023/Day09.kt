package com.skyecodes.adventofcode2023

fun main() {
    Day09.test()
    Day09.run()
}

private object Day09 : NormalDay<List<List<Int>>, Int, Int>() {
    override val exampleResultPart1 = 114
    override val exampleResultPart2 = 5
    override val hasExample2 = true

    override fun parseInput(input: List<String>): List<List<Int>> = input.map { line -> line.split(' ').map { it.toInt() } }

    override fun part1(input: List<List<Int>>): Int = extrapolateAll(input) { n, seq -> n + seq.last() }

    override fun part2(input: List<List<Int>>): Int = extrapolateAll(input) { n, seq -> seq[0] - n }

    private fun extrapolateAll(input: List<List<Int>>, op: (Int, List<Int>) -> Int) = input.sumOf { line ->
        val sequences = mutableListOf(line)
        while (sequences[0].any { it != 0 }) sequences.add(0, sequences[0].zipWithNext { a, b -> b - a })
        sequences.fold(0, op)
    }
}
