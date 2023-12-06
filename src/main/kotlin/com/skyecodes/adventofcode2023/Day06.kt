package com.skyecodes.adventofcode2023

fun main() {
    Day06.test()
    Day06.run()
}

object Day06 : Day<List<Race1>, Int, Race2, Int>() {
    override val exampleResultPart1 = 288
    override val exampleResultPart2 = 71503

    override fun parseInput1(input: List<String>): List<Race1> {
        val (times, distances) = input.map { line -> parseLine(line).map { it.toInt() } }
        return times.zip(distances).map { (time, distance) -> Race1(time, distance) }
    }

    override fun part1(input: List<Race1>): Int = input.map { race -> (0..race.time).count { i -> i * (race.time - i) > race.distance } }.reduce(Int::times)

    override fun parseInput2(input: List<String>): Race2 = input.map { line -> parseLine(line).reduce { acc, s -> acc + s }.toLong() }.let { (time, distance) -> Race2(time, distance) }

    override fun part2(input: Race2): Int = (0..input.time).count { i -> i * (input.time - i) > input.distance }

    private fun parseLine(line: String) = line.split(":")[1].split(" ").filter { it.isNotEmpty() }
}

data class Race1(val time: Int, val distance: Int)
data class Race2(val time: Long, val distance: Long)