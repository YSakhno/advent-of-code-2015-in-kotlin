/*
 * --- Day 9: All in a Single Night ---
 *
 * Every year, Santa manages to deliver all of his presents in a single night.
 *
 * This year, however, he has some new locations to visit; his elves have provided him the distances between every pair
 * of locations.  He can start and end at any two (different) locations he wants, but he must visit each location
 * exactly once.  What is the shortest distance he can travel to achieve this?
 *
 * For example, given the following distances:
 *
 *   London to Dublin = 464
 *   London to Belfast = 518
 *   Dublin to Belfast = 141
 *
 * The possible routes are therefore:
 *
 *   - Dublin -> London -> Belfast = 982
 *   - London -> Dublin -> Belfast = 605
 *   - London -> Belfast -> Dublin = 659
 *   - Dublin -> Belfast -> London = 659
 *   - Belfast -> Dublin -> London = 605
 *   - Belfast -> London -> Dublin = 982
 *
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
 *
 * What is the distance of the shortest route?
 *
 * --- Part Two ---
 *
 * The next year, just to show off, Santa decides to take the route with the longest distance instead.
 *
 * He can still start and end at any two (different) locations he wants, and he still must visit each location exactly
 * once.
 *
 * For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.
 *
 * What is the distance of the longest route?
 */
package io.ysakhno.adventofcode2015.day09

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.allInts
import io.ysakhno.adventofcode2015.util.allWords
import io.ysakhno.adventofcode2015.util.permutations
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun getDistance(line: String): List<Pair<Pair<String, String>, Int>> {
    val (loc1, _, loc2) = line.allWords().toList()
    val distance = line.allInts().toList().last()

    return listOf((loc1 to loc2) to distance, (loc2 to loc1) to distance)
}

private fun solve(input: List<String>, isLookingForMinimal: Boolean): Int {
    val distances = input.flatMap(::getDistance).toMap()
    val locations = distances.keys.map { (loc) -> loc }.distinct()
    val pathLengths = locations.permutations()
        .map(List<String>::zipWithNext)
        .filter { path -> path.all { it in distances.keys } }
        .map { path -> path.sumOf { distances[it] ?: 0 } }

    return if (isLookingForMinimal) pathLengths.min() else pathLengths.max()
}

private fun part1(input: List<String>) = solve(input, true)

private fun part2(input: List<String>) = solve(input, false)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(605, part1(testInput), "Part one (sample input)")
    assertEquals(982, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
