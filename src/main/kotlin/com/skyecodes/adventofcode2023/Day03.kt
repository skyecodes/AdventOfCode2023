package com.skyecodes.adventofcode2023

import kotlin.math.max
import kotlin.math.min

fun main() {
    Day03.test()
    Day03.run()
}

private object Day03 : SimpleDay() {
    override val exampleResultPart1 = 4361
    override val exampleResultPart2 = 467835

    override fun part1(input: List<String>): Int {
        var result = 0
        input.forEachIndexed { y, line ->
            var x = 0
            while (x < line.length) {
                // try to find a number
                var number = ""
                val numberStartX = x
                while (x < line.length && line[x].isDigit()) {
                    number += line[x]
                    x++
                }
                if (number.isNotEmpty()) {
                    // number found, now we find all the surrounding characters
                    val intNumber = number.toInt()
                    var surroundingCharacters = ""
                    val lineStartX = max(0, numberStartX - 1)
                    val lineEndX = min(x, line.length - 1)
                    if (y != 0) {
                        // not on the first line, so we can check the line above                    //......
                        surroundingCharacters += input[y - 1].substring(lineStartX, lineEndX + 1)   // 123
                    }
                    if (y < line.length - 1) {
                        // not on the last line, so we can check the line below                     // 123
                        surroundingCharacters += input[y + 1].substring(lineStartX, lineEndX + 1)   //......
                    }
                    if (numberStartX > 0) {
                        // not the first character on the line, so we can check the character on the left
                        surroundingCharacters += line[numberStartX - 1]                             //.123
                    }
                    if (x < line.length) {
                        // not the last character on the line, so we can check the character on the right
                        surroundingCharacters += line[x]                                            // 123.
                    }
                    if (surroundingCharacters.any { !it.isDigit() && it != '.' }) {
                        // one of the surroundings characters is neither a number or a period => it is a symbol
                        result += intNumber
                    }
                } else {
                    // no number found, we check the next character
                    x++
                }
            }
        }
        return result
    }

    override fun part2(input: List<String>): Int {
        var result = 0
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                // try to find a * character
                if (char == '*') {
                    // find numbers around this character
                    val numbers = mutableListOf<Int>()
                    val lineStartX = max(0, x - 1)
                    val lineEndX = min(x + 1, line.length - 1)
                    if (y != 0) {
                        // not on the first line, so we can check the line above
                        numbers += checkLine(input[y - 1], lineStartX, lineEndX)
                    }
                    if (y < line.length - 1) {
                        // not on the last line, so we can check the line below
                        numbers += checkLine(input[y + 1], lineStartX, lineEndX)
                    }
                    if (x > 0) {
                        // not the first character on the line, so we can check the characters on the left
                        numbers += checkLine(input[y], x - 1, x - 1)
                    }
                    if (x < line.length) {
                        // not the last character on the line, so we can check the characters on the right
                        numbers += checkLine(input[y], x + 1, x + 1)
                    }
                    if (numbers.size == 2) {
                        result += numbers[0] * numbers[1]
                    }
                }
            }
        }
        return result
    }

    /**
     * Checks if the [line] contains numbers between indexes [scanStartX] and [scanEndX].
     * @return a list of numbers
     */
    private fun checkLine(line: String, scanStartX: Int, scanEndX: Int): List<Int> {
        val numbers = mutableListOf<Int>()
        var scanX = scanStartX
        while (scanX <= scanEndX) {
            var numberX = scanX
            var number = ""
            // check if a number starts and keeps going to the right
            while (numberX < line.length && line[numberX].isDigit()) {
                number += line[numberX]
                numberX++
            }
            if (number.isNotEmpty()) {
                val overflow = numberX - scanX
                numberX = scanX - 1
                // now, check if this number keeps going to the left
                while (numberX >= 0 && line[numberX].isDigit()) {
                    number = line[numberX] + number
                    numberX--
                }
                numbers += number.toInt()
                scanX += overflow
            } else {
                scanX++
            }
        }
        return numbers
    }
}
