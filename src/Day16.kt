enum class Operation(val typeId: Int) {
    SUM(0),
    PRODUCT(1),
    MINIMUM(2),
    MAXIMUM(3),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUAL_TO(7);
}

sealed class Data {
    data class Literal(val value: Long) : Data()
    data class Operator(val operation: Operation, val length: Int, val headers: List<Header>) : Data()
}

data class Header(val version: Int, val type: Int, val data: Data)

fun main() {
    fun String.loop(iterations: Int = Int.MAX_VALUE) : Pair<List<Header>, Int> {
        if (iterations == 0 || isEmpty() || all { it == '0' }) {
            return listOf<Header>() to 0
        }

        val version = take(3).toInt(radix = 2)
        val type = drop(3).take(3).toInt(radix = 2)

        val (header, dropped) = when(type) {
            4 -> {
                var literal = ""
                var shouldContinue = true
                var run = drop(6)
                var dropped = 6
                var index = 0
                while(shouldContinue) {
                    val current = run.take(5)
                    literal = literal.plus(current.takeLast(4))
                    run = run.drop(5)
                    dropped += 5
                    shouldContinue = current.first() == '1'
                    index++
                }
                Header(version, type, Data.Literal(literal.toLong(radix = 2))) to dropped
            }
            else -> {
                val typeId = drop(6).take(1).toInt(radix = 2)
                val lengthBits = drop(7).take(if(typeId == 0) 15 else 11)
                val length = lengthBits.toInt(radix = 2)

                val (response, dropped) = drop(7).drop(if(typeId == 0) 15 else 11)
                    .let { if(typeId == 0) it.take(length) else it }
                    .loop((if(typeId == 0) Int.MAX_VALUE else length))

                val operation = Operation.values().first { it.typeId == type }
                Header(version, type, Data.Operator(operation, length, response)) to 7 + (if(typeId == 0) 15 else 11) + dropped
            }
        }

        return if(iterations - 1 == 0 || drop(dropped).isEmpty() || drop(dropped).all { it == '0' }) {
            listOf(header) to dropped
        } else {
            val (subResponse, subDropped) = drop(dropped).loop(iterations = iterations - 1)
             listOf(header).plus(subResponse) to (dropped + subDropped)
        }
    }

    fun Header.sumVersion() : Int {
        return version + if(data is Data.Operator) data.headers.sumOf { it.sumVersion() } else 0
    }

    // Part 1
    fun part1(input: String): Int {
        val binaryNumbers = input.toList()
            .map { it.digitToInt(16).toString(2) }
            .map { (3 downTo it.length).fold(it) { run, _ -> "0$run"  } }
            .joinToString("") { it }

        val (headers, _) = binaryNumbers.loop()

        return headers.sumOf { it.sumVersion() }
    }

    // 1038

    // Part 2
    fun Header.calculate() : Long {
        return when(data) {
            is Data.Literal -> data.value
            is Data.Operator -> when(data.operation) {
                Operation.SUM -> data.headers.sumOf { it.calculate() }
                Operation.PRODUCT -> data.headers.fold(1L) { mult, current -> mult * current.calculate() }
                Operation.MINIMUM -> data.headers.minOf { it.calculate() }
                Operation.MAXIMUM -> data.headers.maxOf { it.calculate() }
                Operation.GREATER_THAN -> if((data.headers[0]).calculate() > (data.headers[1]).calculate()) 1 else 0
                Operation.LESS_THAN -> if((data.headers[0]).calculate() < (data.headers[1]).calculate()) 1 else 0
                Operation.EQUAL_TO -> if((data.headers[0]).calculate() == (data.headers[1]).calculate()) 1 else 0
            }
        }
    }

    fun part2(input: String): Long {
        val binaryNumbers = input.toList()
            .map { it.digitToInt(16).toString(2) }
            .map { (3 downTo it.length).fold(it) { run, _ -> "0$run"  } }
            .joinToString("") { it }

        val (headers, _) = binaryNumbers.loop()
        return headers.first().calculate()
    }

    // Output
    val input = readInput("Day16").first()

    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}