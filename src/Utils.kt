import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.measureTimeMillis

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun String.toInt(default: Int = 0) = this.toIntOrNull() ?: default

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) : Pair<Int, Int> = (this.first + other.first) to (this.second + other.second)

fun <T> Iterable<Iterable<T>>.forEachInGrid(action: (T) -> Unit) = forEach { row -> row.forEach { item -> action(item) } }

fun <T> Iterable<Iterable<T>>.forEachInGridIndexed(action: (column: Int, row: Int, item: T) -> Unit) = forEachIndexed { rowIndex, row ->
    row.forEachIndexed { columnIndex, item ->
        action(columnIndex, rowIndex, item)
    }
}

fun <T, R> Iterable<Iterable<T>>.mapGrid(transform: (T) -> R) : List<List<R>> = map { row -> row.map { item -> transform(item) } }

fun <T, R> Iterable<Iterable<T>>.mapGridIndexed(transform: (column: Int, row: Int, item: T) -> R) : List<List<R>> = mapIndexed { rowIndex, row ->
    row.mapIndexed { columnIndex, item -> transform(columnIndex, rowIndex, item) }
}

fun <T> List<List<T>>.subGrid(columnRange: IntRange, rowRange: IntRange) : List<List<T>> {
    return map { row ->
            row.subList(
            columnRange.first.coerceAtLeast(0),
            (columnRange.last + 1).coerceAtMost(row.size)
        )
    }.subList(
        rowRange.first.coerceAtLeast(0),
        (rowRange.last + 1).coerceAtMost(size)
    )
}

fun IntRange.coerceIn(min: Int, max: Int) : IntRange = first.coerceAtLeast(min)..last.coerceAtMost(max)

val String.isLowercase
    get() = all { ('a'..'z').contains(it) }

fun <T> measureTimeWithAnswer(action: () -> T) : Pair<Long, T> {
    val answer: T
    val milliseconds = measureTimeMillis { answer = action() }
    return milliseconds to answer
}

fun <T> List<List<T>>.getGridOrNull(x: Int, y: Int) = getOrNull(y)?.getOrNull(x)

fun <T> List<List<T>>.getGrid(x: Int, y: Int) = get(y).get(x)