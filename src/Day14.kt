fun main() {

    // Part 1
    fun part1(input: List<String>): Int = input.size

    // Part 2
    fun part2(input: List<String>): Int = input.size

    // Parse Input

    // Output
    val input = readInput("Day14")

    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = \n$answer1\ntaking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = \n$answer2\ntaking $time2 milliseconds")
}