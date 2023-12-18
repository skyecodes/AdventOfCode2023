package com.skyecodes.adventofcode2023

import kotlin.math.abs

fun main() {
    Day11.test()
    Day11.run()
}

private object Day11 : NormalDay<List<String>, Long, Long>() {
    override val exampleResultPart1 = 374L
    override val exampleResultPart2 = 82000210L

    override fun parseInput(input: List<String>): List<String> = input

    override fun part1(input: List<String>): Long = part(input, 1)

    override fun part2(input: List<String>): Long = part(input, 999999)

    private fun part(input: List<String>, multiplier: Long): Long {
        val columnsToAdd = input[0].indices.filter { index -> input.map { it[index] }.all { it == '.' } }
        val rowsToAdd = input.mapIndexed { index, line -> index to line }.filter { (_, line) -> line.all { it == '.' } }.map { it.first }
        val galaxies = buildList { input.forEachIndexed { y, line -> line.forEachIndexed { x, c -> if (c == '#') add(Pos(x + multiplier * (columnsToAdd.count { it in 0 ..< x }), y + multiplier * (rowsToAdd.count { it in 0 ..< y }))) } } }
        val pairs = buildList { galaxies.forEachIndexed { i, a -> galaxies.forEachIndexed { j, b -> if (i < j) add(a to b) } } }
        return pairs.sumOf { (a, b) -> abs(a.x - b.x) + abs(a.y - b.y) }
    }

    private data class Pos(val x: Long, val y: Long)
}