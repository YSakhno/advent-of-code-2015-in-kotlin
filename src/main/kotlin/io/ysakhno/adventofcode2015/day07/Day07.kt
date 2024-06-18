/*
 * --- Day 7: Some Assembly Required ---
 *
 * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates!  Unfortunately, little Bobby is
 * a little under the recommended age range, and he needs help assembling the circuit.
 *
 * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).  A
 * signal is provided to each wire by a gate, another wire, or some specific value.  Each wire can only get a signal
 * from one source, but can provide its signal to multiple destinations.  A gate provides no signal until all of its
 * inputs have a signal.
 *
 * The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x
 * and y to an AND gate, and then connect its output to wire z.
 *
 * For example:
 *
 *   - 123 -> x means that the signal 123 is provided to wire x.
 *   - x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
 *   - p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
 *   - NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
 *
 * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift).  If, for some reason, you'd like to emulate
 * the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for
 * these gates.
 *
 * For example, here is a simple circuit:
 *
 *   123 -> x
 *   456 -> y
 *   x AND y -> d
 *   x OR y -> e
 *   x LSHIFT 2 -> f
 *   y RSHIFT 2 -> g
 *   NOT x -> h
 *   NOT y -> i
 *
 * After it is run, these are the signals on the wires:
 *
 *   d: 72
 *   e: 507
 *   f: 492
 *   g: 114
 *   h: 65412
 *   i: 65079
 *   x: 123
 *   y: 456
 *
 * In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to
 * wire a?
 *
 * --- Part Two ---
 *
 * Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a).
 * What new signal is ultimately provided to wire a?
 */
package io.ysakhno.adventofcode2015.day07

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.allWords
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private sealed interface Wire {
    val name: String
    val value: Int
    var inputFrom: Gate?
}

private data class NamedWire(override val name: String, override var value: Int = -1) : Wire {
    override var inputFrom: Gate? = null
}

private data class AdHocWire(override val value: Int) : Wire {
    override val name: String get() = "??"
    override var inputFrom: Gate?
        get() = null
        set(@Suppress("UNUSED_PARAMETER") ignored) {}
}

private data object EmptyWire : Wire {
    override val name: String get() = "??"
    override val value: Int get() = 0
    override var inputFrom: Gate?
        get() = null
        set(@Suppress("UNUSED_PARAMETER") ignored) {}
}

private enum class Operation { AND, OR, LSHIFT, RSHIFT, NOT, SET }

private data class Gate(val input1: Wire, val operation: Operation, val input2: Wire, val output: NamedWire) {
    fun computeSignal(): Int {
        if (input1.value < 0) input1.inputFrom!!.computeSignal()
        if (input2.value < 0) input2.inputFrom!!.computeSignal()

        return computeInternal()
    }

    private fun computeInternal(): Int {
        check(input1 is EmptyWire || input1.value >= 0) { "Input 1 has no signal" }
        check(input2 is EmptyWire || input2.value >= 0) { "Input 2 has no signal" }

        return when (operation) {
            Operation.AND -> input1.value and input2.value
            Operation.OR -> input1.value or input2.value
            Operation.LSHIFT -> input1.value shl input2.value
            Operation.RSHIFT -> input1.value shr input2.value
            Operation.NOT -> 65_535 - input2.value.coerceAtMost(65_535)
            Operation.SET -> input1.value
        }.also { output.value = it }
    }
}

private fun parseWires(input: List<String>) =
    input.flatMap { it.allWords().filter { word -> word.all(Char::isLowerCase) } }
        .distinct()
        .map(::NamedWire)
        .associateBy(NamedWire::name)

private val ASSIGNMENT_REGEX = "(?<wire>[a-z]+)|(?<lit>0|[1-9][0-9]*)".toRegex()
private val BINARY_OP_REGEX =
    """(?x)
        (?:(?<wire1>[a-z]+)|(?<lit1>0|[1-9][0-9]*))
        \s+
        (?<op>AND|OR|[LR]SHIFT)
        \s+
        (?:(?<wire2>[a-z]+)|(?<lit2>0|[1-9][0-9]*))
    """.toRegex()
private val UNARY_OP_REGEX = "(?<op>NOT)\\s+(?<wire>[a-z]+)".toRegex()

private fun constructGate(line: String, wires: Map<String, NamedWire>): Gate {
    val (src, dest) = line.split("->").map(String::trim)
    val destWire = wires.getValue(dest)

    ASSIGNMENT_REGEX.matchEntire(src)?.let { mr ->
        val wireName = mr.groups["wire"]?.value
        val literal = mr.groups["lit"]?.value

        val input =
            if (literal != null) AdHocWire(literal.toInt(10))
            else if (wireName != null) wires.getValue(wireName)
            else error("Error in regular expression")

        return Gate(input, Operation.SET, EmptyWire, destWire)
    }

    BINARY_OP_REGEX.matchEntire(src)?.let { mr ->
        val wireName1 = mr.groups["wire1"]?.value
        val wireName2 = mr.groups["wire2"]?.value
        val literal1 = mr.groups["lit1"]?.value
        val literal2 = mr.groups["lit2"]?.value
        val op = when (mr.groups["op"]?.value) {
            "AND" -> Operation.AND
            "OR" -> Operation.OR
            "LSHIFT" -> Operation.LSHIFT
            "RSHIFT" -> Operation.RSHIFT
            else -> error("Unknown operator: ${mr.groups["op"]?.value}")
        }

        check(wireName2 != null || literal2 != null) {
            "Either the name of the second wire or the second literal must be defined"
        }

        val input1 =
            if (wireName1 != null) wires.getValue(wireName1)
            else if (literal1 != null) AdHocWire(literal1.toInt(10))
            else error("Either the name of the first wire or the first literal must be defined")
        val input2 =
            if (wireName2 != null) wires.getValue(wireName2)
            else if (literal2 != null) AdHocWire(literal2.toInt(10))
            else error("Either the name of the second wire or the second literal must be defined")

        return Gate(input1, op, input2, destWire)
    }
    UNARY_OP_REGEX.matchEntire(src)?.let { mr ->
        val wireName = mr.groups["wire"]?.value ?: error("Error in regular expression")

        return Gate(EmptyWire, Operation.NOT, wires.getValue(wireName), destWire)
    }

    error("Impossible to create a Gate from expression '$line'")
}

private fun part1(input: List<String>): Int {
    val wires = parseWires(input)
    val wireA = wires["a"] ?: error("Input does not have any wires named 'a'")

    input.map { constructGate(it, wires) }.forEach { gate -> gate.output.inputFrom = gate }

    return wireA.inputFrom!!.computeSignal()
}

private fun part2(input: List<String>): Int {
    val wires = parseWires(input)
    val wireA = wires["a"] ?: error("Input does not have any wires named 'a'")
    val wireB = wires["b"] ?: error("Input does not have any wires named 'b'")

    input.map { constructGate(it, wires) }.forEach { gate -> gate.output.inputFrom = gate }

    val firstTime = wireA.inputFrom!!.computeSignal()

    wires.values.forEach { it.value = -1 }
    wireB.value = firstTime

    return wireA.inputFrom!!.computeSignal()
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput1 = problemInput.readTest(1)
    val testInput2 = problemInput.readTest(2)
    assertEquals(507, part1(testInput1), "Part one (sample input)")
    assertEquals(507, part2(testInput2), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
