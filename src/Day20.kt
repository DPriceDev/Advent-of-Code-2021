
typealias Grid<T> = List<Row<T>>
typealias Row<T> = List<T>
data class Parsed(val indices: List<Int>, val grid: Grid<Int>)

fun main() {

    fun Grid<Int>.expand(value: Int = 0) : Grid<Int> {
        val horizontalExpand: Grid<Int> = map { listOf(value).plus(it).plus(value) }
        val emptyRow: Row<Int> = horizontalExpand.first().map { value }
        return listOf(emptyRow).plus(horizontalExpand).plusElement(emptyRow)
    }

    fun Grid<Int>.convolution(indices: List<Int>, value: Int = 0) : Grid<Int> {
        val outputGrid = map { it.map { 0 }.toMutableList() }

        (0..lastIndex).forEach { columnIndex ->
            (0..first().lastIndex).forEach { rowIndex ->
                val indexString = (-1 .. 1).fold("") { string, columnOffset ->
                    string + (-1 .. 1).fold("") { rowString, rowOffset ->
                        rowString + (getGridOrNull(rowIndex + rowOffset, columnIndex + columnOffset) ?: value).toString()
                    }
                }
                outputGrid[columnIndex][rowIndex] = indices[indexString.toInt(radix = 2)]
            }
        }
        return outputGrid
    }

    // Part 1 + 2
    fun Parsed.enhance(steps: Int): Int {
        val expandedGrid = (0 until steps).fold(grid) { expandedGrid, _ -> expandedGrid.expand() }
        return (0 until steps)
            .fold(expandedGrid) { convolution, index -> convolution.convolution(indices, index % 2) }
            .sumOf { it.count { it == 1 } }
    }

    // Parse
    fun List<String>.parse() : Parsed {
        val indices = first().toList().map { if(it == '#') 1 else 0 }
        val grid = drop(2).map { line -> line.toList().map { if(it == '#') 1 else 0 } }
        return Parsed(indices, grid)
    }

    // Output
    val input = readInput("Day20").parse()

    val (time1, answer1) = measureTimeWithAnswer { input.enhance(2) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { input.enhance(50) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}