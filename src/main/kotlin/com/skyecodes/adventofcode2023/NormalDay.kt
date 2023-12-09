package com.skyecodes.adventofcode2023

abstract class NormalDay<I, R1, R2> : Day<I, R1, I, R2>() {
    abstract fun parseInput(input: List<String>): I

    override fun parseInput1(input: List<String>): I = parseInput(input)
    override fun parseInput2(input: List<String>): I = parseInput(input)
}