/*
 * --- Day 13: Knights of the Dinner Table ---
 *
 * In years past, the holiday feast with your family hasn't gone so well.  Not everyone gets along!  This year, you
 * resolve, will be different.  You're going to find the optimal seating arrangement and avoid all those awkward
 * conversations.
 *
 * You start by writing up a list of everyone invited and the amount their happiness would increase or decrease if they
 * were to find themselves sitting next to each other person.  You have a circular table that will be just big enough to
 * fit everyone comfortably, and so each person will have exactly two neighbors.
 *
 * For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:
 *
 *   Alice would gain 54 happiness units by sitting next to Bob.
 *   Alice would lose 79 happiness units by sitting next to Carol.
 *   Alice would lose 2 happiness units by sitting next to David.
 *   Bob would gain 83 happiness units by sitting next to Alice.
 *   Bob would lose 7 happiness units by sitting next to Carol.
 *   Bob would lose 63 happiness units by sitting next to David.
 *   Carol would lose 62 happiness units by sitting next to Alice.
 *   Carol would gain 60 happiness units by sitting next to Bob.
 *   Carol would gain 55 happiness units by sitting next to David.
 *   David would gain 46 happiness units by sitting next to Alice.
 *   David would lose 7 happiness units by sitting next to Bob.
 *   David would gain 41 happiness units by sitting next to Carol.
 *
 * Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but David
 * would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.
 *
 * If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54).  Finally,
 * seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41).  The
 * arrangement looks like this:
 *
 *        +41 +46
 *   +55   David    -2
 *   Carol       Alice
 *   +60    Bob    +54
 *        -7  +83
 *
 * After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most
 * optimal, with a total change in happiness of 330.
 *
 * What is the total change in happiness for the optimal seating arrangement of the actual guest list?
 *
 * --- Part Two ---
 *
 * In all the commotion, you realize that you forgot to seat yourself.  At this point, you're pretty apathetic toward
 * the whole thing, and your happiness wouldn't really go up or down regardless of who you sit next to.  You assume
 * everyone else would be just as ambivalent about sitting next to you, too.
 *
 * So, add yourself to the list, and give all happiness relationships that involve you a score of 0.
 *
 * What is the total change in happiness for the optimal seating arrangement that actually includes yourself?
 */
package io.ysakhno.adventofcode2015.day13

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import io.ysakhno.adventofcode2015.util.swap
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val HAPPINESS_PATTERN_REGEX = """(?x)
        (?<name1>[A-Za-z]+)\x20
        would\x20
        (?<gainOrLose>gain|lose)\x20
        (?<change>0|[1-9][0-9]*)\x20
        happiness\x20units\x20by\x20sitting\x20next\x20to\x20
        (?<name2>[A-Za-z]+)\.
    """.toRegex()

private fun parseDisposition(line: String): Pair<Pair<String, String>, Int> {
    val match = HAPPINESS_PATTERN_REGEX.matchEntire(line) ?: error("Could not parse input line '$line'")

    val name1 = match.groups["name1"]?.value ?: error("Error in regular expression")
    val name2 = match.groups["name2"]?.value ?: error("Error in regular expression")
    val isGain = (match.groups["gainOrLose"]?.value ?: error("Error in regular expression")) == "gain"
    val absoluteChange = match.groups["change"]?.value?.toInt() ?: error("Error in regular expression")
    val change = if (isGain) absoluteChange else -absoluteChange

    return (name1 to name2) to change
}

private fun permutations(people: List<String>) = sequence {
    val queue = ArrayDeque<Set<String>>().apply { this += people.map(::setOf) }
    while (queue.isNotEmpty()) {
        val taken = queue.removeFirst()

        if (taken.size == people.size) yield(taken.toList())
        else people.filter { it !in taken }.forEach { queue.addLast(taken + it) }
    }
}

private fun computeHappiness(arrangement: List<String>, dispositions: Map<Pair<String, String>, Int>) =
    if (arrangement.isNotEmpty()) (arrangement + arrangement.first()).zipWithNext().sumOf { pair ->
        dispositions.getOrDefault(pair, 0) + dispositions.getOrDefault(pair.swap(), 0)
    } else 0

private fun solve(input: List<String>, isMeToo: Boolean): Int {
    val dispositions = input.associate(::parseDisposition)
    val people = dispositions.keys.map { it.first }.distinct().let { if (isMeToo) it + "Me" else it }

    return permutations(people).map { computeHappiness(it, dispositions) }.max()
}

private fun part1(input: List<String>) = solve(input, false)

private fun part2(input: List<String>) = solve(input, true)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(330, part1(testInput), "Part one (sample input)")
    assertEquals(286, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
