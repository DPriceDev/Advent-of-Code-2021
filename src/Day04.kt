fun main() {

    data class Board(var sum: Int = 0, var complete: Boolean = false)
    data class Row(var sum: Int = 0)
    data class Column(var sum: Int = 0)
    data class Number(val value: Int, val row: Row, val column: Column, val board: Board) {
        init {
            column.sum += value
            row.sum += value
            board.sum += value
        }

        val isComplete: Boolean
            get() = (column.sum == 0 || row.sum == 0) && !board.complete
    }

    // Part 1
    fun part1(input: List<String>): Int {
        val calls = input.first().split(",").map { it.toInt() }
        val initialBoards = input
            .subList(1, input.lastIndex)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { board ->
                board.map { row ->
                    row.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
                }
            }

        val numberMap = initialBoards.fold(mutableMapOf<Int, List<Number>>()) { numberMap, board ->
            val trackingBoard = Board()
            val columns = board.first().map { Column() }
            val rows = board.map { Row() }

            board.foldIndexed(numberMap) { rowIndex, _, row ->
                row.foldIndexed(numberMap) { columnIndex, _, number ->
                    val trackingNumber = Number(number, rows[rowIndex], columns[columnIndex], trackingBoard)
                    numberMap[number] = numberMap.getOrDefault(number, listOf()).plus(trackingNumber)
                    numberMap
                }
            }
        }

        calls.forEach { call ->
            numberMap[call]?.forEach { number ->
                number.column.sum -= call
                number.row.sum -= call
                number.board.sum -= call
                if (number.isComplete) {
                    return number.board.sum * call
                }
            }
        }

        error("No Board won :(")
    }

    // Part 2
    fun part2(input: List<String>): Int {
        val calls = input.first().split(",").map { it.toInt() }
        val initialBoards = input
            .subList(1, input.lastIndex)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { board ->
                board.map { row ->
                    row.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
                }
            }

        val numberMap = initialBoards.fold(mutableMapOf<Int, List<Number>>()) { numberMap, board ->
            val trackingBoard = Board()
            val columns = board.first().map { Column() }
            val rows = board.map { Row() }

            board.foldIndexed(numberMap) { rowIndex, _, row ->
                row.foldIndexed(numberMap) { columnIndex, _, number ->
                    val trackingNumber = Number(number, rows[rowIndex], columns[columnIndex], trackingBoard)
                    numberMap[number] = numberMap.getOrDefault(number, listOf()).plus(trackingNumber)
                    numberMap
                }
            }
        }

        var boardCount = initialBoards.size
        calls.forEach { call ->
            numberMap[call]?.forEach { number ->
                number.column.sum -= call
                number.row.sum -= call
                number.board.sum -= call

                if (number.isComplete) {
                    --boardCount
                    number.board.complete = true
                    if (boardCount == 0) {
                        return number.board.sum * call
                    }
                }
            }
        }

        error("No Board won :(")
    }

    // Results
    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}