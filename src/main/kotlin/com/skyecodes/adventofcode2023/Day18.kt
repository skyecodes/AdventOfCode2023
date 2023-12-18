package com.skyecodes.adventofcode2023

fun main() {
    Day18.test()
    Day18.run()
}

private object Day18 : Day<List<DigPlan>, Long, List<DigPlan>, Long>() {
    override val exampleResultPart1 = 62L
    override val exampleResultPart2 = 952408144115L

    override fun parseInput1(input: List<String>): List<DigPlan> = input.map { line ->
        val (dir, len) = line.split(" ")
        DigPlan(when (dir) {
            "U" -> DigDirection.UP
            "R" -> DigDirection.RIGHT
            "D" -> DigDirection.DOWN
            else -> DigDirection.LEFT
        }, len.toInt())
    }

    override fun part1(input: List<DigPlan>): Long = part(input)

    @OptIn(ExperimentalStdlibApi::class)
    override fun parseInput2(input: List<String>): List<DigPlan> = input.map { line ->
        val (len, dir) = line.split(" ")[2].substring(2, 8).chunked(5)
        DigPlan(when (dir) {
            "0" -> DigDirection.RIGHT
            "1" -> DigDirection.DOWN
            "2" -> DigDirection.LEFT
            else -> DigDirection.UP
        }, len.hexToInt())
    }

    override fun part2(input: List<DigPlan>): Long = part(input)

    private fun part(input: List<DigPlan>): Long {
        val positions = buildList {
            add(DigPos(0, 0))
            input.forEach { add(it.direction.move(last(), it.length)) }
        }
        return (positions.zipWithNext().fold(0L) { acc, (a, b) -> acc + a.shoelace(b) } + input.sumOf { it.length }) / 2 + 1
    }
}

private enum class DigDirection(val move: DigPos.(Int) -> DigPos) {
    UP({ DigPos(x, y - it) }),
    RIGHT({ DigPos(x + it, y) }),
    DOWN({ DigPos(x, y + it) }),
    LEFT({ DigPos(x - it, y) })
}

private data class DigPlan(val direction: DigDirection, val length: Int)

private data class DigPos(val x: Long, val y: Long) {
    fun shoelace(pos: DigPos): Long = x * pos.y - y * pos.x
}

