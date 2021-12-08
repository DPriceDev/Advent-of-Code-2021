

fun main() {
    val regex = """(\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) \| (\w+) (\w+) (\w+) (\w+)""".toRegex()


    // Part 1
    fun part1(input: List<String>) = input
        .map { regex.find(it)?.destructured?.toList() ?: error("Invalid Row") }
        .map { it.subList(10, 14) }.sumOf { digits ->
            digits.count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
        }

    // 476

    fun String.removeFrom(list: MutableList<String>) : String {
        list.remove(this)
        return this
    }

    // Part 2
    fun part2(input: List<String>) = input
        .map { regex.find(it)?.destructured?.toList() ?: error("Invalid Row") }
        .map { it.subList(0, 10).toMutableList() to it.subList(10, 14) }
        .sumOf { (inputDigits, outputDigits) ->
            val one = inputDigits.find { it.length == 2 }?.removeFrom(inputDigits)
            val seven = inputDigits.find { it.length == 3 }?.removeFrom(inputDigits)
            val four = inputDigits.find { it.length == 4 }?.removeFrom(inputDigits)
            val eight = inputDigits.find { it.length == 7 }?.removeFrom(inputDigits)

            val three = inputDigits.find { digit -> digit.length == 5 && one?.all { digit.contains(it) } == true }
            inputDigits.remove(three)
            val nine = inputDigits.find { digit -> digit.length == 6 && four?.all { digit.contains(it) } == true }
            inputDigits.remove(nine)
            val six = inputDigits.find { digit -> digit.length == 6 && seven?.count { digit.contains(it) } == 2 }
            inputDigits.remove(six)
            val zero = inputDigits.find { digit -> digit.length == 6 }
            inputDigits.remove(zero)
            val five = inputDigits.find { digit -> digit.length == 5 && six?.count { digit.contains(it) } == 5 }
            inputDigits.remove(five)

            val characters = listOf(
                "0" to zero,
                "1" to one,
                "2" to inputDigits.first(),
                "3" to three,
                "4" to four,
                "5" to five,
                "6" to six,
                "7" to seven,
                "8" to eight,
                "9" to nine
            )

            outputDigits
                .map { outputDigit ->
                    characters.find { (_, digit) -> digit?.toList()?.sorted() == outputDigit.toList().sorted() }?.first
                }
                .joinToString("")
                .toInt()
        }

    // Output
    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}