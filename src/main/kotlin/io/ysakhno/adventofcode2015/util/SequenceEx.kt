@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

package io.ysakhno.adventofcode2015.util

import java.math.BigDecimal
import java.math.BigInteger

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfInts") // to resolve "platform declaration clash"
fun Sequence<Int>.differences(): Sequence<Int> = zipWithNext { a, b -> b - a }

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfLongs") // to resolve "platform declaration clash"
fun Sequence<Long>.differences(): Sequence<Long> = zipWithNext { a, b -> b - a }

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfFloats") // to resolve "platform declaration clash"
fun Sequence<Float>.differences(): Sequence<Float> = zipWithNext { a, b -> b - a }

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfDoubles") // to resolve "platform declaration clash"
fun Sequence<Double>.differences(): Sequence<Double> = zipWithNext { a, b -> b - a }

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfBigIntegers") // to resolve "platform declaration clash"
fun Sequence<BigInteger>.differences(): Sequence<BigInteger> = zipWithNext { a, b -> b - a }

/** Returns a sequence of differences between every two adjacent elements of this sequence. */
@JvmName("differencesOfBigDecimals") // to resolve "platform declaration clash"
fun Sequence<BigDecimal>.differences(): Sequence<BigDecimal> = zipWithNext { a, b -> b - a }
