/*
 * --- Day 2: I Was Told There Would Be No Math ---
 *
 * You need to submit an order for more wrapping paper.  You have a list of the dimensions (length l, width w, and
 * height h) of each item, and only want to order exactly as much as you need.
 *
 * Every item is a box (a perfect right rectangular prism), which makes calculating the required wrapping paper for each
 * item a little easier: find the surface area of the box, which is 2*l*w + 2*w*h + 2*h*l.  You also need a little extra
 * paper for each item: the area of the smallest side.
 *
 * For example:
 *
 *   - An item with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper plus 6 square feet of
 *     slack, for a total of 58 square feet.
 *   - An item with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper plus 1 square foot
 *     of slack, for a total of 43 square feet.
 *
 * All numbers in the list are in feet. How many total square feet of wrapping paper should you order?
 *
 * --- Part Two ---
 *
 * You are also running low on ribbon.  Ribbon is all the same width, so you only have to worry about the length you
 * need to order, which again needs to be exact.
 *
 * The ribbon required to wrap an item is the shortest distance around its sides, or the smallest perimeter of any one
 * face.  Each item also requires a bow made out of ribbon as well; the feet of ribbon required for the perfect bow is
 * equal to the cubic feet of volume of the item.
 *
 * For example:
 *
 *   - An item with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the item plus 2*3*4 = 24 feet of
 *     ribbon for the bow, for a total of 34 feet.
 *   - An item with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the item plus 1*1*10 = 10 feet of
 *     ribbon for the bow, for a total of 14 feet.
 *
 * How many total feet of ribbon should you order?
 */
package io.ysakhno.adventofcode2015.day02

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.allInts
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Box(val length: Int, val width: Int, val height: Int) {
    val surfaceArea get() = 2 * length * width + 2 * width * height + 2 * height * length
    val volume get() = length * width * height
    val areaOfSmallestSide: Int
        get() {
            val side1 = length * width
            val side2 = width * height
            val side3 = height * length

            return minOf(side1, side2, side3)
        }
    val smallestPerimeter: Int
        get() {
            val perimeter1 = (length + width) * 2
            val perimeter2 = (width + height) * 2
            val perimeter3 = (height + length) * 2

            return minOf(perimeter1, perimeter2, perimeter3)
        }
    val wrappingPaper get() = surfaceArea + areaOfSmallestSide
    val ribbon get() = smallestPerimeter + volume
}

private fun String.parseBox() = replace('x', ' ').allInts().toList().let { (l, w, h) -> Box(l, w, h) }

private fun List<String>.toBoxes() = map(String::parseBox)

private fun part1(input: List<String>) = input.toBoxes().sumOf(Box::wrappingPaper)

private fun part2(input: List<String>) = input.toBoxes().sumOf(Box::ribbon)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(101, part1(testInput), "Part one (sample input)")
    assertEquals(48, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
