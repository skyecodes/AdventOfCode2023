package com.skyecodes.adventofcode2023

fun main() {
    Day20.test()
    Day20.run()
}

private object Day20 : NormalDay<Map<String, Module>, Int, Int>() {
    override val exampleResultPart1 = 11687500
    override val exampleResultPart2 = 0

    override fun parseInput(input: List<String>): Map<String, Module> = input.associate {  line ->
        val (name, outputsStr) = line.split(" -> ")
        val outputs = outputsStr.split(", ")
        when (name[0]) {
            '%' -> name.drop(1) to FlipFlopModule(outputs)
            '&' -> name.drop(1) to ConjunctionModule(outputs)
            else -> name to BroadcasterModule(outputs)
        }
    }.initConjunctionModuleInputs()

    /**
     * For each [ConjunctionModule] we need to initialize its state by finding all the input modules (so all modules that output to this [ConjunctionModule])
     */
    private fun Map<String, Module>.initConjunctionModuleInputs(): Map<String, Module> = also { map ->
        map.filter { it.value is ConjunctionModule }.forEach { (conName, conModule) ->
            map.filter { conName in it.value.outputs }.forEach { (inputName, _) -> (conModule as ConjunctionModule).inputStateMap[inputName] = PulseType.LOW }
        }
    }

    override fun part1(input: Map<String, Module>): Int {
        val pulsesSent = mutableMapOf(PulseType.LOW to 0, PulseType.HIGH to 0)
        val pulseQueue = ArrayDeque<Pulse>()
        for (i in 0 ..< 1000) {
            pulseQueue.add(Pulse("", "broadcaster", PulseType.LOW))
            while (pulseQueue.isNotEmpty()) {
                val pulse = pulseQueue.removeFirst()
                pulsesSent[pulse.type] = pulsesSent[pulse.type]!! + 1
                input[pulse.destination]?.process(pulse)?.let { pulseQueue += it }
            }
        }
        return pulsesSent.values.reduce { a, b -> a * b }
    }

    override fun part2(input: Map<String, Module>): Int {
        /*val pulseQueue = ArrayDeque<List<Pulse>>()
        var i = 0
        while (true) {
            i++
            pulseQueue.add(listOf(Pulse("", "broadcaster", PulseType.LOW)))
            while (pulseQueue.isNotEmpty()) {
                pulseQueue.removeLast().forEach { pulse ->
                    input[pulse.destination]?.process(pulse)?.let {
                        if (it.any { p -> p.type == PulseType.LOW && p.destination == "rx" }) return i
                        pulseQueue.addLast(it)
                    }
                }
            }
        }*/
        return 0
    }
}

sealed class Module(val outputs: List<String>) {
    fun process(pulse: Pulse): List<Pulse>? = getResult(pulse)?.let { type -> outputs.map { Pulse(pulse.destination, it, type) } }

    abstract fun getResult(pulse: Pulse): PulseType?
}

class BroadcasterModule(outputs: List<String>) : Module(outputs) {
    override fun getResult(pulse: Pulse): PulseType = pulse.type
}

class FlipFlopModule(outputs: List<String>) : Module(outputs) {
    private var currentState = PulseType.LOW

    override fun getResult(pulse: Pulse): PulseType? {
        return if (pulse.type == PulseType.LOW) {
            (if (currentState == PulseType.LOW) PulseType.HIGH else PulseType.LOW).also { currentState = it }
        }
        else null
    }
}

class ConjunctionModule(outputs: List<String>) : Module(outputs) {
    val inputStateMap = mutableMapOf<String, PulseType>()

    override fun getResult(pulse: Pulse): PulseType {
        inputStateMap[pulse.source] = pulse.type
        return if (inputStateMap.values.all { it == PulseType.HIGH }) PulseType.LOW else PulseType.HIGH
    }
}

enum class PulseType { LOW, HIGH }

class Pulse(val source: String, val destination: String, val type: PulseType)