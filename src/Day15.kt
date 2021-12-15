fun main() {
    data class Point(val score: Int, var distance: Int = Int.MAX_VALUE, var visited: Boolean = false)
    data class Coordinate(val x: Int, val y: Int)
    data class QueueItem(val coordinate: Coordinate, val point: Point)

    fun <T> List<List<T>>.getGrid(coordinate: Coordinate) = getGrid(coordinate.y, coordinate.x)

    fun Point.visit(queue: MutableSet<QueueItem>, currentPoint: Point, coordinate: Coordinate) {
        val temp = currentPoint.distance + score
        if (distance > temp) {
            distance = temp
        }
        if (!visited) {
            queue.add(QueueItem(coordinate, this))
        }
    }

    fun List<List<Point>>.dijkstra(
        start: Coordinate = Coordinate(0, 0),
        end: Coordinate = Coordinate(lastIndex, first().lastIndex)
    ): Int {
        getGrid(start).visited = true
        getGrid(start).distance = 0
        val queue: MutableSet<QueueItem> = mutableSetOf(QueueItem(start, getGrid(start)))

        while (queue.isNotEmpty()) {
            val (coordinate, point) = queue.minByOrNull { it.point.distance } ?: error("no value in queue!")
            if (coordinate == end) {
                return point.distance
            }

            getGridOrNull(coordinate.y - 1, coordinate.x)?.visit(queue, point, coordinate.copy(y = coordinate.y - 1))
            getGridOrNull(coordinate.y + 1, coordinate.x)?.visit(queue, point, coordinate.copy(y = coordinate.y + 1))
            getGridOrNull(coordinate.y, coordinate.x - 1)?.visit(queue, point, coordinate.copy(x = coordinate.x - 1))
            getGridOrNull(coordinate.y, coordinate.x + 1)?.visit(queue, point, coordinate.copy(x = coordinate.x + 1))

            queue.removeIf { it.coordinate == coordinate }
            point.visited = true
        }
        error("No Path Found!")
    }

    // Part 1
    fun part1(grid: List<List<Point>>) = grid.map { it.toMutableList() }.dijkstra()

    // Part 2
    fun List<Point>.toIncrementedRow(index: Int) = map { point ->
        val score = point.score + index
        point.copy(score = if (score > 9) score - 9 else score)
    }.toMutableList()

    fun part2(grid: List<List<Point>>): Int {
        var gridC = grid.map { row ->
            row.plus(
                (1 until 5).map { index -> row.toIncrementedRow(index) }.flatten()
            )
        }

        gridC = gridC.plus(
            (1 until 5).map { index ->
                gridC.map { row -> row.toIncrementedRow(index) }
            }.flatten()
        )

        return gridC.dijkstra()
    }

    // Parse input
    fun List<String>.toGrid() = map { row -> row.toList().map { digit -> Point(digit.digitToInt()) } }

    // Output
    val input = readInput("Day15")

    val (time1, answer1) = measureTimeWithAnswer { part1(input.toGrid()) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input.toGrid()) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}