/*
 * --- Day 10: Elves Look, Elves Say ---
 *
 * Today, the Elves are playing a game called look-and-say.  They take turns making sequences by reading aloud the
 * previous sequence and using that reading as the next sequence.  For example, 211 is read as "one two, two ones",
 * which becomes 1221 (1 2, 2 1s).
 *
 * Look-and-say sequences are generated iteratively, using the previous value as input for the next step.  For each
 * step, take the previous value, and replace each run of digits (like 111) with the number of digits (3) followed by
 * the digit itself (1).
 *
 * For example:
 *
 *   - 1 becomes 11 (1 copy of digit 1).
 *   - 11 becomes 21 (2 copies of digit 1).
 *   - 21 becomes 1211 (one 2 followed by one 1).
 *   - 1211 becomes 111221 (one 1, one 2, and two 1s).
 *   - 111221 becomes 312211 (three 1s, two 2s, and one 1).
 *
 * Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?
 *
 * --- Part Two ---
 *
 * Neat, right?  You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's Game of
 * Life fame).
 *
 * Now, starting again with the digits in your puzzle input, apply this process 50 times.  What is the length of the new
 * result?
 */
package io.ysakhno.adventofcode2015.day10

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun playLookAndSay(str: String): String {
    var digit = str[0]
    var count = 1
    val result = StringBuilder()

    for (i in 1..<str.length) {
        val ch = str[i]
        if (ch != digit) {
            result.append(count).append(digit)
            digit = ch
            count = 1
        } else ++count
    }

    return result.append(count).append(digit).toString()
}

private fun solve(input: String, numIterations: Int) =
    (1..numIterations).fold(input) { acc, _ -> playLookAndSay(acc) }.length

private fun part1(input: String) = solve(input, 40)

private fun part2(input: String) = solve(input, 50)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest().first()
    assertEquals(182376, part1(testInput), "Part one (sample input)")
    assertEquals(2584304, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read().first()
    part1(input).println()
    part2(input).println()
}
