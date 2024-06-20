/*
 * --- Day 16: Aunt Sue ---
 *
 * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a “Thank You” card.  However, there's a
 * small problem: she signed it "From, Aunt Sue".
 *
 * You have 500 Aunts named "Sue".
 *
 * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently
 * number 1 to 500, for sanity) gave you the gift.  You open the present and, as luck would have it, good ol' Aunt Sue
 * got you a My First Crime Scene Analysis Machine!  Just what you wanted.  Or needed, as the case may be.
 *
 * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given sample,
 * as well as how many distinct kinds of those compounds there are.  According to the instructions, these are what the
 * MFCSAM can detect:
 *
 *   - children, by human DNA age analysis.
 *   - cats.  It doesn't differentiate individual breeds.
 *   - Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
 *   - goldfish.  No other kinds of fish.
 *   - trees, all in one group.
 *   - cars, presumably by exhaust or gasoline or something.
 *   - perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
 *
 * In fact, many of your Aunts Sue have many of these.  You put the wrapping from the gift into the MFCSAM.  It beeps
 * inquisitively at you a few times and then prints out a message on ticker tape:
 *
 *   children: 3
 *   cats: 7
 *   samoyeds: 2
 *   pomeranians: 3
 *   akitas: 0
 *   vizslas: 0
 *   goldfish: 5
 *   trees: 3
 *   cars: 2
 *   perfumes: 1
 *
 * You make a list of the things you can remember about each Aunt Sue.  Things missing from your list aren't zero - you
 * simply don't remember the value.
 *
 * What is the number of the Sue that got you the gift?
 *
 * --- Part Two ---
 *
 * As you're about to send the “Thank You” note, something in the MFCSAM's instructions catches your eye.  Apparently,
 * the output from the machine isn't exact values - some of them indicate ranges.
 *
 * In particular, the cats and trees readings indicates that there are greater than that many, while the pomeranians and
 * goldfish readings indicate that there are fewer than that many.
 *
 * What is the number of the real Aunt Sue?
 */
package io.ysakhno.adventofcode2015.day16

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.allInts
import io.ysakhno.adventofcode2015.util.println
import java.util.Locale

private val problemInput = object : ProblemInput {}

private enum class FeatureType {
    CHILDREN,
    CATS,
    SAMOYEDS,
    POMERANIANS,
    AKITAS,
    VIZSLAS,
    GOLDFISH,
    TREES,
    CARS,
    PERFUMES,
}

private typealias Features = Map<FeatureType, Int>

private val analysis = mapOf(
    FeatureType.CHILDREN to 3,
    FeatureType.CATS to 7,
    FeatureType.SAMOYEDS to 2,
    FeatureType.POMERANIANS to 3,
    FeatureType.AKITAS to 0,
    FeatureType.VIZSLAS to 0,
    FeatureType.GOLDFISH to 5,
    FeatureType.TREES to 3,
    FeatureType.CARS to 2,
    FeatureType.PERFUMES to 1,
)

private fun parseFeatures(line: String): Pair<Int, Features> {
    val (aunt, list) = line.split(':', limit = 2)
    val auntNumber = aunt.allInts().first()
    val items = list.split(',')

    return auntNumber to items.associate { item ->
        val (type, count) = item.split(':')

        FeatureType.valueOf(type.trim().uppercase(Locale.US)) to count.trim().toInt()
    }
}

private fun matches(auntDesc: Pair<Int, Features>) =
    auntDesc.second.all { analysis[it.key] == it.value }

private fun part1(input: List<String>) = input.map(::parseFeatures).first(::matches).first

private fun matchesAlternatively(auntDesc: Pair<Int, Features>) = auntDesc.second.all {
    when (it.key) {
        FeatureType.CATS, FeatureType.TREES -> analysis.getValue(it.key) < it.value
        FeatureType.POMERANIANS, FeatureType.GOLDFISH -> analysis.getValue(it.key) > it.value
        else -> analysis[it.key] == it.value
    }
}

private fun part2(input: List<String>) = input.map(::parseFeatures).first(::matchesAlternatively).first

fun main() {
    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
