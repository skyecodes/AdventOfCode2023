package com.skyecodes.adventofcode2023

import java.nio.file.Files

object Util {
    fun readFile(path: String): List<String> = this::class.java.getResourceAsStream(path)!!.bufferedReader().readLines()
    fun readFileForDay(day: String): List<String> = readFile("/Day$day/input.txt")
}