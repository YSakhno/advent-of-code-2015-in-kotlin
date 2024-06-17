@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

package io.ysakhno.adventofcode2015.util

import java.math.BigInteger

/** The cleaner shorthand for printing output. */
fun <T> T.println() = also { println(this) }

/** A regex to find integer numbers in a string of text. */
private val INTEGER_NUMBER_REGEX = "[-+]?\\b[0-9]+\\b".toRegex()

/** A regex to find real numbers in a string of text. */
private val REAL_NUMBER_REGEX = "[-+]?\\b[0-9]+(?:\\.[0-9]+\\b)?".toRegex()

/** A regex to find words in a string of text. */
private val WORD_REGEX = "\\b[A-Za-z]+\\b".toRegex()

/** Finds all integer numbers in this string and returns them as a sequence of `Int`s. */
fun String.allInts(): Sequence<Int> = INTEGER_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toInt)

/** Finds all integer numbers in this string and returns them as a sequence of `Long`s. */
fun String.allLongs(): Sequence<Long> = INTEGER_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toLong)

/**
 * Finds all real numbers in this string and returns them as a sequence of `Float`s.
 *
 * Note that this function does not handle numbers with scientific notation.  For example, `"1e-3"` will not be
 * recognized as a valid number.
 */
fun String.allFloats(): Sequence<Float> = REAL_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toFloat)

/**
 * Finds all real numbers in this string and returns them as a sequence of `Double`s.
 *
 * Note that this function does not handle numbers with scientific notation.  For example, `"1e-3"` will not be
 * recognized as a valid number.
 */
fun String.allDoubles(): Sequence<Double> =
    REAL_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toDouble)

/**
 * Finds all words (character sequences consisting of only Latin letters) in this string and returns them as a sequence
 * of `String`s.
 *
 * Note that this function does not handle numbers.  For example, none of the `"0"`, `"42"`, `"3.14"` will be recognized
 * as a valid word.  Also, this function does not handle words with apostrophes.  For example, `"don't"` will not be
 * recognized as a valid word.
 */
fun String.allWords(): Sequence<String> = WORD_REGEX.findAll(this).map(MatchResult::value)

/** Calculates the least common multiple (LCM) of this [BigInteger] and the given `BigInteger` [value]. */
fun BigInteger.lcm(value: BigInteger): BigInteger = this * value / gcd(value)

/** Calculates the least common multiple (LCM) of the given [values]. */
fun lcm(vararg values: BigInteger): BigInteger = values.reduce { acc, value -> acc.lcm(value) }

/** Calculates the least common multiple (LCM) of the given [values]. */
fun lcm(values: Iterable<BigInteger>): BigInteger = values.reduce { acc, value -> acc.lcm(value) }

/** Swaps the first and second elements of this pair creating a new pair with the elements swapped. */
fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first
