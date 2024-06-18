/*
 * --- Day 6: Probably a Fire Hazard ---
 *
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to
 * deploy one million lights in a 1000x1000 grid.
 *
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the
 * ideal lighting configuration.
 *
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999,
 * 999,999, and 999,0.  The instructions include whether to turn on, turn off, or toggle various inclusive ranges given
 * as coordinate pairs.  Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair
 * like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square.  The lights all start turned off.
 *
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you
 * in order.
 *
 * For example:
 *
 *   - turn on 0,0 through 999,999 would turn on (or leave on) every light.
 *   - toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and
 *     turning on the ones that were off.
 *   - turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 *
 * After following the instructions, how many lights are lit?
 *
 * --- Part Two ---
 *
 * You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from
 * Ancient Nordic Elvish.
 *
 * The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or
 * more.  The lights all start at zero.
 *
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 *
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of
 * zero.
 *
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 *
 * What is the total brightness of all lights combined after following Santa's instructions?
 *
 * For example:
 *
 *   - turn on 0,0 through 0,0 would increase the total brightness by 1.
 *   - toggle 0,0 through 999,999 would increase the total brightness by 2000000.
 */
package io.ysakhno.adventofcode2015.day06

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.allInts
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private enum class Action(val actionPrefix: String) {
    TURN_OFF("turn off"),
    TURN_ON("turn on"),
    TOGGLE("toggle"),
}

private data class Rect(var left: Int, var top: Int, var right: Int, var bottom: Int)

private data class Instruction(val action: Action, val rect: Rect)

private fun String.toInstruction(): Instruction {
    val (x1, y1, x2, y2) = allInts().toList()
    val action = Action.entries.first { this.startsWith(it.actionPrefix) }
    val rect = Rect(x1, y1, x2, y2)

    return Instruction(action, rect)
}

private fun List<String>.toInstructions() = map(String::toInstruction)

private fun Array<BooleanArray>.turnOn(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            this[y][x] = true
        }
    }
}

private fun Array<BooleanArray>.turnOff(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            this[y][x] = false
        }
    }
}

private fun Array<BooleanArray>.toggle(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            this[y][x] = !this[y][x]
        }
    }
}

private fun part1(input: List<String>): Int {
    val grid = Array(1000) { BooleanArray(1000) }

    input.toInstructions().forEach { (action, rect) ->
        when (action) {
            Action.TURN_OFF -> grid.turnOff(rect)
            Action.TURN_ON -> grid.turnOn(rect)
            Action.TOGGLE -> grid.toggle(rect)
        }
    }

    return grid.flatMap(BooleanArray::toList).count { it }
}

private fun Array<LongArray>.turnOn(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            ++this[y][x]
        }
    }
}

private fun Array<LongArray>.turnOff(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            this[y][x] = (this[y][x] - 1L).coerceAtLeast(0L)
        }
    }
}

private fun Array<LongArray>.toggle(rect: Rect) {
    for (y in rect.top..rect.bottom) {
        for (x in rect.left..rect.right) {
            this[y][x] += 2L
        }
    }
}

private fun part2(input: List<String>): Long {
    val grid = Array(1000) { LongArray(1000) }

    input.toInstructions().forEach { (action, rect) ->
        when (action) {
            Action.TURN_OFF -> grid.turnOff(rect)
            Action.TURN_ON -> grid.turnOn(rect)
            Action.TOGGLE -> grid.toggle(rect)
        }
    }

    return grid.flatMap(LongArray::toList).sum()
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(1_000_000 - 1000 - 4, part1(testInput), "Part one (sample input)")
    assertEquals(1_000_000L + 2000L - 4L, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
