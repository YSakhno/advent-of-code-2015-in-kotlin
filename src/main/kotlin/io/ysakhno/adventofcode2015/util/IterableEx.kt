@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

package io.ysakhno.adventofcode2015.util

import java.math.BigDecimal
import java.math.BigInteger

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfInts") // to resolve "platform declaration clash"
fun Iterable<Int>.differences(): List<Int> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfLongs") // to resolve "platform declaration clash"
fun Iterable<Long>.differences(): List<Long> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfFloats") // to resolve "platform declaration clash"
fun Iterable<Float>.differences(): List<Float> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfDoubles") // to resolve "platform declaration clash"
fun Iterable<Double>.differences(): List<Double> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfBigIntegers") // to resolve "platform declaration clash"
fun Iterable<BigInteger>.differences(): List<BigInteger> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfBigDecimals") // to resolve "platform declaration clash"
fun Iterable<BigDecimal>.differences(): List<BigDecimal> = zipWithNext { a, b -> b - a }

/**
 * Transposes the two-dimensional structure represented by this iterable of lists.
 *
 * Each List in the input iterable represents a row of a two-dimensional structure, and this function transposes the
 * rows and columns, creating a new two-dimensional list where the columns become rows and vice versa.  Transposition is
 * performed such that elements at each position of each list are combined into a single list that is placed at a
 * corresponding position in the resulting list.
 */
fun <E> Iterable<List<E>>.transpose(): List<List<E>> = firstOrNull()?.indices?.map { i -> map { it[i] } } ?: emptyList()

/**
 * Returns a bordered version of this list of lists using specified value as [border].  The input list may be empty, in
 * which case the result is 2x2 grid of just the border itself.
 *
 * This function is useful when the list of lists represents a grid (or a map) of some sort, and traversing (moving
 * about) the grid is necessary.  Bordering the original grid with some 'impassable' border element allows for easier
 * navigation without the need for bounds checking.
 */
fun <E> List<List<E>>.bordered(border: E): List<List<E>> = listOf(List( (firstOrNull()?.size ?: 0) + 2) { border })
    .let { extraRow -> extraRow + map { listOf(border) + it + listOf(border) } + extraRow }
