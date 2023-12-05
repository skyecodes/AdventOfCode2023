package com.skyecodes.adventofcode2023

abstract class Day<I1, I2, R1, R2> {
    private val day: String get() = this.javaClass.simpleName
    abstract val exampleResultPart1: R1
    abstract val exampleResultPart2: R2
    open val hasExample2 = false

    abstract fun part1(input: I1): R1
    abstract fun part2(input: I2): R2

    abstract fun parseInput1(input: List<String>): I1
    abstract fun parseInput2(input: List<String>): I2

    open fun test() {
        val data = Util.readExampleFileForDay(day)
        var data2 = data
        if (hasExample2) data2 = Util.readExample2FileForDay(day)
        check(part1(parseInput1(data)) == exampleResultPart1)
        check(part2(parseInput2(data2)) == exampleResultPart2)
    }

    open fun run() {
        val data = Util.readInputFileForDay(day)
        println("Part 1: " + part1(parseInput1(data)))
        println("Part 2: " + part2(parseInput2(data)))
    }
}