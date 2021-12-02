import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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