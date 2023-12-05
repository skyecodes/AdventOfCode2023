package com.skyecodes.adventofcode2023

fun main() {
    Day04.test()
    Day04.run()
}

object Day04 : SimpleDay() {
    override val exampleResultPart1 = 13
    override val exampleResultPart2 = 30

    override fun part1(input: List<String>) = input.sumOf { card -> card.countWinningNumbers().let { if (it > 0) 1 shl (it - 1) else 0 } }

    override fun part2(input: List<String>): Int {
        val cardCounts = input.map { 1 }.toTypedArray()
        input.forEachIndexed { index, card ->
            val count = card.countWinningNumbers()
            for (j in index.rangeUntil(index + count)) {
                cardCounts[j + 1] += cardCounts[index]
            }
        }
        return cardCounts.sum()
    }

    private fun String.countWinningNumbers(): Int {
        val (winning, numbers) = this.split(": ")[1].split(" | ").map { part -> part.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
        return numbers.count { it in winning }
    }
}