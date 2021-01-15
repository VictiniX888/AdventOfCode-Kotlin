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

private fun findShortestDistance(graph: Graph): Int {
    var states = mutableMapOf<Pair<Set<Char>, Set<Char>>, Int>()

    // initialize states
    states[Pair(setOf(']', '^', '_', '`'), emptySet())] = 0

    val (edges, doors) = graph
    val allKeys = ('a' until 'a' + KEYS).toSet()

    repeat(KEYS) {
        val newStates = mutableMapOf<Pair<Set<Char>, Set<Char>>, Int>()

        for (state in states) {
            val (keys, steps) = state
            val (currentKeys, prevKeys) = keys

            for (currentKey in currentKeys) {
                val keyIndex = when (currentKey) {
                    ']' -> 0
                    '^' -> 1
                    '_' -> 2
                    '`' -> 3
                    else -> currentKey - 'a' + 4
                }

                for (nextKey in allKeys - currentKeys - prevKeys) {
                    val nextSteps = edges[keyIndex][nextKey - 'a' + 4]
                    if (nextSteps != -1) {
                        val nextDoors = doors[keyIndex][nextKey - 'a' + 4]
                        if ((prevKeys + currentKeys).containsAll(nextDoors)) {
                            val minSteps = newStates[Pair(currentKeys - currentKey + nextKey, prevKeys + currentKey)]
                            val newSteps = steps + nextSteps
                            if (minSteps == null || newSteps < minSteps) {
                                newStates[Pair(currentKeys - currentKey + nextKey, prevKeys + currentKey)] = newSteps
                            }
                        }
                    }
                }
            }
        }

        states = newStates
    }

    return states.values.minOrNull() ?: -1
}

private fun createGraph(map: Grid): Graph {
    val weights = Array(KEYS + 4) { Array(KEYS + 4) { -1 } }
    val doorGrid = Array(KEYS + 4) { Array(KEYS + 4) { listOf<Char>() } }

    for (char in ']' until 'a' + KEYS) {
        val startingKey = if (char < 'a') '@' else char
        //var keysFound = if (startingKey == '@') 0 else 1
        var steps = 0
        val startPos = when (char) {
            ']' -> Point(map.width() / 2 - 1, map.height() / 2 - 1)
            '^' -> Point(map.width() / 2 + 1, map.height() / 2 - 1)
            '_' -> Point(map.width() / 2 - 1, map.height() / 2 + 1)
            '`' -> Point(map.width() / 2 + 1, map.height() / 2 + 1)
            else -> map.pointOf(startingKey.toInt())
        }

        val states = mutableSetOf<State>()
        val newStates = mutableSetOf<State>()
        val prevPos = mutableSetOf<Point>()

        // initializing states
        states.add(State(startPos, Direction.NORTH, listOf()))
        states.add(State(startPos, Direction.SOUTH, listOf()))

        while (states.isNotEmpty()) {
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
                                    weights[char - 'a' + 4][newPosElement - 'a' + 4] = steps
                                    doorGrid[char - 'a' + 4][newPosElement - 'a' + 4] = doors
                                    if (startingKey != '@') {
                                        weights[newPosElement - 'a' + 4][char - 'a' + 4] = steps
                                        doorGrid[newPosElement - 'a' + 4][char - 'a' + 4] = doors
                                    }
                                    //keysFound++
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

private fun createMap(input: List<String>): Grid {
    val height = input.size
    val width = input[0].length

    val map = Grid(0)
    input.forEachIndexed{ y, str ->
        str.forEachIndexed { x, char ->
            if ((x == (width / 2) - 1 || x == (width / 2) + 1) && (y == (height / 2) - 1 || y == (height / 2) + 1)) {
                map.set('@'.toInt(), x, y)
            } else if (x in (width / 2) - 1 .. (width / 2) + 1 && y in (height / 2) - 1 .. (height / 2) + 1) {
                map.set('#'.toInt(), x, y)
            } else {
                map.set(char.toInt(), x, y)
            }
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