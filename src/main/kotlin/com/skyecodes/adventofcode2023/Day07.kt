package com.skyecodes.adventofcode2023

fun main() {
    Day07.test()
    Day07.run()
}

private val cards = arrayOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

private val cards2 = arrayOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

private val cardReplacements = cards2.drop(1)

private object Day07 : Day<List<Hand>, Int, List<Hand2>, Int>() {
    override val exampleResultPart1 = 6440
    override val exampleResultPart2 = 5905

    override fun parseInput1(input: List<String>): List<Hand> = input.map { line ->
        val (cardString, bid) = line.split(" ")
        Hand(cardString.map { cards.indexOf(it) }, bid.toInt())
    }

    override fun part1(input: List<Hand>): Int = calculate(input)

    override fun parseInput2(input: List<String>): List<Hand2> = input.map { line ->
        val (cardString, bid) = line.split(" ")
        Hand2(cardString, cardString.map { cards2.indexOf(it) }, bid.toInt())
    }

    override fun part2(input: List<Hand2>): Int = calculate(input)

    private fun calculate(input: List<Hand>): Int = input.sorted().mapIndexed { i, hand -> (i + 1) * hand.bid }.sum()
}

private open class Hand(val cards: List<Int>, val bid: Int) : Comparable<Hand> {
    open val type: Int by lazy { calculateType(cards) }

    override fun compareTo(other: Hand): Int {
        var c = type.compareTo(other.type)
        var i = 0
        while (c == 0) {
            c = cards[i].compareTo(other.cards[i++])
        }
        return c
    }
}

private class Hand2(val cardString: String, cards: List<Int>, bid: Int) : Hand(cards, bid) {
    override val type: Int by lazy { calculateTypeWithJokers(cardString, cards) }
}

private fun calculateType(cards: List<Int>): Int {
    val counts = cards.groupingBy { it }.eachCount()
    return when (counts.size) {
        1 -> 6
        2 -> if (counts.values.max() == 4) 5 else 4
        3 -> if (counts.values.max() == 3) 3 else 2
        4 -> 1
        else -> 0
    }
}

private fun calculateTypeWithJokers(cardString: String, cards: List<Int>): Int =
    if (!cards.contains(0)) calculateType(cards)
    else cardReplacements
        .map { cardString.replaceFirst('J', it) }
        .maxOf { newCardString -> calculateTypeWithJokers(newCardString, newCardString.map { cards2.indexOf(it) }) }
