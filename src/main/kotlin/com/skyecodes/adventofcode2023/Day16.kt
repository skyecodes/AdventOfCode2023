package com.skyecodes.adventofcode2023

fun main() {
    Day16.test()
    Day16.run()
}

private object Day16 : NormalDay<TileMap2, Int, Int>() {
    override val exampleResultPart1 = 46
    override val exampleResultPart2 = 51

    override fun parseInput(input: List<String>): TileMap2 = input.map { line -> line.map { when(it) {
        '/' -> Mirror.UPWARD
        '\\' -> Mirror.DOWNWARD
        '-' -> Axis.HORIZONTAL
        '|' -> Axis.VERTICAL
        else -> EmptySpace
    } } }

    override fun part1(input: TileMap2): Int = input.energize(Beam(Pos(0, -1), Direction.RIGHT))

    override fun part2(input: TileMap2): Int {
        val beams = buildList {
            for (row in input.indices) {
                add(Beam(Pos(row, -1), Direction.RIGHT))
                add(Beam(Pos(row, input[row].size), Direction.LEFT))
            }
            for (col in input[0].indices) {
                add(Beam(Pos(-1, col), Direction.BOTTOM))
                add(Beam(Pos(input.size, col), Direction.TOP))
            }
        }
        return beams.maxOf { input.energize(it) }
    }

    private fun TileMap2.energize(initialBeam: Beam): Int {
        var beams = listOf(initialBeam)
        val beamHistory = mutableSetOf<Beam>()
        val posHistory = mutableSetOf<Pos>()
        while (beams.isNotEmpty()) {
            beams = beams.flatMap { beam -> when (val tile = getTile(beam.nextPos)) {
                EmptySpace -> listOf(Beam(beam.nextPos, beam.direction))
                is Mirror -> listOf(Beam(beam.nextPos, tile.reflectionMap[beam.direction]!!))
                is Axis -> tile.splitBeam(beam.direction).map { Beam(beam.nextPos, it) }
                else -> emptyList()
            } }.filter { it !in beamHistory }.onEach { beamHistory += it; posHistory += it.pos }
        }
        return posHistory.size
    }

    private fun TileMap2.getTile(pos: Pos) = getOrNull(pos.row)?.getOrNull(pos.col)

    private data class Pos(val row: Int, val col: Int) {
        operator fun plus(pos: Pos) = Pos(row + pos.row, col + pos.col)
    }

    private enum class Direction(val offset: Pos, val axis: Axis) {
        TOP(Pos(-1, 0), Axis.VERTICAL),
        RIGHT(Pos(0, 1), Axis.HORIZONTAL),
        BOTTOM(Pos(1, 0), Axis.VERTICAL),
        LEFT(Pos(0, -1), Axis.HORIZONTAL)
    }

    private data class Beam(val pos: Pos, val direction: Direction) {
        val nextPos = pos + direction.offset
    }

    private data object EmptySpace : Tile2

    private enum class Mirror(val reflectionMap: Map<Direction, Direction>) : Tile2 {
        UPWARD(mapOf(
            Direction.TOP to Direction.RIGHT,
            Direction.RIGHT to Direction.TOP,
            Direction.BOTTOM to Direction.LEFT,
            Direction.LEFT to Direction.BOTTOM)
        ),
        DOWNWARD(mapOf(
            Direction.TOP to Direction.LEFT,
            Direction.LEFT to Direction.TOP,
            Direction.BOTTOM to Direction.RIGHT,
            Direction.RIGHT to Direction.BOTTOM)
        );
    }

    private enum class Axis : Tile2 {
        HORIZONTAL,
        VERTICAL;

        fun splitBeam(direction: Direction) = if (direction.axis == this) listOf(direction) else Direction.entries.filter { it.axis == this }
    }
}

private typealias TileMap2 = List<List<Tile2>>

private sealed interface Tile2