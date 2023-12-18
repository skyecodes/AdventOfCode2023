package com.skyecodes.adventofcode2023

fun main() {
    Day01.test()
    Day01.run()
}

private object Day01 : SimpleDay() {
    override val exampleResultPart1 = 142
    override val exampleResultPart2 = 281
    override val hasExample2 = true
    override fun part1(input: List<String>): Int = input.sumOf { line ->
        val first = line.toCharArray().first { it.isDigit() }.digitToInt()
        val last = line.reversed().toCharArray().first { it.isDigit() }.digitToInt()
        first * 10 + last
    }

    override fun part2(input: List<String>): Int {
        val map = mapOf(
            "one" to 1,
            "1" to 1,
            "two" to 2,
            "2" to 2,
            "three" to 3,
            "3" to 3,
            "four" to 4,
            "4" to 4,
            "five" to 5,
            "5" to 5,
            "six" to 6,
            "6" to 6,
            "seven" to 7,
            "7" to 7,
            "eight" to 8,
            "8" to 8,
            "nine" to 9,
            "9" to 9
        )

        return input.sumOf { line ->
            val mapFirst = map.entries.map { (key, value) -> line.indexOf(key) to value }.filter { it.first >= 0 }
            val mapLast = map.entries.map { (key, value) -> line.lastIndexOf(key) to value }.filter { it.first >= 0 }
            val first = mapFirst.minBy { it.first }.second
            val last = mapLast.maxBy { it.first }.second
            first * 10 + last
        }
    }
}