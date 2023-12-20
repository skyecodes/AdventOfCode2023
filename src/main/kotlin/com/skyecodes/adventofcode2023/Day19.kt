package com.skyecodes.adventofcode2023

import kotlin.math.max
import kotlin.math.min

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

    private tailrec fun runWorkflow(name: String, workflows: Map<String, Workflow>, rating: Rating): Char {
        val result = workflows[name]!!.firstNotNullOf { it.checkRule(rating) }
        return if (result in finalResults) result[0] else runWorkflow(result, workflows, rating)
    }

    override fun part2(input: WorkflowRating): Long = runWorkflowRange("in", input.workflows, mapOf('x' to (1 to 4000), 'm' to (1 to 4000), 'a' to (1 to 4000), 's' to (1 to 4000)))

    private fun runWorkflowRange(name: String, workflows: Map<String, Workflow>, rating: RangeRating, result: Long = 0L): Long {
        var res = result
        val workflow = workflows[name]!!
        var curRating = rating
        for (it in workflow) {
            val (acceptedRating, rejectedRating) = it.splitRange(curRating)
            if (it.result == "A") {
                res += acceptedRating.values.fold(1L) { acc, intRange -> acc * (intRange.second - intRange.first + 1) }
            } else if (it.result != "R") {
                res += runWorkflowRange(it.result, workflows, acceptedRating, result)
            }
            curRating = rejectedRating
        }
        return res
    }
}

private typealias Workflow = List<Rule>

private sealed class Rule(val result: String) {
    abstract fun checkRule(rating: Rating): String?
    abstract fun splitRange(rating: RangeRating): Pair<RangeRating, RangeRating>
}

private class IfRule(val part: Char, val opSign: Char, val value: Int, result: String) : Rule(result) {
    private val op: (Int, Int) -> Boolean = if (opSign == '>') { a, b -> a > b } else { a, b -> a < b }

    override fun checkRule(rating: Rating): String? = if (op(rating[part]!!, value)) result else null

    override fun splitRange(rating: RangeRating): Pair<RangeRating, RangeRating> {
        val ratingToSplit = rating[part]!!
        val rangeAccepted: Pair<Int, Int>
        val rangeRejected: Pair<Int, Int>
        if (opSign == '>') {
            rangeAccepted = max(ratingToSplit.first, value) + 1 to ratingToSplit.second
            rangeRejected = ratingToSplit.first to min(ratingToSplit.second, value)
        } else {
            rangeAccepted = ratingToSplit.first to min(ratingToSplit.second, value) - 1
            rangeRejected = min(ratingToSplit.second, value) to ratingToSplit.second
        }
        val ratingAccepted = mutableMapOf<Char, Pair<Int, Int>>()
        val ratingRejected = mutableMapOf<Char, Pair<Int, Int>>()
        rating.forEach { (k, v) ->
            if (k == part) {
                ratingAccepted[k] = rangeAccepted
                ratingRejected[k] = rangeRejected
            } else {
                ratingAccepted[k] = v
                ratingRejected[k] = v
            }
        }
        return ratingAccepted to ratingRejected
    }
}

private class ElseRule(result: String) : Rule(result) {
    override fun checkRule(rating: Rating): String = result

    override fun splitRange(rating: RangeRating): Pair<RangeRating, RangeRating> = rating to emptyMap()
}

private data class WorkflowRating(val workflows: Map<String, Workflow>, val ratings: List<Rating>)

private typealias Rating = Map<Char, Int>

private typealias RangeRating = Map<Char, Pair<Int, Int>>