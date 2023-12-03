package com.skyecodes.adventofcode2023

fun main() {
    Day02.test()
    Day02.run()
}

object Day02 : Day<Int>() {
    override val exampleResultPart1 = 8
    override val exampleResultPart2 = 2286

    override fun part1(input: List<String>): Int {
        val maximums = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        return input.sumOf { game ->
            val (gameTitle, gameSets) = game.split(": ")
            val gameNumber = gameTitle.split(" ")[1].toInt()
            if (gameSets.split("; ")
                    .flatMap { set -> set.split(", ") }
                    .all { cube ->
                        val (amount, color) = cube.split(" ")
                        amount.toInt() <= maximums[color]!!
                    }) gameNumber else 0
        }
    }

    override fun part2(input: List<String>): Int {
        return input.sumOf { game ->
            val gameSets = game.split(": ")[1]
            val minimums = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )
            gameSets.split("; ")
                .flatMap { set -> set.split(", ") }
                .forEach { cube ->
                    val (amount, color) = cube.split(" ")
                    val intAmount = amount.toInt()
                    if (intAmount > minimums[color]!!) {
                        minimums[color] = intAmount
                    }
                }
            minimums.values.reduce(Int::times)
        }
    }
}
