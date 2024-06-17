package io.ysakhno.adventofcode2015.util

import kotlin.io.path.readLines
import kotlin.io.path.toPath

/** Marker interface for custom ad-hoc objects defined in the package of the particular day's solution. */
interface ProblemInput {

    /**
     * Determines the path to input resource files for a particular day from this object.
     *
     * For file named `Day12.kt`, this property will return the path to all resource files for day 12.
     *
     * @receiver the object that must be defined in the target source file, whose base filename should be determined.
     */
    private val dayFilePath: String get() = javaClass.`package`.name.replace('.', '/')

    /**
     * Reads lines from the main input file for the given day.
     *
     * When called on an object defined in a package related to day 12 (for example), this function will return lines
     * read from the file located in `"<full-path-for-day-12>/input.txt"`.
     *
     * @receiver the object that must be defined in the target source file that defines the location of input files.
     * Location is determined by the source package of the object in the receiver.  If the top-level object is defined
     * like so:
     *
     * ```kotlin
     * val problemInput = object : ProblemInput {}
     * ```
     * then the main input can be read through the expression `problemInput.read()` anywhere in that source file.
     */
    fun read(): List<String> = readInput("$dayFilePath/input.txt")

    /**
     * Reads lines from the test input file (i.e. some sample input usually given in the description) for the given day.
     *
     * When called on an object defined in a package related to day 12 (for example), this function will return lines
     * read from the file located in `"<full-path-for-day-12>/test.txt"`.
     *
     * @receiver the object that must be defined in the target source file that defines the location of input files.
     * Location is determined by the source package of the object in the receiver.  If the top-level object is defined
     * like so:
     *
     * ```kotlin
     * val problemInput = object : ProblemInput {}
     * ```
     * then the test input can be read through the expression `problemInput.readTest()` anywhere in that source file.
     * @param number the number of the test input file to read (if several input files need to be supplied for the
     * particular day).  This number will then be used as a suffix in the file name, for example `test-5.txt`.  If set
     * to `0` (default), then no suffix will be added, and the input file is supposed to be named just `text.txt`.
     */
    fun readTest(number: Int = 0): List<String> =
        readInput(if (number == 0) "$dayFilePath/test.txt" else "$dayFilePath/test-$number.txt")
}

/** Reads lines from the specified resource txt file. */
fun readInput(filename: String): List<String> = checkNotNull(object {}.javaClass.classLoader.getResource(filename)) {
    "Resource $filename cannot be found or accessed."
}.toURI().toPath().readLines().filter(String::isNotBlank)
