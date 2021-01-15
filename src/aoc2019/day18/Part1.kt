package aoc2019.day18

import java.nio.file.Files
import java.nio.file.Paths

private const val KEYS = 26

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day18/input.txt"))

    val map = createMap(input)
    val graph = createGraph(map)
    val answer = findShortestDistance(graph)
    println(answer)
}

typealias Doors = List<Char>
typealias State = Triple<Point, Direction, Doors>
// Graph is a pair of 2D arrays. Array 1 holds weights of edges and array 2 holds doors between edges
// Indices: Part 1: 0 -> @; 1-26 -> a-z     Part 2: 0-3 -> @; 4-29 -> a-z
typealias Graph = Pair<Array<Array<Int>>, Array<Array<List<Char>>>>

// bfs with pruning, ~2 seconds on my machine
private fun findShortestDistance(graph: Graph): Int {
    var states = mutableMapOf<Pair<Char, Set<Char>>, Int>()

    // initialize states
    states[Pair('@', emptySet())] = 0

    val (edges, doors) = graph
    val allKeys = ('a' until 'a' + KEYS).toSet()

    repeat(KEYS) {
        val newStates = mutableMapOf<Pair<Char, Set<Char>>, Int>()

        for (state in states) {
            val (keys, steps) = state
            val (currentKey, prevKeys) = keys

            if (currentKey == '@') {
                for (nextKey in allKeys) {
                    val nextDoors = doors[0][nextKey - 'a' + 1]
                    if ((prevKeys + currentKey).containsAll(nextDoors)) {
                        val minSteps = newStates[Pair(nextKey, prevKeys + currentKey)]
                        val newSteps = steps + edges[0][nextKey - 'a' + 1]
                        if (minSteps == null || newSteps < minSteps) {
                            newStates[Pair(nextKey, prevKeys + currentKey)] = newSteps
                        }
                    }
                }
            } else {
                for (nextKey in allKeys - currentKey - prevKeys) {
                    val nextDoors = doors[currentKey - 'a' + 1][nextKey - 'a' + 1]
                    if ((prevKeys + currentKey).containsAll(nextDoors)) {
                        val minSteps = newStates[Pair(nextKey, prevKeys + currentKey)]
                        val newSteps = steps + edges[currentKey - 'a' + 1][nextKey - 'a' + 1]
                        if (minSteps == null || newSteps < minSteps) {
                            newStates[Pair(nextKey, prevKeys + currentKey)] = newSteps
                        }
                    }
                }
            }
        }

        states = newStates
    }

    return states.values.minOrNull() ?: -1
}

// using a dfs implementation, ~50 seconds on my input
/*
private fun findShortestDistance(graph: Graph): Int {
    val (edges, doors) = graph
    var minSteps = Int.MAX_VALUE
    val keys = ('a' until 'a' + KEYS).toSet()

    for (key in keys) {
        if (doors[0][key - 'a' + 1].isEmpty()) {
            val remainingSteps = dfs(setOf(), key, keys - key, 0, graph)
            if (remainingSteps != -1) {
                val steps = remainingSteps + edges[0][key - 'a' + 1]
                if (steps < minSteps) {
                    minSteps = steps
                }
            }
        }
    }

    return minSteps
}

private fun dfs(path: Set<Char>, start: Char, neighbors: Set<Char>, depth: Int, graph: Graph): Int {
    val (edges, doors) = graph
    val newPath = path + start

    if (neighbors.size == 1) {
        val keyIndex = start - 'a' + 1
        val nextIndex = neighbors.first() - 'a' + 1
        if (newPath.containsAll(doors[keyIndex][nextIndex])) {
            return edges[keyIndex][nextIndex]
        } else {
            return -1
        }
    } else {
        var minSteps = Int.MAX_VALUE
        for (neighbor in neighbors) {
            val state = Pair(newPath, neighbor)
            val keyIndex = start - 'a' + 1
            val nextIndex = neighbor - 'a' + 1
            val cacheIndex = pathCache[depth].indexOf(state)
            if (cacheIndex == -1) {
                if (newPath.containsAll(doors[keyIndex][nextIndex])) {
                    val remainingSteps = dfs(newPath, neighbor, neighbors - neighbor, depth + 1, graph)

                    pathCache[depth].add(state)
                    stepCache[depth].add(remainingSteps)

                    if (remainingSteps != -1) {
                        val steps = remainingSteps + edges[keyIndex][nextIndex]
                        if (steps < minSteps) {
                            minSteps = steps
                        }
                    }
                }
            } else {
                val remainingSteps = stepCache[depth][cacheIndex]
                if (remainingSteps != -1) {
                    val steps = remainingSteps + edges[keyIndex][nextIndex]
                    if (steps < minSteps) {
                        minSteps = steps
                    }
                }
            }
        }

        if (minSteps == Int.MAX_VALUE) {
            return -1
        } else {
            return minSteps
        }
    }
}
 */

private fun createGraph(map: Grid): Graph {
    val weights = Array(KEYS + 1) { Array(KEYS + 1) { -1 } }
    val doorGrid = Array(KEYS + 1) { Array(KEYS + 1) { listOf<Char>() } }

    for (char in '`' until 'a' + KEYS) {
        val startingKey = if (char == '`') '@' else char
        var keysFound = if (startingKey == '@') 0 else 1
        var steps = 0
        val startPos = map.pointOf(startingKey.toInt())

        val states = mutableSetOf<State>()
        val newStates = mutableSetOf<State>()
        val prevPos = mutableSetOf<Point>()

        // initializing states
        states.add(State(startPos, Direction.NORTH, listOf()))
        states.add(State(startPos, Direction.SOUTH, listOf()))

        while (keysFound < KEYS) {
            steps++
            states.forEach { (pos, dir, doors) ->
                Direction.values().forEach { newDir ->
                    if (newDir != dir.opposite()) {
                        val newPos = pos.move(newDir)
                        val newPosElement = map.get(newPos).toChar()

                        if (newPosElement != '#' && newPos !in prevPos) {
                            if (newPosElement in 'A' until 'A' + KEYS) {
                                newStates.add(State(newPos, newDir, doors + newPosElement.toLowerCase()))
                            } else {
                                if (newPosElement in 'a' until 'a' + KEYS) {
                                    weights[char - 'a' + 1][newPosElement - 'a' + 1] = steps
                                    doorGrid[char - 'a' + 1][newPosElement - 'a' + 1] = doors
                                    if (startingKey != '@') {
                                        weights[newPosElement - 'a' + 1][char - 'a' + 1] = steps
                                        doorGrid[newPosElement - 'a' + 1][char - 'a' + 1] = doors
                                    }
                                    keysFound++
                                }
                                newStates.add(State(newPos, newDir, doors))
                            }
                            prevPos.add(newPos)
                        }
                    }
                }
            }

            states.clear()
            states.addAll(newStates)

            newStates.clear()
        }
    }

    return Pair(weights, doorGrid)
}

// an earlier bfs approach, ~21 seconds on my input
/*
private fun bfs(map: Grid): Int {
    var hasFoundAllKeys = false
    var steps = 0

    val startPos = map.pointOf('@'.toInt())
    val states = mutableSetOf(State(startPos, emptyList()))
    val prevStates = mutableSetOf<State>()
    val newStates = mutableSetOf<State>()
    prevStates.add(State(startPos, emptyList()))

    while (!hasFoundAllKeys) {
        states.forEach { (pos, keys) ->
            Direction.values().forEach { dir ->
                val newPos = pos.move(dir)
                val newPosElement = map.get(newPos).toChar()

                if (newPosElement != '#' && !(newPosElement in 'A'..'Z' && !keys.contains(newPosElement.toLowerCase()))) {
                    val newKeys = if (newPosElement in 'a'..'z' && !keys.contains(newPosElement)) {
                        (keys + newPosElement).sorted()
                    } else { keys }

                    val newState = State(newPos, newKeys)
                    if (!prevStates.contains(newState)) {
                        if (newKeys.size == 26) {
                            hasFoundAllKeys = true
                        } else {
                            newStates.add(newState)
                        }
                    }
                }
            }
        }

        states.clear()
        states.addAll(newStates)

        prevStates.addAll(newStates)

        newStates.clear()

        steps++
    }

    return steps
}
 */

private fun createMap(input: List<String>): Grid {
    val map = Grid(0)
    input.forEachIndexed{ y, str ->
        str.forEachIndexed { x, char ->
            map.set(char.toInt(), x, y)
        }
    }

    return map
}

private fun Direction.opposite(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.SOUTH
        Direction.SOUTH -> Direction.NORTH
        Direction.EAST  -> Direction.WEST
        Direction.WEST  -> Direction.EAST
    }
}