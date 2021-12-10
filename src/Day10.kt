sealed class Result {
    data class Valid(val line: List<Char>, val append: String = "") : Result()
    data class Invalid(val character: Char) : Result()
}

enum class SyntaxPair(val opening: Char, val closing: Char, val score: Int, val closingScore: Long) {
    Parentheses('(', ')', 3, 1),
    Square('[', ']', 57, 2),
    Braces('{', '}', 1197, 3),
    Type('<', '>', 25137, 4)
}

fun main() {
    val syntaxPairs = SyntaxPair.values().toList()

    fun List<Char>.check(): Result {
        return if (isNotEmpty() && syntaxPairs.any { it.opening == first() }) {
            val closingSyntax = syntaxPairs.find { it.opening == first() }?.closing
            val result = subList(1, size).check()
            when {
                result is Result.Valid && result.line.isEmpty() -> result.copy(append = result.append + closingSyntax)
                result is Result.Valid && result.line.first() == closingSyntax -> result.line.subList(1, result.line.size).check()
                result is Result.Valid -> Result.Invalid(result.line.first())
                else -> result
            }
        } else {
            Result.Valid(this)
        }
    }

    // Part 1
    fun part1(input: List<String>) = input
        .map { it.toList().check() }
        .filterIsInstance<Result.Invalid>()
        .sumOf { result -> syntaxPairs.find { it.closing == result.character }?.score ?: error("No Score found") }

    // Part 2
    fun part2(input: List<String>) = input
        .map { it.toList().check() }
        .filterIsInstance<Result.Valid>()
        .map { result ->
            result.append.toList().fold(0L) { running, character ->
                (running * 5L) + (syntaxPairs.find { it.closing == character }?.closingScore ?: error("No Closing Score found"))
            }
        }
        .sorted()
        .let { it[it.lastIndex / 2] }


    // Output
    val input = readInput("Day10")

    println(part1(input))
    println(part2(input))
}