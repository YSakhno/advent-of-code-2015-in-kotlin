/*
 * --- Day 4: The Ideal Stocking Stuffer ---
 *
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically
 * forward-thinking little girls and boys.
 *
 * To do this, he needs to find MD5 hashes, which, in hexadecimal, start with at least five zeroes.  The input to the
 * MD5 hash is some secret key (the puzzle input) followed by a number in decimal.  To mine AdventCoins, you must find
 * Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.
 *
 * For example:
 *
 *   - If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes
 *     (000001dbbfa...), and it is the lowest such number to do so.
 *   - If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes
 *     is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
 *
 * --- Part Two ---
 *
 * Now find one that starts with six zeroes.
 */
package io.ysakhno.adventofcode2015.day04

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import java.security.MessageDigest
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

@OptIn(ExperimentalStdlibApi::class)
private fun String.md5() = MessageDigest.getInstance("MD5").digest(toByteArray()).toHexString()

private fun String.findWithMD5Prefix(prefix: String) =
    generateSequence(1) { it + 1 }.map { this + it }.map(String::md5).indexOfFirst { it.startsWith(prefix) } + 1

private fun part1(key: String) = key.findWithMD5Prefix("0".repeat(5))

private fun part2(key: String) = key.findWithMD5Prefix("0".repeat(6))

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest().first()
    assertEquals(1048970, part1(testInput), "Part one (sample input)")
    assertEquals(5714438, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read().first()
    part1(input).println()
    part2(input).println()
}
