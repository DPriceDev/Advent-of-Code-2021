fun main() {

    // Part 1
    fun part1(input: List<String>): Int {
        val points = input.map { row -> row.toCharArray().map { it.digitToInt() }.toList() }

        return (0..points.size).fold(0) { total, columnIndex ->
            (0..points.first().size).fold(total) { rowTotal, rowIndex ->
                val top = points.getOrElse(columnIndex - 1) { listOf(9) }.getOrElse(rowIndex) { 9 }
                val bot = points.getOrElse(columnIndex + 1) { listOf(9) }.getOrElse(rowIndex) { 9 }
                val left = points.getOrElse(columnIndex) { listOf(9) }.getOrElse(rowIndex - 1) { 9 }
                val right = points.getOrElse(columnIndex) { listOf(9) }.getOrElse(rowIndex + 1) { 9 }

                points.getOrElse(columnIndex) { listOf(9) }.getOrElse(rowIndex) { 9 }
                    .takeIf { it < top && it < bot && it < left && it < right }
                    ?.let { rowTotal + it + 1 }
                    ?: rowTotal
            }
        }
    }

    // Part 2
    data class Basin(val height: Int, val group: Int = -1)

    fun check(
        group: Int,
        points: List<List<Basin>>,
        columnIndex: Int,
        rowIndex: Int
    ): List<List<Basin>> = points
        .mapTo(mutableListOf()) { it.toMutableList() }
        .let { it to points.getOrElse(columnIndex) { return points }.getOrElse(rowIndex) { return points } }
        .takeIf { (_, current) -> current.group == -1 && current.height != 9 }
        ?.let { (workingPoints, current) ->
            workingPoints.apply { this[columnIndex][rowIndex] = current.copy(group = group) }
        }
        ?.let { check(group, it, columnIndex - 1, rowIndex) }
        ?.let { check(group, it, columnIndex + 1, rowIndex) }
        ?.let { check(group, it, columnIndex, rowIndex - 1) }
        ?.let { check(group, it, columnIndex, rowIndex + 1) }
        ?: points

    fun part2(input: List<String>): Int {
        val points = input.map { row -> row.toCharArray().map { Basin(it.digitToInt()) } }
        return (0..points.lastIndex)
            .fold(points) { currentPoints, columnIndex ->
                (0..currentPoints.first().lastIndex).fold(currentPoints) { currentPoints, rowIndex ->
                    check(
                        (columnIndex * currentPoints.first().lastIndex) + rowIndex,
                        currentPoints,
                        columnIndex,
                        rowIndex
                    )
                }
            }
            .flatten()
            .filter { it.group != -1 }
            .groupBy { it.group }
            .values
            .map { it.count() }
            .sorted()
            .takeLast(3)
            .let { (a, b, c) -> a * b * c }
    }

    // Output
    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}