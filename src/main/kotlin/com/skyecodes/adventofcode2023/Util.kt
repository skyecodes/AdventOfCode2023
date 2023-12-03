package com.skyecodes.adventofcode2023

object Util {
    private fun readFile(path: String): List<String> = this::class.java.getResourceAsStream(path)!!.bufferedReader().readLines()
    private fun readFileForDay(day: String, file: String): List<String> = readFile("/$day/$file.txt")
    fun readInputFileForDay(day: String): List<String> = readFileForDay(day, "input")
    fun readExampleFileForDay(day: String): List<String> = readFileForDay(day, "example")
    fun readExample2FileForDay(day: String): List<String> = readFileForDay(day, "example2")
}