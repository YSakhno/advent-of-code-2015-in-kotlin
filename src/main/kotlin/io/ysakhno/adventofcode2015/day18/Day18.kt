/*
 * --- Day 18: Like a GIF For Your Yard ---
 *
 * After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed.
 * You arrange them in a 100x100 grid.
 *
 * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration.  With so few
 * lights, he says, you'll have to resort to animation.
 *
 * Start by setting your lights to the included initial configuration (your puzzle input).  A # means "on",
 * and a . means "off".
 *
 * Then, animate your grid in steps, where each step decides the next configuration based on the current one.  Each
 * light's next state (either on or off) depends on its current state and the current states of the eight lights
 * adjacent to it (including diagonals).  Lights on the edge of the grid might have fewer than eight neighbors; the
 * missing ones always count as "off".
 *
 * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
 * marked B, which is on an edge, only has the neighbors marked 1 through 5:
 *
 *   1B5...
 *   234...
 *   ......
 *   ..123.
 *   ..8A4.
 *   ..765.
 *
 * The state a light should have next is based on its current state (on or off) plus the number of neighbors that are
 * on:
 *
 *   - A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
 *   - A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
 *
 * All of the lights update simultaneously; they all consider the same current state before moving to the next.
 *
 * Here's a few steps from an example configuration of another 6x6 grid:
 *
 *   Initial state:
 *   .#.#.#
 *   ...##.
 *   #....#
 *   ..#...
 *   #.#..#
 *   ####..
 *
 *   After 1 step:
 *   ..##..
 *   ..##.#
 *   ...##.
 *   ......
 *   #.....
 *   #.##..
 *
 *   After 2 steps:
 *   ..###.
 *   ......
 *   ..###.
 *   ......
 *   .#....
 *   .#....
 *
 *   After 3 steps:
 *   ...#..
 *   ......
 *   ...#..
 *   ..##..
 *   ......
 *   ......
 *
 *   After 4 steps:
 *   ......
 *   ......
 *   ..##..
 *   ..##..
 *   ......
 *   ......
 *
 * After 4 steps, this example has four lights on.
 *
 * In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?
 *
 * --- Part Two ---
 *
 * You flip the instructions over; Santa goes on to point out that this is all just an implementation of Conway's Game
 * of Life.  At least, it was, until you notice that something's wrong with the grid of lights you bought: four lights,
 * one in each corner, are stuck on and can't be turned off.  The example above will actually run like this:
 *
 *   Initial state:
 *   ##.#.#
 *   ...##.
 *   #....#
 *   ..#...
 *   #.#..#
 *   ####.#
 *
 *   After 1 step:
 *   #.##.#
 *   ####.#
 *   ...##.
 *   ......
 *   #...#.
 *   #.####
 *
 *   After 2 steps:
 *   #..#.#
 *   #....#
 *   .#.##.
 *   ...##.
 *   .#..##
 *   ##.###
 *
 *   After 3 steps:
 *   #...##
 *   ####.#
 *   ..##.#
 *   ......
 *   ##....
 *   ####.#
 *
 *   After 4 steps:
 *   #.####
 *   #....#
 *   ...#..
 *   .##...
 *   #.....
 *   #.#..#
 *
 *   After 5 steps:
 *   ##.###
 *   .##..#
 *   .##...
 *   .##...
 *   #.#...
 *   ##...#
 *
 * After 5 steps, this example now has 17 lights on.
 *
 * In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on state,
 * how many lights are on after 100 steps?
 */
package io.ysakhno.adventofcode2015.day18

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun List<BooleanArray>.simulate(areCornerLightStuck: Boolean): List<BooleanArray> {
    val result = map { BooleanArray(it.size) }

    for (i in result.indices) {
        val resultLine = result[i]

        for (j in resultLine.indices) {
            var count = 0
            if (i > 0) {
                if (j > 0 && this[i - 1][j - 1]) ++count
                if (this[i - 1][j]) ++count
                if (j < resultLine.lastIndex && this[i - 1][j + 1]) ++count
            }
            if (j > 0 && this[i][j - 1]) ++count
            if (j < resultLine.lastIndex && this[i][j + 1]) ++count
            if (i < lastIndex) {
                if (j > 0 && this[i + 1][j - 1]) ++count
                if (this[i + 1][j]) ++count
                if (j < resultLine.lastIndex && this[i + 1][j + 1]) ++count
            }

            result[i][j] = count == 3 || (this[i][j] && count == 2)
        }
    }

    if (areCornerLightStuck) {
        result.first().apply {
            this[0] = true
            this[lastIndex] = true
        }
        result.last().apply {
            this[0] = true
            this[lastIndex] = true
        }
    }

    return result
}

private fun solve(input: List<String>, numSteps: Int, areCornerLightStuck: Boolean = false): Int {
    val grid = input.map { line -> line.map { it == '#' } }.map(List<Boolean>::toBooleanArray)

    return (1..numSteps).fold(grid) { g, _ -> g.simulate(areCornerLightStuck) }.sumOf { line -> line.count { it } }
}

private fun part1(input: List<String>, numSteps: Int) = solve(input, numSteps)

private fun part2(input: List<String>, numSteps: Int) = solve(input, numSteps, true)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput1 = problemInput.readTest(1)
    val testInput2 = problemInput.readTest(2)
    assertEquals(4, part1(testInput1, 4), "Part one (sample input)")
    assertEquals(17, part2(testInput2, 5), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input, 100).println()
    part2(input, 100).println()
}
