/*
 * --- Day 20: Infinite Elves and Infinite Houses ---
 *
 * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door.  He sends them down a street with
 * infinite houses numbered sequentially: 1, 2, 3, 4, 5, and so on.
 *
 * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
 *
 *   - The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
 *   - The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
 *   - Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
 *
 * There are infinitely many Elves, numbered starting with 1.  Each Elf delivers presents equal to ten times his or her
 * number at each house.
 *
 * So, the first nine houses on the street end up like this:
 *
 *   House 1 got 10 presents.
 *   House 2 got 30 presents.
 *   House 3 got 40 presents.
 *   House 4 got 70 presents.
 *   House 5 got 60 presents.
 *   House 6 got 120 presents.
 *   House 7 got 80 presents.
 *   House 8 got 150 presents.
 *   House 9 got 130 presents.
 *
 * The first house gets 10 presents: it is visited only by Elf 1, which delivers 1 * 10 = 10 presents.  The fourth house
 * gets 70 presents, because it is visited by Elves 1, 2, and 4, for a total of 10 + 20 + 40 = 70 presents.
 *
 * What is the lowest house number of the house to get at least as many presents as the number in your puzzle input?
 *
 * --- Part Two ---
 *
 * The Elves decide they don't want to visit an infinite number of houses.  Instead, each Elf will stop after delivering
 * presents to 50 houses.  To make up for it, they decide to deliver presents equal to eleven times their number at each
 * house.
 *
 * With these changes, what is the new lowest house number of the house to get at least as many presents as the number
 * in your puzzle input?
 */
package io.ysakhno.adventofcode2015.day20

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import kotlin.math.sqrt
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val Int.allDivisors
    get() = (1..sqrt(toDouble()).toInt())
        .filter { this % it == 0 }
        .flatMap { setOf(it, this / it) }

private fun part1(threshold: Int) = generateSequence(1) { it + 1 }.map { houseNumber ->
    houseNumber.allDivisors.sumOf { it * 10 }
}.indexOfFirst { it >= threshold } + 1

private fun part2(threshold: Int) = generateSequence(1) { it + 1 }.map { houseNumber ->
    houseNumber.allDivisors.filter { houseNumber / it <= 50 }.sumOf { it * 11 }
}.indexOfFirst { it >= threshold } + 1

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest().first().toInt()
    assertEquals(6, part1(testInput), "Part one (sample input)")
    assertEquals(6, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read().first().toInt()
    part1(input).println()
    part2(input).println()
}
