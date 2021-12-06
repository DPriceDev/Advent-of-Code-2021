fun main() {

    fun Map<Int, ULong>.calculate(lifetime: Int): ULong = (0 until lifetime)
        .fold(this) { map, _ ->
            map.toList().fold(mutableMapOf()) { newMap, (key, value) ->
                when (key) {
                    0 -> {
                        newMap[6] = newMap.getOrDefault(6, 0u) + value
                        newMap[8] = value
                    }
                    7 -> newMap[key - 1] = newMap.getOrDefault(key - 1, 0u) + value
                    else -> newMap[key - 1] = value
                }
                newMap
            }
        }
        .toList()
        .sumOf { it.second }

    // Part 1
    fun part1(input: List<String>) = input
            .first()
            .split(',')
            .map { it.toInt() }
            .groupBy { it }
            .mapValues { it.value.count().toULong() }
            .calculate(80)

    // Part 2
    fun part2(input: List<String>) = input
        .first()
        .split(',')
        .map { it.toInt() }
        .groupBy { it }
        .mapValues { it.value.count().toULong() }
        .calculate(256)

    // Output
    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}