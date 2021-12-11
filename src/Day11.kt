fun main() {
    data class Octopus(var power: Int, var flashedOn: Int = 0)

    fun List<List<Octopus>>.checkGrid(
        step: Int,
        rowRange: IntRange = 0..first().lastIndex,
        columnRange: IntRange = 0..first().lastIndex
    ) : Int = subGrid(columnRange, rowRange).mapGridIndexed { columnIndex, rowIndex, octopus ->
        when {
            octopus.flashedOn == step -> 0
            octopus.power + 1 > 9 -> {
                octopus.power = 0
                octopus.flashedOn = step
                1 + checkGrid(
                    step = step,
                        columnRange = ((columnRange.first + columnIndex - 1)..(columnRange.first + columnIndex + 1)).coerceIn(0, first().lastIndex),
                        rowRange = ((rowRange.first + rowIndex - 1)..(rowRange.first + rowIndex + 1)).coerceIn(0, lastIndex)
                )
            }
            else -> 0.also { octopus.power += 1 }
        }
    }.sumOf { it.sum() }


    // Part 1
    fun part1(input: List<String>) = input
        .map { row -> row.toList().map { octopus -> Octopus(octopus.digitToInt()) }.toMutableList() }
        .let { grid -> (1..100).sumOf { step -> grid.checkGrid(step) } }

    // Part 2
    fun part2(input: List<String>) = input
        .map { row -> row.toList().map { octopus -> Octopus(octopus.digitToInt()) }.toMutableList() }
        .let { grid -> (1..Int.MAX_VALUE).find { step -> grid.checkGrid(step) == 100 } ?: error("not found") }

    // Output
    val input = readInput("Day11")

    println(part1(input))
    println(part2(input))
}