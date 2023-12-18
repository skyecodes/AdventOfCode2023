package com.skyecodes.adventofcode2023

import kotlin.math.min

fun main() {
    Day05.test()
    Day05.run()
}

private object Day05 : Day<Almanac1, Long, Almanac2, Long>() {
    override val exampleResultPart1 = 35L
    override val exampleResultPart2 = 46L

    override fun parseInput1(input: List<String>): Almanac1 {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
        val maps = parseMaps(input)
        return Almanac1(seeds, maps)
    }

    override fun parseInput2(input: List<String>): Almanac2 {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }.chunked(2).map { CategoryRange(it[0], it[1]) }
        val maps = parseMaps(input).map { map -> map.sortedBy { it.sourceStart } }
        return Almanac2(seeds, maps)
    }

    private fun parseMaps(input: List<String>): List<AlmanacMap> = buildList {
        val iterator = input.drop(3).iterator()
        while (iterator.hasNext()) {
            add(buildList {
                while (iterator.hasNext()) {
                    val line = iterator.next()
                    if (line.isEmpty()) {
                        iterator.next() // we wanna skip text
                        break
                    }
                    val (destinationStart, sourceStart, length) = line.split(" ").map { it.toLong() }
                    add(AlmanacCategory(destinationStart, sourceStart, length))
                }
            })
        }
    }

    override fun part1(input: Almanac1): Long = input.seeds.minOf { input.maps.fold(it) { value, map -> map.convert(value) } }

    override fun part2(input: Almanac2): Long = input.convertRecursive(input.seeds, 0).minOf { it.start }
}

private typealias AlmanacMap = List<AlmanacCategory>

private data class AlmanacCategory(val destinationStart: Long, val sourceStart: Long, val length: Long) {
    val sourceEnd = sourceStart + length
}

private data class CategoryRange(val start: Long, val length: Long) {
    val end = start + length
}

private data class Almanac1(val seeds: List<Long>, val maps: List<AlmanacMap>)

private data class Almanac2(val seeds: List<CategoryRange>, val maps: List<AlmanacMap>) {
    fun convertRecursive(seeds: List<CategoryRange>, mapIndex: Int): List<CategoryRange> = if (mapIndex < maps.size) convertRecursive(seeds.flatMap { maps[mapIndex].convert2(it) }, mapIndex + 1) else seeds
}

private fun AlmanacMap.convert(source: Long): Long =
    find { source in (it.sourceStart..< it.sourceEnd) }?.let { source - it.sourceStart + it.destinationStart } ?: source

private fun AlmanacMap.convert2(source: CategoryRange): List<CategoryRange> = buildList {
    val map = this@convert2
    var x = source.start
    var i = 0
    while (x < source.end) {
        if (i == map.size) {
            add(CategoryRange(x, source.end - x))
            break
        }
        val category = map[i]
        if (x < category.sourceStart) {
            add(CategoryRange(x, category.sourceStart - x))
            x = category.sourceStart
        } else if (source.start > category.sourceEnd) {
            i++
        } else {
            val end = min(source.end, category.sourceEnd)
            add(CategoryRange(x + category.destinationStart - category.sourceStart, end - x))
            x = end
            i++
            if (i == map.size && x < source.end) {
                add(CategoryRange(x, source.end - x))
                break
            }
        }
    }
}
