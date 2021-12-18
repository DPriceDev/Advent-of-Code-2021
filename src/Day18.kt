import kotlin.math.ceil

sealed class Number {
    sealed class Leaf : Number() {
        abstract var value: Int
        var prev: Leaf? = null
        var next: Leaf? = null

        data class Left(override var value: Int) : Leaf()
        data class Right(override var value: Int) : Leaf()
    }

    data class Branch(var left: Number, var right: Number) : Number()
}

fun Number.Branch.findExplodable(level: Int = 1): Number? =
    left.findExplodable(level, Number.Leaf.Left(0)) { left = it }
        ?: right.findExplodable(level, Number.Leaf.Right(0)) { right = it }

fun Number.findExplodable(
    level: Int,
    leaf: Number.Leaf,
    setLeaf: (Number.Leaf) -> Unit
): Number? = when (val number = this) {
    is Number.Leaf -> null
    is Number.Branch -> when {
        level < 4 -> number.findExplodable(level + 1)
        level == 4 && number.left is Number.Leaf.Left && number.right is Number.Leaf.Right -> {
            updateAdjacentValues(number)
            setLeaf(leaf.updateLinkedLeaves(number))
            this
        }
        else -> null
    }
}

fun Number.Leaf.updateLinkedLeaves(branch: Number.Branch): Number.Leaf {
    (branch.right as Number.Leaf.Right).next?.prev = this
    next = (branch.right as Number.Leaf.Right).next
    prev = (branch.left as Number.Leaf.Left).prev
    (branch.left as Number.Leaf.Left).prev?.next = this
    return this
}

fun updateAdjacentValues(branch: Number.Branch) {
    (branch.left as Number.Leaf).prev?.let { it.value += (branch.left as Number.Leaf).value }
    (branch.right as Number.Leaf).next?.let { it.value += (branch.right as Number.Leaf).value }
}

fun Number.Branch.findSplitable(): Number? = left.findSplitable { left = it } ?: right.findSplitable { right = it }

fun Number.findSplitable(setBranch: (Number.Branch) -> Unit): Number? = when (val number = this) {
    is Number.Leaf -> {
        if (number.value > 9) {
            setBranch(
                Number.Branch(
                    Number.Leaf.Left(
                        number.value / 2
                    ),
                    Number.Leaf.Right(
                        ceil(number.value.toFloat() / 2f).toInt()
                    )
                ).updateBranchLinks(number)
            )
            number
        } else {
            null
        }
    }
    is Number.Branch -> number.findSplitable()
}

private fun Number.Branch.updateBranchLinks(number: Number.Leaf): Number.Branch {
    number.prev?.let { prev -> prev.next = left as Number.Leaf }
    number.next?.let { next -> next.prev = right as Number.Leaf }
    (left as Number.Leaf.Left).prev = number.prev
    (left as Number.Leaf.Left).next = (right as Number.Leaf.Right)
    (right as Number.Leaf.Right).prev = (left as Number.Leaf.Left)
    (right as Number.Leaf.Right).next = number.next
    return this
}

fun Number.Branch.magnitude(): Long = (3 * left.magnitude()) + (2 * right.magnitude())

fun Number.magnitude(): Long = when (val number = this) {
    is Number.Leaf -> number.value.toLong()
    is Number.Branch -> number.magnitude()
}

fun main() {

    fun MutableList<Char>.badlyParseInput(): Number {
        return when {
            get(0).isDigit() && get(1) == ',' -> {
                Number.Leaf.Left(get(0).digitToInt()).also {
                    this.removeAt(0)
                    this.removeAt(0)
                }
            }
            get(0) == ',' && get(1).isDigit() -> {
                Number.Leaf.Right(get(1).digitToInt()).also {
                    this.removeAt(0)
                    this.removeAt(0)
                }
            }
            get(0).isDigit() -> {
                Number.Leaf.Right(get(0).digitToInt()).also {
                    this.removeAt(0)
                }
            }
            get(0) == '[' -> Number.Branch(
                apply { removeAt(0) }.badlyParseInput(),
                badlyParseInput(),
            ).also { removeAt(0) }
            get(0) == ',' && get(1) == '[' -> Number.Branch(
                apply {
                    removeAt(0)
                    removeAt(0)
                }.badlyParseInput(),
                badlyParseInput(),
            ).also { removeAt(0) }
            else -> error("")
        }
    }

    fun Number.asLeaves(): List<Number.Leaf> = when (this) {
        is Number.Leaf -> listOf(this)
        is Number.Branch -> left.asLeaves().plus(right.asLeaves())
    }

    fun Number.Branch.asLeaves(): List<Number.Leaf> = left.asLeaves().plus(right.asLeaves())

    fun Number.add(other: Number) = Number.Branch(this, other).apply {
        val last = (left as Number.Branch).asLeaves().last()
        val first = (right as Number.Branch).asLeaves().first()
        first.prev = last
        last.next = first
    }

    fun Number.Branch.reduce(): Number {
        (0..Int.MAX_VALUE).forEach { _ -> findExplodable() ?: findSplitable() ?: return this }
        error("Number failed to reduce")
    }

    // Part 1
    fun part1(input: List<String>) = input.map { it.toMutableList().badlyParseInput() }.apply {
        map { (it as Number.Branch).asLeaves() }.forEach {
            if (it.size >= 3) {
                it.windowed(3).forEach { (a, b, c) ->
                    a.next = b
                    b.prev = a
                    b.next = c
                    c.prev = b
                }
            } else {
                it.first().next = it.last()
                it.last().prev = it.first()
            }
        }
    }.reduce { first, second -> first.add(second).reduce() }.let { (it as Number.Branch).magnitude() }


    // Part 2
    fun part2(input: List<String>): Long = input.asSequence().map { line ->
        input.map { it to line }.plus(input.map { line to it })
    }.flatten().map { listOf(it.first, it.second) }.map { part1(it) }.maxOf { it }

    // Output
    val input = readInput("Day18")

    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}