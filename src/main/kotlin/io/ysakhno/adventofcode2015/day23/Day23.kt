/*
 * --- Day 23: Opening the Turing Lock ---
 *
 * Little Jane Marie just got her very first computer for Christmas from some unknown benefactor.  It comes with
 * instructions and an example program, but the computer itself seems to be malfunctioning.  She's curious what the
 * program does, and would like you to help her run it.
 *
 * The manual explains that the computer supports two registers and six instructions.  The registers are named a and b,
 * can hold any non-negative integer, and begin with a value of 0.  The instructions are as follows:
 *
 *   - hlf r sets register r to half its current value, then continues with the next instruction.
 *   - tpl r sets register r to triple its current value, then continues with the next instruction.
 *   - inc r increments register r, adding 1 to it, then continues with the next instruction.
 *   - jmp offset is a jump; it continues with the instruction offset away relative to itself.
 *   - jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
 *   - jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
 *
 * All three jump instructions work with an offset relative to that instruction.  The offset is always written with a
 * prefix + or - to indicate the direction of the jump (forward or backward, respectively).  For example, jmp +1 would
 * simply continue with the next instruction, while jmp +0 would continuously jump back to itself forever.
 *
 * The program exits when it tries to run an instruction beyond the ones defined.
 *
 * For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
 *
 *   inc a
 *   jio a, +2
 *   tpl a
 *   inc a
 *
 * What is the value in register b when the program in your puzzle input is finished executing?
 *
 * --- Part Two ---
 *
 * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer.  Definitely
 * not to distract you, what is the value in register b after the program is finished executing if register a starts as
 * 1 instead?
 */
package io.ysakhno.adventofcode2015.day23

import io.ysakhno.adventofcode2015.day23.Mnemonic.HLF
import io.ysakhno.adventofcode2015.day23.Mnemonic.INC
import io.ysakhno.adventofcode2015.day23.Mnemonic.JIE
import io.ysakhno.adventofcode2015.day23.Mnemonic.JIO
import io.ysakhno.adventofcode2015.day23.Mnemonic.JMP
import io.ysakhno.adventofcode2015.day23.Mnemonic.TPL
import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private enum class Mnemonic(val value: String, val isFreeFlowing: Boolean = true) {
    HLF("hlf"),
    TPL("tpl"),
    INC("inc"),
    JMP("jmp", false),
    JIE("jie", false),
    JIO("jio", false),
}

private data class Instruction(val mnemonic: Mnemonic, val register: Char? = null, val offset: Int? = null) {
    val isFreeFlowing get() = mnemonic.isFreeFlowing
}

private typealias Program = List<Instruction>

private val List<String>.register get() = first().let { if (it == "a" || it == "b") it[0] else null }

private val List<String>.offset get() = firstOrNull { it[0] == '-' || it[0] == '+' }?.toInt(10)

private fun String.asMnemonic() = Mnemonic.entries.first { it.value == this }

private fun parseInstruction(line: String): Instruction {
    val (mnemonicStr, rest) = line.split(' ', limit = 2).map(String::trim)
    val parts = rest.split(',').map(String::trim)

    return Instruction(mnemonic = mnemonicStr.asMnemonic(), register = parts.register, offset = parts.offset)
}

private fun Program.run(registerFile: IntArray): IntArray {
    require(registerFile.size > 'b'.code) { "Register file is not sufficiently large" }
    var instructionPointer = 0

    while (instructionPointer in indices) {
        val instruction = this[instructionPointer]

        fun registerNum() = instruction.register!!.code

        when (instruction.mnemonic) {
            HLF -> registerFile[registerNum()] /= 2
            TPL -> registerFile[registerNum()] *= 3
            INC -> ++registerFile[registerNum()]
            JMP -> instructionPointer += instruction.offset!!
            JIE -> instructionPointer += if (registerFile[registerNum()] % 2 == 0) instruction.offset!! else 1
            JIO -> instructionPointer += if (registerFile[registerNum()] == 1) instruction.offset!! else 1
        }

        if (instruction.isFreeFlowing) ++instructionPointer
    }

    return registerFile
}

private fun part1(input: List<String>) = input.map(::parseInstruction).run(IntArray(128))['b'.code]

private fun part2(input: List<String>) =
    input.map(::parseInstruction).run(IntArray(128).also { it['a'.code] = 1 })['b'.code]

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(2, part1(testInput), "Part one (sample input)")
    assertEquals(2, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
