package com.skyecodes.adventofcode2023

fun main() {
    val games = Util.readFileForDay("02")
    println(part1(games))
    println(part2(games))
}

private fun part1(games: List<String>): Int {
    val maximums = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )
    return games.sumOf { game ->
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

private fun part2(games: List<String>): Int {
    return games.sumOf { game ->
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