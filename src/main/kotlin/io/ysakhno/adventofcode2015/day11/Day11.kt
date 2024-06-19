/*
 * --- Day 11: Corporate Policy ---
 *
 * Santa's previous password expired, and he needs help choosing a new one.
 *
 * To help him remember his new password after the old one expires, Santa has devised a method of coming up with a
 * password based on the previous one.  Corporate policy dictates that passwords must be exactly eight lowercase letters
 * (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is
 * valid.
 *
 * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on.  Increase the rightmost letter one
 * step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.
 *
 * Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password
 * requirements:
 *
 *   - Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on,
 *     up to xyz.  They cannot skip letters; abd doesn't count.
 *   - Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are
 *     therefore confusing.
 *   - Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
 *
 * For example:
 *
 *   - hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement
 *     (because it contains i and l).
 *   - abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 *   - abbcegjk fails the third requirement, because it only has one double letter (bb).
 *   - The next password after abcdefgh is abcdffaa.
 *   - The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords
 *     that start with ghi..., since i is not allowed.
 *
 * Given Santa's current password (your puzzle input), what should his next password be?
 *
 * --- Part Two ---
 *
 * Santa's password expired again. What's the next one?
 */
package io.ysakhno.adventofcode2015.day11

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun String.increment(): String {
    val chars = toCharArray()

    for (idx in chars.size - 1 downTo 0) {
        if (chars[idx] != 'z') {
            ++chars[idx]
            break
        } else if (idx == 0) {
            return "a".repeat(8)
        } else {
            chars[idx] = 'a'
        }
    }

    return chars.concatToString()
}

private val String.isRequirement0Satisfied get() = length == 8
private val String.isRequirement1Satisfied: Boolean
    get() {
        for (i in 0..<length - 2) {
            if (this[i] + 1 == this[i + 1] && this[i + 1] + 1 == this[i + 2]) return true
        }
        return false
    }
private val String.isRequirement2Satisfied: Boolean get() = !contains("[ilo]".toRegex())
private val String.isRequirement3Satisfied: Boolean get() = matches(".*(.)\\1.*(.)\\2.*".toRegex())

private fun generatePasswords(seed: String) = generateSequence(seed, String::increment)
    .drop(1)
    .takeWhile { it != seed }
    .filter(String::isRequirement0Satisfied)
    .filter(String::isRequirement1Satisfied)
    .filter(String::isRequirement2Satisfied)
    .filter(String::isRequirement3Satisfied)

private fun part1(input: String) = generatePasswords(input).take(1).last()

private fun part2(input: String) = generatePasswords(input).take(2).last()

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals("abcdffaa", part1(testInput[0]), "Part one (sample input: ${testInput[0]})")
    assertEquals("ghjaabcc", part1(testInput[1]), "Part one (sample input: ${testInput[1]})")
    println("All tests passed")

    val input = problemInput.read().first()
    part1(input).println()
    part2(input).println()
}
