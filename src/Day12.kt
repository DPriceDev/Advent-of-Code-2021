fun main() {
    data class Node(val name: String, val connectedTo: Set<String> = setOf())
    data class Path(val nodes: List<Node> = listOf())

    val regex = """(\w+)-(\w+)""".toRegex()

    // Very slow for part 2, Potentially better to flip this and construct node routes first to remove duplicate checks
    fun Map<String, Node>.searchForPaths(
        currentNode: Node,
        nodes: List<Node> = listOf(currentNode),
        visitSmallTwice: Boolean = false
    ): List<Path> = when (currentNode.name) {
        "end" -> listOf(Path(nodes))
        else -> this[currentNode.name]
            ?.connectedTo
            ?.map { get(it) ?: error("Node Not Found") }
            ?.filter { connectedNode ->
                !nodes.filter { it.name.isLowercase }.any { it.name == connectedNode.name }
                        || (visitSmallTwice
                        && !nodes.filter { it.name.isLowercase }.groupBy { it.name }.any { it.value.count() == 2 }
                        && (nodes.filter { it.name.isLowercase }.count { it.name == connectedNode.name } < 2)
                        && (connectedNode.name != "start"))
            }
            .takeIf { it?.isNotEmpty() ?: false }
            ?.map { path -> searchForPaths(path, nodes.plus(path), visitSmallTwice) }
            ?.flatten()
            ?: listOf()
    }

    // Part 1
    fun part1(input: Map<String, Node>): Int = input.searchForPaths(Node("start")).count()

    // Part 2
    fun part2(input: Map<String, Node>): Int = input.searchForPaths(Node("start"), visitSmallTwice = true).count()

    // Parse input
    fun List<String>.asNodes(): Map<String, Node> = fold(mapOf()) { nodeMap, line ->
        val (a, b) = regex.find(line)?.destructured ?: error("Failed to parse line")
        nodeMap.toMutableMap().apply {
            val nodeA = getOrPut(a) { Node(a) }
            val nodeB = getOrPut(b) { Node(b) }
            put(a, nodeA.copy(connectedTo = nodeA.connectedTo.plus(b)))
            put(b, nodeB.copy(connectedTo = nodeB.connectedTo.plus(a)))
        }
    }

    val input = readInput("Day12").asNodes()

    // Output
    val (time1, answer1) = measureTimeWithAnswer { part1(input) }
    println("part 1 answer = $answer1 taking $time1 milliseconds")
    val (time2, answer2) = measureTimeWithAnswer { part2(input) }
    println("part 2 answer = $answer2 taking $time2 milliseconds")
}