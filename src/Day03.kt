fun main() {

    // Part 1
    fun part1(input: List<String>) = input
        .map { line -> line.toCharArray().map { it.digitToInt() } }
        .fold(listOf<Int>()) { counts, binaryIntegers ->
            binaryIntegers.mapIndexed { index, integer ->
                integer + (counts.getOrNull(index) ?: 0)
            }
        }
        .let { list ->
            val max = list.map { if(it > 500) 1 else 0 }.joinToString("") { it.toString() }.toInt(radix = 2)
            val min = list.map { if(it <= 500) 1 else 0 }.joinToString("") { it.toString() }.toInt(radix = 2)
            max * min
        }

    // Part 2
    data class CombinedRows(val oxygenRows: List<List<Int>>, val scrubberRows: List<List<Int>>)

    fun List<List<Int>>.calculate(index: Int, check: (Int, Int) -> Boolean) : List<List<Int>> {
        val zeros = size - count { it[index] == 1 }
        return filter { binaryIntegers -> binaryIntegers[index] == if(check(size - zeros, zeros)) 1 else 0 }
    }

    fun part2(input: List<String>) = input
        .map { line -> line.toCharArray().map { it.digitToInt() } }
        .let { lines -> Pair(0..lines.first().size, CombinedRows(lines, lines)) }
        .let { (indices, combinedRows) ->
            indices.fold(combinedRows) { running, index ->
                CombinedRows(
                    oxygenRows = running.oxygenRows
                        .takeIf { it.size > 1 }
                        ?.calculate(index) { ones, zeros -> ones >= zeros }
                        ?: running.oxygenRows,
                    scrubberRows = running.scrubberRows
                        .takeIf { it.size > 1 }
                        ?.calculate(index) { ones, zeros -> ones < zeros }
                        ?: running.scrubberRows
                )
            }
        }.let { (oxygenRows, scrubberRows) ->
            val oxygenRating = oxygenRows.first()
                .joinToString("") { it.toString() }
                .toInt(radix = 2)
            val scrubberRating = scrubberRows.first()
                .joinToString("") { it.toString() }
                .toInt(radix = 2)
            oxygenRating * scrubberRating
        }

    // Print
    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}