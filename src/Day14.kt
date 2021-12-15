fun main() {
    data class Operation(val pair: String, val firstPair: String, val secondPair: String, val insertedChar: Char)

    fun String.operate(operations: List<Operation>, steps: Int): Long {
        val characterCounts = groupBy { it }.mapValues { it.value.count().toLong() }.toMutableMap()
        val initialPairCounts = windowed(2).groupBy { it }.mapValues { it.value.count().toLong() }

        (0 until steps).fold(initialPairCounts) { pairCounts, _ ->
            operations.fold(pairCounts.toMutableMap()) { updatedPairCounts, (pair, firstPair, secondPair, insertedChar) ->
                pairCounts[pair]?.takeIf { it != 0L }?.let { count ->
                    updatedPairCounts[firstPair] = updatedPairCounts[firstPair]?.plus(count) ?: count
                    updatedPairCounts[secondPair] = updatedPairCounts[secondPair]?.plus(count) ?: count
                    updatedPairCounts[pair] = updatedPairCounts[pair]?.minus(count) ?: 0
                    characterCounts[insertedChar] = characterCounts[insertedChar]?.plus(count) ?: count
                }
                updatedPairCounts
            }
        }
        return characterCounts.values.maxOf { it } - characterCounts.values.minOf { it }
    }

    // Part 1
    fun part1(start: String, operations: List<Operation>): Long = start.operate(operations, 10)

    // Part 2
    fun part2(start: String, operations: List<Operation>): Long = start.operate(operations, 40)

    // Parse Input
    fun List<String>.parse(): Pair<String, List<Operation>> {
        return first() to subList(2, size).map {
            val (group, insert) = """(\w+) -> (\w+)""".toRegex().find(it)?.destructured ?: error("operation not found")
            Operation(group, "${group[0]}${insert.first()}", "${insert.first()}${group[1]}", insert.first())
        }
    }

    // Output
    val input = readInput("Day14").parse()

    val (time1, answer1) = measureTimeWithAnswer { part1(input.first, input.second) }
    println("part 1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input.first, input.second) }
    println("part 2 taking $time2 milliseconds")
}