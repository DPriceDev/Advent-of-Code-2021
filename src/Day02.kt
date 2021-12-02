fun main() {
    val regex = """forward (\d)|down (\d)|up (\d)""".toRegex()

    fun part1(input: List<String>) = input.map {
        val (forward, up, down) = regex.find(it)?.destructured ?: error("Regex Failed")
        forward.toInt() to up.toInt() - down.toInt()
    }
        .reduce { current, pair -> current + pair }
        .let { it.first * it.second }

    fun part2(input: List<String>) = input.runningFold(0 to (0 to 0)) { current, next ->
        val (forward, down, up) = regex.find(next)?.destructured ?: error("Regex Failed")
        val aim = current.first + down.toInt() - up.toInt()
        aim to (forward.toInt() to aim * forward.toInt())
    }
        .fold(0 to 0) { current, pair -> current + pair.second }
        .let { it.first * it.second }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
