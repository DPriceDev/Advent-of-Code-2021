fun main() {
    fun part1(input: List<String>) = input
        .map { it.toInt() }
        .windowed(2) { it.first() < it.last() }
        .sumOf { if(it) 1L else 0L }

    fun part2(input: List<String>) = input
        .map { it.toInt() }
        .windowed(3) { it.sum() }
        .windowed(2) { it.first() < it.last() }
        .sumOf { if(it) 1L else 0L }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
