fun main() {

    // Part 1
    fun part1(input: List<String>) = input.size

    // Part 2
    fun part2(input: List<String>) = input.size

    // Output
    val input = readInput("Day19")

    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}