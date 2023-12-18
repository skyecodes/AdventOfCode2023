package com.skyecodes.adventofcode2023

fun main() {
    Day15.test()
    Day15.run()
}

private object Day15 : SimpleDay() {
    override val exampleResultPart1 = 1320
    override val exampleResultPart2 = 145

    override fun part1(input: List<String>) = input[0].split(',').sumOf { part -> part.hash() }

    override fun part2(input: List<String>): Int {
        val hashmap = mutableMapOf<String, Int>()
        val boxes = mutableMapOf<Int, MutableList<Lens>>()
        input[0].split(',').forEach { part ->
            if ('-' in part) {
                val label = part.dropLast(1)
                val hash = hashmap.getOrPut(label) { label.hash() }
                boxes[hash]?.removeAll { it.label == label }
            } else {
                val (label, focalLength) = part.split('=')
                val hash = hashmap.getOrPut(label) { label.hash() }
                val box = boxes.getOrPut(hash) { mutableListOf() }
                val slot = box.indexOfFirst { it.label == label }
                val lens = Lens(label, focalLength.toInt())
                if (slot == -1) box += lens else box[slot] = lens
            }
        }
        return boxes.entries.sumOf { (boxNumber, lenses) -> lenses.mapIndexed { slot, lens -> (1 + boxNumber) * (1 + slot) * lens.focalLength }.sum() }
    }

    private fun String.hash(): Int = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    private data class Lens(val label: String, val focalLength: Int)
}