/*
 * --- Day 5: Doesn't He Have Intern-Elves For This? ---
 *
 * Santa needs help figuring out which strings in his text file are naughty or nice.
 *
 * A nice string is one with all of the following properties:
 *
 *   - It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
 *   - It contains at least one letter that appears twice in a row, like xx, abcdde (dd),
 *     or aabbccdd (aa, bb, cc, or dd).
 *   - It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
 *
 * For example:
 *
 *   - ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...),
 *     and none of the disallowed substrings.
 *   - aaa is nice because it has at least three vowels and a double letter, even though the letters used by different
 *     rules overlap.
 *   - jchzalrnumimnmhp is naughty because it has no double letter.
 *   - haegwjzuvuyypxyu is naughty because it contains the string xy.
 *   - dvszwmarrgswjxmb is naughty because it contains only one vowel.
 *
 * How many strings are nice?
 *
 * --- Part Two ---
 *
 * Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or
 * nice.  None of the old rules apply, as they are all clearly ridiculous.
 *
 * Now, a nice string is one with all of the following properties:
 *
 *   - It contains a pair of any two letters that appears at least twice in the string without overlapping,
 *     like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
 *   - It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe),
 *     or even aaa.
 *
 * For example:
 *
 *   - qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one
 *     letter between them (zxz).
 *   - xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though
 *     the letters used by each rule overlap.
 *   - uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
 *   - ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears
 *     twice.
 *
 * How many strings are nice under these new rules?
 */
package io.ysakhno.adventofcode2015.day05

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val String.isNiceByRule11 get() = count { it in "aeiou" } >= 3
private val String.isNiceByRule12 get() = zipWithNext().count { (first, second) -> first == second } > 0
private val String.isNiceByRule13 get() = !contains("ab|cd|pq|xy".toRegex())

private val String.isNiceByRule21 get() = matches(".*(..).*\\1.*".toRegex())
private val String.isNiceByRule22 get() = matches(".*(.).\\1.*".toRegex())

private fun part1(input: List<String>) = input.count { it.isNiceByRule11 && it.isNiceByRule12 && it.isNiceByRule13 }

private fun part2(input: List<String>) = input.count { it.isNiceByRule21 && it.isNiceByRule22 }

fun main() {
    // Test if implementation meets criteria from the description
    val testInput1 = problemInput.readTest(1)
    val testInput2 = problemInput.readTest(2)
    assertEquals(2, part1(testInput1), "Part one (sample input)")
    assertEquals(2, part2(testInput2), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
