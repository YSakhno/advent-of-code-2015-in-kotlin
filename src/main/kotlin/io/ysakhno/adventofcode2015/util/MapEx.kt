@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

package io.ysakhno.adventofcode2015.util

/**
 * Transposes this map into a map of lists with values from this map becoming keys in the resulting map, having their
 * values being the corresponding keys from the original map.  If any key in the resulting map has two or more values,
 * they will be grouped together in one list under that corresponding key.
 */
@JvmName("transposeMap") // to resolve "platform declaration clash"
fun <K, V> Map<K, V>.transpose(): Map<V, List<K>> =
    entries.fold(mutableMapOf<V, MutableList<K>>()) { acc, (key, value) ->
        acc.apply { getOrPut(value, ::mutableListOf).add(key) }
    }

/**
 * Transposes this map of collections into a map of lists with values from this map becoming keys in the resulting map,
 * having their values being the corresponding keys from the original map.  If any key in the resulting map has two or
 * more values, they will be grouped together in one list under that corresponding key.  Keys from the original map that
 * had empty lists as values will not be mapped under any key in the resulting map (effectively, they will disappear).
 */
@JvmName("transposeMappedLists") // to resolve "platform declaration clash"
fun <K, V> Map<K, Collection<V>>.transpose(): Map<V, List<K>> =
    entries.fold(mutableMapOf<V, MutableList<K>>()) { acc, (key, values) ->
        values.forEach { value -> acc.getOrPut(value, ::mutableListOf).add(key) }
        acc
    }
