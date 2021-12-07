import kotlin.math.abs

fun main() {

    // Part 1
    fun part1(input: String) : Int {
        val positions = input
            .split(',')
            .map { it.toInt() }

        val max = positions.maxOf { it }
        val min = positions.minOf { it }

        val distanceMaps = (min..max).mapIndexed { index, target ->
            target to positions.map { position ->
                abs(position - target)
            }
        }

        val distances = distanceMaps.map { it.first to it.second.sum() }
        val minimumDistance = distances.reduce { acc, i ->
            if(acc.second < i.second) acc else i
        }

        return minimumDistance.second
    }


    // Part 2
    fun part2(input: String) : Int {
        val positions = input
            .split(',')
            .map { it.toInt() }

        val max = positions.maxOf { it }
        val min = positions.minOf { it }

        val distanceMaps = (min..max).map { target ->
            target to positions.map { position -> abs(position - target) }
        }

        val distances = distanceMaps.map { (target, positions) ->
            target to positions.sumOf { position ->
                position * (position + 1) / 2
            }
        }

        val minimumDistance = distances.reduce { acc, i ->
            if(acc.second < i.second) acc else i
        }

        return minimumDistance.second
    }

    // Output
    val input = readInput("Day07").first()
    println(part1(input))
    println(part2(input))
}