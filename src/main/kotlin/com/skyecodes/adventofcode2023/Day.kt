package com.skyecodes.adventofcode2023

abstract class Day<R> {
    private val day: String get() = this.javaClass.simpleName
    abstract val exampleResultPart1: R
    abstract val exampleResultPart2: R
    open val hasExample2 = false

    abstract fun part1(input: List<String>): R
    abstract fun part2(input: List<String>): R

    open fun test() {
        var exampleInput = Util.readExampleFileForDay(day)
        assert(part1(exampleInput) == exampleResultPart1)
        if (hasExample2) exampleInput = Util.readExample2FileForDay(day)
        assert(part2(exampleInput) == exampleResultPart2)
    }

    open fun run() {
        val input = Util.readInputFileForDay(day)
        println("Part 1: " + part1(input))
        println("Part 2: " + part2(input))
    }
}