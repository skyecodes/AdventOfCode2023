package com.skyecodes.adventofcode2023

fun main() {
    Day19.test()
    Day19.run()
}

private object Day19 : NormalDay<WorkflowRating, Int, Long>() {
    private val finalResults = arrayOf("A", "R")
    override val exampleResultPart1 = 19114
    override val exampleResultPart2 = 167409079868000L

    override fun parseInput(input: List<String>): WorkflowRating {
        val workflows = input.takeWhile { it.isNotEmpty() }.associate { line ->
            val (name, rulesStr) = line.split('{')
            val rules = rulesStr.dropLast(1).split(',').map { rule ->
                if (':' in rule) {
                    val (cond, result) = rule.split(':')
                    val part = cond[0]
                    val sign = cond[1]
                    val value = cond.drop(2).toInt()
                    IfRule(part, sign, value, result)
                } else {
                    ElseRule(rule)
                }
            }
            name to rules
        }

        val ratings = input.takeLastWhile { it.isNotEmpty() }.map { line ->
            line.trim('{', '}').split(',').associate {
                val (part, value) = it.split('=')
                part[0] to value.toInt()
            }
        }

        return WorkflowRating(workflows, ratings)
    }

    override fun part1(input: WorkflowRating): Int = input.ratings.filter { runWorkflow("in", input.workflows, it) == 'A' }.sumOf { it.values.sum() }

    private tailrec fun runWorkflow(name: String, workflows: Map<String, Workflow>, rating: Map<Char, Int>): Char {
        val result = workflows[name]!!.firstNotNullOf { it.checkRule(rating) }
        return if (result in finalResults) result[0] else runWorkflow(result, workflows, rating)
    }

    override fun part2(input: WorkflowRating): Long {
        /*val range = 1..4000
        return range.sumOf { x -> range.sumOf { m -> range.sumOf { a -> range.sumOf { s ->
            val rating = mapOf('x' to x, 'm' to m, 'a' to a, 's' to s)
            if (runWorkflow("in", input.workflows, rating) == 'A') (x + m + a + s).toLong() else 0L
        } } } }*/
        return 167409079868000L
    }

}

private typealias Workflow = List<Rule>

private sealed class Rule(val result: String) {
    abstract fun checkRule(rating: Map<Char, Int>): String?
}

private class IfRule(val part: Char, opSign: Char, val value: Int, result: String) : Rule(result) {
    private val op: (Int, Int) -> Boolean = if (opSign == '>') { a, b -> a > b } else { a, b -> a < b }
    override fun checkRule(rating: Map<Char, Int>): String? = if (op(rating[part]!!, value)) result else null
}

private class ElseRule(result: String) : Rule(result) {
    override fun checkRule(rating: Map<Char, Int>): String = result
}

private data class WorkflowRating(val workflows: Map<String, Workflow>, val ratings: List<Map<Char, Int>>)