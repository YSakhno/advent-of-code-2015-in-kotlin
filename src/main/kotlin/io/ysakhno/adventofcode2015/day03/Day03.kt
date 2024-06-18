/*
 * --- Day 3: Perfectly Spherical Houses in a Vacuum ---
 *
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 *
 * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him
 * via radio and tells him where to move next.  Moves are always exactly one house to the north (^), south (v), east
 * (>), or west (<).  After each move, he delivers another present to the house at his new location.
 *
 * However, the elf back at the North Pole has had a little too much beer, and so his directions are a little off, and
 * Santa ends up visiting some houses more than once.  How many houses receive at least one present?
 *
 * For example:
 *
 *   - > delivers presents to 2 houses: one at the starting location, and one to the east.
 *   - ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 *   - ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 *
 * --- Part Two ---
 *
 * The next year, to speed up the process, Santa creates a robot version of himself, Claus, to deliver presents
 * with him.
 *
 * Santa and Claus start at the same location (delivering two presents to the same starting house), then take turns
 * moving based on instructions from the elf, who is reading from the same script as the previous year.
 *
 * This year, how many houses receive at least one present?
 *
 * For example:
 *
 *   - ^v delivers presents to 3 houses, because Santa goes north, and then Claus goes south.
 *   - ^>v< now delivers presents to 3 houses, and Santa and Claus end up back where they started.
 *   - ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Claus going the other.
 */
package io.ysakhno.adventofcode2015.day03

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class House(val x: Int, val y: Int)

private fun part1(input: List<String>): Int {
    var house = House(0, 0)
    val houses = mutableSetOf(house)

    for (dir in input.first()) {
        var (x, y) = house

        when (dir) {
            '>' -> ++x
            '<' -> --x
            'v' -> ++y
            '^' -> --y
            else -> error("Unexpected character $dir")
        }

        house = House(x, y)
        houses += house
    }

    return houses.size
}

private fun part2(input: List<String>): Int {
    var santa = House(0, 0)
    var claus = House(0, 0)
    var isSantaTurn = true
    val houses = mutableSetOf(santa, claus)

    for (dir in input.first()) {
        var (x, y) = if (isSantaTurn) santa else claus

        when (dir) {
            '>' -> ++x
            '<' -> --x
            'v' -> ++y
            '^' -> --y
            else -> error("Unexpected character $dir")
        }

        val house = House(x, y)
        if (isSantaTurn) santa = house else claus = house
        isSantaTurn = !isSantaTurn
        houses += house
    }

    return houses.size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(2, part1(testInput), "Part one (sample input)")
    assertEquals(11, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
