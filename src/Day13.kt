data class Coordinate(val x: Int, val y: Int)
sealed class Fold {
    data class X(val position: Int) : Fold()
    data class Y(val position: Int) : Fold()
}

fun main() {

    // Part 1
    fun part1(coordinates: List<Coordinate>, folds: List<Fold>): Int = folds.first().let { fold ->
        coordinates.map { coordinate ->
            when {
                fold is Fold.Y && coordinate.y > fold.position -> coordinate.copy(y = fold.position - (coordinate.y - fold.position))
                fold is Fold.X && coordinate.x > fold.position -> coordinate.copy(x = fold.position - (coordinate.x - fold.position))
                else -> coordinate
            }
        }.toSet().size
    }

    // Part 2
    fun part2(coordinates: List<Coordinate>, folds: List<Fold>): String {
        val result = folds.fold(coordinates.toSet()) { running, fold ->
            running.map { coordinate ->
                when {
                    fold is Fold.Y && coordinate.y > fold.position -> coordinate.copy(y = fold.position - (coordinate.y - fold.position))
                    fold is Fold.X && coordinate.x > fold.position -> coordinate.copy(x = fold.position - (coordinate.x - fold.position))
                    else -> coordinate
                }
            }.toSet()
        }

        return arrayOfNulls<Array<Char>>(result.maxOf { it.y } + 1)
            .map { _ -> (0..result.maxOf { it.x }).map { '.' }.toMutableList() }
            .apply { result.forEach { coordinate -> this[coordinate.y][coordinate.x] = '#' } }
            .joinToString("\n") { row -> row.joinToString("") { character -> character.toString() } }
    }

    // Parse Input
    fun List<String>.toCoordinatesAndFolds(): Pair<List<Coordinate>, List<Fold>> {
        val coordinates = subList(0, indexOf("")).map { coordinateLine ->
            val (x, y) = """(\d+),(\d+)""".toRegex().find(coordinateLine)?.destructured ?: error("Coordinate not found")
            Coordinate(x.toInt(), y.toInt())
        }

        val folds = subList(indexOf("") + 1, size).map { foldLine ->
            val (axis, position) = """fold along (\w+)=(\d+)""".toRegex().find(foldLine)?.destructured ?: error("fold not found")
            if (axis == "y") Fold.Y(position.toInt()) else Fold.X(position.toInt())
        }

        return coordinates to folds
    }

    // Output
    val input = readInput("Day13").toCoordinatesAndFolds()

    val (time1, answer1) = measureTimeWithAnswer { part1(input.first, input.second) }
    println("part 1 answer = \n$answer1\ntaking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input.first, input.second) }
    println("part 2 answer = \n$answer2\ntaking $time2 milliseconds")
}