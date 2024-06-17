/*
 * <PUZZLE-DESCRIPTION-HERE>
 */
package io.ysakhno.adventofcode2015.dayXX

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun part1(input: List<String>): Int {
    return input.size
}

private fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(0, part1(testInput), "Part one (sample input)")
    assertEquals(0, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
