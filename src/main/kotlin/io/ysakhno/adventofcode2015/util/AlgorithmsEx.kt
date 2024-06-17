@file:Suppress("unused")

package io.ysakhno.adventofcode2015.util

/**
 * Searches this list or its sub-range for an element for which the given [comparison] function returns zero using the
 * binary search algorithm.  Unlike the [binarySearch] function from Kotlin Standard Library, this function (and
 * consequently, the `comparison` lambda) is inlineable.
 *
 * The list is expected to be sorted so that the signs of the [comparison] function's return values ascend on the list
 * elements, i.e. negative values come before zero and zeroes come before positive values.  Otherwise, the result is
 * undefined.
 *
 * If the list contains multiple elements for which [comparison] returns zero, there is no guarantee, which one will be
 * found.
 *
 * @param comparison function that returns zero when called on the list element being searched. On the elements coming
 * before the target element, the function must return negative values; on the elements coming after the target element,
 * the function must return positive values.
 * @return the index of the found element, if it is contained in the list within the specified range; otherwise, the
 * inverted insertion point `(-insertion point - 1)`. The _insertion point_ is defined as the index at which the element
 * should be inserted, so that the list (or the specified subrange of list) still remains sorted.
 */
inline fun <E> List<E>.inlinableBinarySearch(fromIndex: Int = 0, toIndex: Int = size, comparison: (E) -> Int): Int {
    checkBoundsStartEnd(fromIndex, toIndex, size)

    var low = fromIndex
    var high = toIndex - 1

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = comparison(midVal)

        if (cmp < 0) low = mid + 1
        else if (cmp > 0) high = mid - 1
        else return mid // key found
    }
    return -(low + 1) // key not found
}

/**
 * Searches this list or its sub-range for an element having the key returned by the specified [selector] function equal
 * to the provided [key] value using the binary search algorithm.  Unlike the [binarySearchBy] function from Kotlin
 * Standard Library, this function (and consequently, the `selector` lambda) is inlineable.
 *
 * The list is expected to be sorted into ascending order according to the Comparable natural ordering of keys of its
 * elements (the values returned by [selector]).  Otherwise, the result is undefined.
 *
 * If the list contains multiple elements with the specified [key], there is no guarantee, which one will be found.
 *
 * @return the index of the element with the specified [key], if it is contained in the list within the specified range;
 * otherwise, the inverted insertion point `(-insertion point - 1)`. The insertion point is defined as the index at
 * which the element should be inserted, so that the list (or the specified subrange of list) still remains sorted.
 */
inline fun <E, K : Comparable<K>> List<E>.inlinableBinarySearchBy(
    key: K,
    fromIndex: Int = 0,
    toIndex: Int = size,
    selector: (E) -> K,
): Int = inlinableBinarySearch(fromIndex, toIndex) { selector(it).compareTo(key) }

/** Returns a value _x_ such that _[f] (x)_ is `true`.  Based on the values of [f] at [lo] and [hi]. */
inline fun bisect(lo: Double = 0.0, hi: Double? = null, eps: Double = 1e-9, f: (Double) -> Boolean): Double {
    val boolOfLo = f(lo)
    var high = hi ?: run {
        var offset = 1.0
        while (f(lo + offset) == boolOfLo) {
            offset += offset * 0.5
        }
        lo + offset
    }

    require(f(high) != boolOfLo) { "f(lo) must not be equal to f(hi) (hi = $high)" }

    var low = lo

    while (high - low > eps) {
        val mid = (high + low) / 2
        if (f(mid) == boolOfLo) {
            low = mid
        } else {
            high = mid
        }
    }

    return if (boolOfLo) low else high
}

/**
 * Checks [start] and [end] indices against `0` and [size] bounds.
 *
 * @throws [IndexOutOfBoundsException] if [start] is negative, or [end] is greater than [size].
 * @throws [IllegalArgumentException] if [start] is greater than [end].
 */
fun checkBoundsStartEnd(start: Int, end: Int, size: Int): Unit = when {
    start > end -> throw IllegalArgumentException("Start index ($start) must not be greater than end index ($end).")
    start < 0 -> throw IndexOutOfBoundsException("Start index ($start) must be greater than or equal to zero.")
    end > size -> throw IndexOutOfBoundsException("End index ($end) must not be greater than size ($size).")
    else -> Unit
}
