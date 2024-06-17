@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

package io.ysakhno.adventofcode2015.util

/**
 * Creates a memoized version the given [function] that takes one parameter.
 *
 * @param function the function to memoize.
 * @return a function that caches the results of the original [function].
 */
fun <T, U> memoize(function: (parameter: T) -> U): Lazy<(T) -> U> = lazy {
    val cache = mutableMapOf<T, U>()
    return@lazy { parameter -> cache.getOrPut(parameter) { function(parameter) } }
}

/**
 * Creates a memoized version of the given [function] that takes two parameters.
 *
 * @param function the function to memoize.
 * @return a function that caches the results of the original [function].
 */
fun <T1, T2, U> memoize(function: (parameter1: T1, parameter2: T2) -> U): Lazy<(T1, T2) -> U> = lazy {
    val cache = mutableMapOf<Pair<T1, T2>, U>()
    return@lazy { parameter1, parameter2 ->
        val key = parameter1 to parameter2
        cache.getOrPut(key) { function(parameter1, parameter2) }
    }
}

/**
 * Creates a memoized version the given [function] that takes three parameters.
 *
 * @param function the function to memoize.
 * @return a function that caches the results of the original [function].
 */
fun <T1, T2, T3, U> memoize(function: (parameter1: T1, parameter2: T2, parameter3: T3) -> U): Lazy<(T1, T2, T3) -> U> =
    lazy {
        val cache = mutableMapOf<Triple<T1, T2, T3>, U>()
        return@lazy { parameter1, parameter2, parameter3 ->
            val key = Triple(parameter1, parameter2, parameter3)
            cache.getOrPut(key) { function(parameter1, parameter2, parameter3) }
        }
    }
