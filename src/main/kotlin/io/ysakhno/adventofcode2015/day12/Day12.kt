/*
 * --- Day 12: JSAbacusFramework.io ---
 *
 * Santa's Accounting-Elves need help balancing the books after a recent order.  Unfortunately, their accounting
 * software uses a peculiar storage format.  That's where you come in.
 *
 * They have a JSON document, which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers,
 * and strings.  Your first job is to simply find all of the numbers throughout the document and add them together.
 *
 * For example:
 *
 *   - [1,2,3] and {"a":2,"b":4} both have a sum of 6.
 *   - [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
 *   - {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
 *   - [] and {} both have a sum of 0.
 *
 * You will not encounter any strings containing numbers.
 *
 * What is the sum of all numbers in the document?
 *
 * --- Part Two ---
 *
 * Uh oh - the Accounting-Elves have realized that they double-counted everything red.
 *
 * Ignore any object (and all of its children) which has any property with the value "red".  Do this only for objects
 * ({...}), not arrays ([...]).
 *
 *   - [1,2,3] still has a sum of 6.
 *   - [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
 *   - {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
 *   - [1,"red",5] has a sum of 6, because "red" in an array has no effect.
 */
package io.ysakhno.adventofcode2015.day12

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val NUMBERS_REGEX = "(?<![-0-9])(?:0|-?[1-9][0-9]*)(?!=[0-9])".toRegex()

private fun part1(input: List<String>) =
    input.flatMap(NUMBERS_REGEX::findAll).map(MatchResult::value).sumOf(String::toInt)

private val OBJECT_REGEX = "\\{[^{}]*}".toRegex()

private val RED_PROPERTY_REGEX = "\"\\s*:\\s*\"red\"".toRegex()

private fun String.getInnermostObject() = OBJECT_REGEX.find(this)?.let { it.value to it.range }

private fun String.computeSum() = if (!contains(RED_PROPERTY_REGEX)) part1(listOf(this)) else 0

private fun String.computeIteratively(): Int {
    var text = this
    var inner = text.getInnermostObject()

    while (inner != null) {
        text =
            text.substring(0, inner.second.first) + inner.first.computeSum() + text.substring(inner.second.last + 1)
        inner = text.getInnermostObject()
    }

    return text.computeSum()
}

private fun part2(input: List<String>) = input.sumOf(String::computeIteratively)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(6 + 6 + 6 + 3 + 6 + 3 + 15 + 12 - 10, part1(testInput), "Part one (sample input)")
    assertEquals(6 + 6 + 4 + 3 + 6 + 3 + 12 - 10, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
