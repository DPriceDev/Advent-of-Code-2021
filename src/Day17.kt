import kotlin.math.abs

fun main() {
    data class TargetArea(val width: IntRange, val height: IntRange)
    data class Point(val x: Int, val y: Int)
    data class VelocityAtStep(val velocity: Int, val step: Int)
    val regex = """target area: x=(-*\d+)..(-*\d+), y=(-*\d+)..(-*\d+)""".toRegex()

    // Part 1
    fun part1(area: TargetArea) = area.let { abs(it.height.first) - 1 }.let { (it * (it + 1)) / 2 }

    fun Int.stepsToZero(starting: Int): Pair<Int, Boolean>? {
        var step = 0
        var running = 0

        while(this - running > 0 && starting - step > 0) {
            running += (starting - step)
            step++
        }

        return takeIf { it - running == 0 }?.let { step to (starting - step == 1) }
    }

    fun Int.stepDownBy(steps: Int) : Int = (0 until steps).fold(0) { running, step -> running + (this - step) }

    // Part 2
    fun part2(area: TargetArea): Int {
        val maxY = abs(area.height.first) - 1

        val xPoints = area.width.map { x ->
            val velocityStepPair = mutableListOf<VelocityAtStep>()
            (0 until x).forEach { startingVelocity ->
                x.stepsToZero(startingVelocity)?.let { (steps, hitZero) ->
                    velocityStepPair.add(VelocityAtStep(startingVelocity, steps))

                    if(hitZero) {
                        repeat(maxY - area.height.first) {
                            velocityStepPair.add(VelocityAtStep(startingVelocity, steps + it))
                        }
                    }
                }
            }
            velocityStepPair.plus(VelocityAtStep(x, 1))
        }

        val resultSet = mutableSetOf<Point>()
        xPoints.flatten().forEach { (velocityX, numberOfSteps) ->
            (area.height.first..maxY).forEach { velocityY ->
                resultSet.takeIf { velocityY.stepDownBy(numberOfSteps) in area.height }?.add(Point(velocityX, velocityY))
            }
        }

        return resultSet.size
    }

    // Parse Input
    fun String.toTargetArea() : TargetArea {
        val (x1, x2, y1, y2) = regex.find(this)?.destructured ?: error("failed to parse")
        return TargetArea(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt())
    }

    // Output
    val input = readInput("Day17").first().toTargetArea()

    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}