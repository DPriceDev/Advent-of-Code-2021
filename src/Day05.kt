fun main() {
    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()

    data class Coordinate(val x: Int, val y: Int)
    data class Line(val start: Coordinate, val end: Coordinate)
    data class Point(var value: Int)

    infix operator fun Coordinate.rangeTo(other: Coordinate): List<Coordinate> {
        val y = when {
            this.y <= other.y -> (this.y..other.y).toList()
            else -> (this.y downTo other.y).toList()
        }
        val x = when {
            this.x <= other.x -> (this.x..other.x).toList()
            else -> (this.x downTo other.x).toList()
        }
        val xCoordinates = if (x.size == 1) y.map { x.first() } else x
        val yCoordinates = if (y.size == 1) x.map { y.first() } else y
        return xCoordinates.zip(yCoordinates).map { (x, y) -> Coordinate(x, y) }
    }

    // Part 1
    fun part1(input: List<String>) = input
        .asSequence()
        .map { line -> regex.find(line)?.destructured?.toList()?.map { it.toInt() } ?: error("Invalid coords") }
        .map { (x1, y1, x2, y2) -> Line(Coordinate(x1, y1), Coordinate(x2, y2)) }
        .filter { it.start.x == it.end.x || it.start.y == it.end.y }
        .fold(mutableMapOf<Int, MutableMap<Int, Point>>()) { map, (start, end) ->
            (start..end).fold(map) { map, (x, y) ->
                map.apply { getOrPut(y) { mutableMapOf() }.getOrPut(x) { Point(0) }.value += 1 }
            }
        }
        .map { pointMap -> pointMap.value.values.count { it.value > 1 } }
        .sum()


    // Part 2
    fun part2(input: List<String>) = input
        .asSequence()
        .map { line -> regex.find(line)?.destructured?.toList()?.map { it.toInt() } ?: error("Invalid coords") }
        .map { (x1, y1, x2, y2) -> Line(Coordinate(x1, y1), Coordinate(x2, y2)) }
        .fold(mutableMapOf<Int, MutableMap<Int, Point>>()) { map, (start, end) ->
            (start..end).fold(map) { map, (x, y) ->
                map.apply { getOrPut(y) { mutableMapOf() }.getOrPut(x) { Point(0) }.value += 1 }
            }
        }
        .map { pointMap -> pointMap.value.values.count { it.value > 1 } }
        .sum()

    // Output
    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}