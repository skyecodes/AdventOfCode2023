package com.skyecodes.adventofcode2023

fun main() {
    Day08.test()
    Day08.run()
}

object Day08 : Day<Instructions, Int, Instructions, Long>() {
    override val exampleResultPart1 = 6
    override val exampleResultPart2 = 6L
    override val hasExample2 = true

    override fun parseInput1(input: List<String>): Instructions = parseInput(input)

    override fun part1(input: Instructions): Int {
        var count = 0
        var directionIndex = 0
        var currentNode = "AAA"
        while (currentNode != "ZZZ") {
            currentNode = input.nodes[currentNode]!!.getNodeForDirection(input.directions[directionIndex])
            count++
            if (++directionIndex >= input.directions.length) directionIndex = 0
        }
        return count
    }

    private fun Pair<String, String>.getNodeForDirection(direction: Char): String = if (direction == 'L') first else second

    override fun parseInput2(input: List<String>): Instructions = parseInput(input)

    override fun part2(input: Instructions): Long {
        var count = 0
        var directionIndex = 0
        var currentNodes = input.nodes.keys.filter { it.endsWith('A') }
        val counts = buildList {
            while (currentNodes.isNotEmpty()) {
                count++
                currentNodes = currentNodes.map { input.nodes[it]!!.getNodeForDirection(input.directions[directionIndex]) }
                val (completedNodes, runningNodes) = currentNodes.partition { it.endsWith('Z') }
                if (completedNodes.isNotEmpty()) {
                    add(count)
                    currentNodes = runningNodes
                }
                if (++directionIndex >= input.directions.length) directionIndex = 0
            }
        }

        var result = counts[0].toLong()
        for (i in 1..< counts.size) {
            result = findLCM(result, counts[i].toLong())
        }
        return result
    }

    private fun parseInput(input: List<String>): Instructions {
        val directions = input[0]
        val nodes = input.drop(2).associate {
            val (node, leftright) = it.split(" = ")
            val (left, right) = leftright.split(", ")
            node to (left.drop(1) to right.dropLast(1))
        }
        return Instructions(directions, nodes)
    }

    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }
}

data class Instructions(val directions: String, val nodes: Map<String, Pair<String, String>>)