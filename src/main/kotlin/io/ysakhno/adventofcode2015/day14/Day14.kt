/*
 * --- Day 14: Reindeer Olympics ---
 *
 * This year is the Reindeer Olympics!  Reindeer can fly at high speeds, but must rest occasionally to recover their
 * energy.  Santa would like to know which of his reindeer is fastest, and so he has them race.
 *
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all), and always spend whole
 * seconds in either state.
 *
 * For example, suppose you have the following Reindeer:
 *
 *   Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 *   Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 *
 * After one second, Comet has gone 14 km, while Dancer has gone 16 km.  After ten seconds, Comet has gone 140 km, while
 * Dancer has gone 160 km.  On the eleventh second, Comet begins resting (staying at 140 km), and Dancer continues on
 * for a total distance of 176 km.  On the 12th second, both reindeer are resting.  They continue to rest until the
 * 138th second, when Comet flies for another ten seconds.  On the 174th second, Dancer flies for another 11 seconds.
 *
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120 km (poor Dancer
 * has only gotten 1056 km by that point).  So, in this situation, Comet would win (if the race ended at 1000 seconds).
 *
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, what distance has the
 * winning reindeer traveled?
 *
 * --- Part Two ---
 *
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 *
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead.  (If there are
 * multiple reindeer tied for the lead, they each get one point.)  He keeps the traditional 2503 seconds time limit, of
 * course, as doing otherwise would be entirely ridiculous.
 *
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point.  He stays in
 * the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the lead and gets
 * his first point.  Of course, since Dancer had been in the lead for the 139 seconds before that, he has accumulated
 * 139 points by the 140th second.
 *
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion, only has 312.
 * So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
 *
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, how many points
 * does the winning reindeer have?
 */
package io.ysakhno.adventofcode2015.day14

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Reindeer(val name: String, val speed: Int, val flyingTime: Int, val restingTime: Int)

private val REINDEER_DESCRIPTION_REGEX = """(?x)
        (?<name>[A-Z][a-z]+)\x20
        can\x20fly\x20
        (?<speed>0|[1-9][0-9]*)\x20km/s\x20
        for\x20(?<flyingTime>0|[1-9][0-9]*)\x20seconds,\x20
        but\x20then\x20must\x20rest\x20for\x20
        (?<restingTime>0|[1-9][0-9]*)\x20seconds\.
    """.toRegex()

private fun parseReindeer(line: String): Reindeer {
    val mr = REINDEER_DESCRIPTION_REGEX.matchEntire(line) ?: error("Could not parse '$line'")

    val name = mr.groups["name"]?.value ?: error("Error in regular expression")
    val speed = mr.groups["speed"]?.value?.toInt() ?: error("Error in regular expression")
    val flyingTime = mr.groups["flyingTime"]?.value?.toInt() ?: error("Error in regular expression")
    val restingTime = mr.groups["restingTime"]?.value?.toInt() ?: error("Error in regular expression")

    return Reindeer(name = name, speed = speed, flyingTime = flyingTime, restingTime = restingTime)
}

private data class ReindeerState(
    val reindeer: Reindeer,
    var distanceTravelled: Int = 0,
    var timeInState: Int = 1,
    var isFlying: Boolean = true,
    var score: Int = 0,
) {
    fun modify(): ReindeerState {
        if (isFlying) {
            if (timeInState >= reindeer.flyingTime) {
                timeInState = 0
                isFlying = false
            }
            distanceTravelled += reindeer.speed
        } else {
            if (timeInState >= reindeer.restingTime) {
                timeInState = 0
                isFlying = true
            }
        }
        ++timeInState
        return this
    }
}

private fun part1(input: List<String>, raceDuration: Int) =
    (1..raceDuration).fold(input.map(::parseReindeer).map(::ReindeerState)) { states, _ ->
        states.map(ReindeerState::modify)
    }.maxOf(ReindeerState::distanceTravelled)

private inline fun <T, R : Comparable<R>> Iterable<T>.allMaxBy(selector: (T) -> R) =
    maxOf(selector).let { maxValue -> filter { element -> selector(element) == maxValue } }

private fun part2(input: List<String>, raceDuration: Int) =
    (1..raceDuration).fold(input.map(::parseReindeer).map(::ReindeerState)) { states, _ ->
        states.map(ReindeerState::modify).also { st ->
            st.allMaxBy(ReindeerState::distanceTravelled).forEach { ++it.score }
        }
    }.maxOf(ReindeerState::score)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(1120, part1(testInput, 1000), "Part one (sample input)")
    assertEquals(689, part2(testInput, 1000), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input, 2503).println()
    part2(input, 2503).println()
}
