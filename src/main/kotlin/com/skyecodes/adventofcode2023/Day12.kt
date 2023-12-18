package com.skyecodes.adventofcode2023

fun main() {
    Day12.test()
    Day12.run()
}

private object Day12 : Day<List<Record>, Long, List<Record>, Long>() {
    override val exampleResultPart1 = 21L
    override val exampleResultPart2 = 525152L
    private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    override fun parseInput1(input: List<String>): List<Record> = input.map { line ->
        val (condition, groups) = line.split(' ')
        Record(condition, groups.split(',').map { it.toInt() })
    }

    //override fun part1(input: List<Record>): Long = input.sumOf { record -> compute(record.pattern, record.groups) }

    override fun part1(input: List<Record>): Long = input.sumOf { record ->
        val unknown = record.pattern.count { it == '?' }
        val damaged = record.pattern.count { it == '#' }
        val unknownDamaged = record.groups.sum() - damaged
        val unknownOperational = unknown - unknownDamaged
        countPossibilities(record.pattern, record.groups, unknownDamaged, unknownOperational)
    }

    private fun countPossibilities(pattern: String, groups: List<Int>, unknownDamaged: Int, unknownOperational: Int): Long {
        var value = 0L
        if (unknownDamaged == 0 && unknownOperational == 0) {
            if (pattern.split('.').filter { it.isNotEmpty() }.mapIndexed { index, s -> index to s.length }.all { (index, len) -> len == groups[index] }) value++
        } else {
            if (unknownDamaged > 0) value += countPossibilities(pattern.replaceFirst('?', '#'), groups, unknownDamaged - 1, unknownOperational)
            if (unknownOperational > 0) value += countPossibilities(pattern.replaceFirst('?', '.'), groups, unknownDamaged, unknownOperational - 1)
        }
        return value
    }

    override fun parseInput2(input: List<String>): List<Record> = input.map { line ->
        val (condition, groups) = line.split(' ')
        Record((0..<5).joinToString("?") { condition }, (0..<5).flatMap { groups.split(',').map { it.toInt() } })
    }

    override fun part2(input: List<Record>): Long = input.sumOf { record -> compute(record.pattern, record.groups) }

    private fun compute(pattern: String, groups: List<Int>): Long = cache.getOrPut(pattern to groups) { process(pattern, groups) }

    private fun process(pattern: String, groups: List<Int>): Long = when (pattern.firstOrNull()) {
        '.' -> compute(pattern.substring(1), groups)
        '?' -> compute('.' + pattern.substring(1), groups) + compute('#' + pattern.substring(1), groups)
        '#' -> {
            if (groups.isEmpty()) 0
            else {
                val groupSize = groups[0]
                val remainingGroups = groups.drop(1)
                val potentialGroupSize = pattern.takeWhile { it == '#' || it == '?' }.length

                if (potentialGroupSize < groupSize) 0
                else if (pattern.length == groupSize) compute("", remainingGroups)
                else if (pattern[groupSize] == '#') 0
                else compute(pattern.substring(groupSize + 1), remainingGroups)
            }
        }
        else -> if (groups.isEmpty()) 1 else 0
    }
}

private data class Record(val pattern: String, val groups: List<Int>)
