package com.skyecodes.adventofcode2023

fun main() {
    Day10.test()
    Day10.run()
}

object Day10 : NormalDay<TileMap, Int, Int>() {
    override val exampleResultPart1 = 8
    override val exampleResultPart2 = 4
    override val hasExample2 = true

    override fun part1(input: TileMap): Int = loop(input).size / 2

    override fun part2(input: TileMap): Int {
        val loopPos = loop(input)
        var isInsideLoop = false
        var area = 0
        input.forEachIndexed { y, line -> line.forEachIndexed { x, tile ->
            val pos = Pos(x, y)
            if (pos in loopPos) {
                val pipe: Pipe? = if (tile is Pipe) tile else if (tile is Start) getStartPipe(loopPos) else null
                if (pipe?.directions?.contains(Direction.SOUTH) == true) isInsideLoop = !isInsideLoop
            } else if (isInsideLoop) area++
        } }
        return area
    }

    override fun parseInput(input: List<String>): List<List<Tile>> = input.map { line -> line.map { when(it) {
        '|' -> Pipe.VERTICAL
        '-' -> Pipe.HORIZONTAL
        'L' -> Pipe.NE
        'J' -> Pipe.NW
        '7' -> Pipe.SW
        'F' -> Pipe.SE
        'S' -> Start
        else -> Ground
    } } }

    private fun getStartPipe(loopPos: List<Pos>): Pipe {
        val startPos = loopPos[0]
        val directions = listOf(loopPos[1] - startPos, loopPos.last() - startPos)
        return Pipe.entries.first { pipe -> pipe.directions.all { it.delta in directions } }
    }

    private fun loop(tileMap: TileMap): List<Pos> {
        var currentPos = tileMap.findStart()
        var direction = Direction.entries.first { direction ->
            val nextPos = currentPos + direction.delta
            val nextTile = tileMap.getTile(nextPos)
            if (nextTile is Pipe) direction.opposite in nextTile.directions else false
        }
        var currentTile: Tile

        val loopPos = mutableListOf<Pos>()
        do {
            loopPos.add(currentPos)
            currentPos += direction.delta
            currentTile = tileMap.getTile(currentPos)!!
            if (currentTile is Pipe) direction = currentTile.directions.first { it != direction.opposite }
        } while (currentTile != Start)

        return loopPos
    }

    private fun TileMap.findStart(): Pos {
        val y = indexOfFirst { it.contains(Start) }
        val x = this[y].indexOf(Start)
        return Pos(x, y)
    }

    private fun TileMap.getTile(pos: Pos) = getOrNull(pos.y)?.getOrNull(pos.x)
}

typealias TileMap = List<List<Tile>>

sealed interface Tile

enum class Pipe(val directions: List<Direction>) : Tile {
    VERTICAL(listOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL(listOf(Direction.WEST, Direction.EAST)),
    NE(listOf(Direction.NORTH, Direction.EAST)),
    NW(listOf(Direction.NORTH, Direction.WEST)),
    SW(listOf(Direction.SOUTH, Direction.WEST)),
    SE(listOf(Direction.SOUTH, Direction.EAST)),
}

data object Ground : Tile

data object Start : Tile

data class Pos(val x: Int, val y: Int) {
    operator fun plus(pos: Pos): Pos = Pos(x + pos.x, y + pos.y)
    operator fun minus(pos: Pos): Pos = Pos(x - pos.x, y - pos.y)
}

enum class Direction(val delta: Pos, opposite: () -> Direction) {
    NORTH(Pos(0, -1), { SOUTH }),
    EAST(Pos(1, 0), { WEST }),
    SOUTH(Pos(0, 1), { NORTH }),
    WEST(Pos(-1, 0), { EAST });

    val opposite by lazy(opposite)
}
