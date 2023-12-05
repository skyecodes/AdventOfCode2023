package com.skyecodes.adventofcode2023

abstract class SimpleDay : Day<List<String>, List<String>, Int, Int>() {
    override fun parseInput1(input: List<String>) = input
    override fun parseInput2(input: List<String>) = input
}