package aoc2019.day20

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day20/input.txt"))
        .map { it.toCharArray().toList() }

    val answer = findShortestPath(input)
    println(answer)
}

private fun findShortestPath(map: List<List<Char>>): Int {
    val warpPoints = mapWarpPoints(map)
    val startingPosition = Position(warpPoints[Point(0, 0)] ?: error("Starting point not found"))
    val endPosition = Position(warpPoints[Point(-1, -1)] ?: error("End point not found"))

    var steps = 0
    var currentPositions = setOf(startingPosition)
    val prevPositions = mutableSetOf<Position>()

    outer@while (true) {
        val newPositions = mutableSetOf<Position>()
        for (currentPosition in currentPositions) {
            if (currentPosition == endPosition) {
                break@outer
            }

            if (currentPosition.point in warpPoints) {
                if (currentPosition.x < 3 || currentPosition.x >= map[0].size - 3 || currentPosition.y < 3 || currentPosition.y >= map.size - 3) {
                    if (currentPosition.depth > 0) {
                        if (warpPoints[currentPosition.point]!! != Point(0, 0) || warpPoints[currentPosition.point]!! != Point(-1, -1)) {
                            newPositions += Position(warpPoints[currentPosition.point]!!, currentPosition.depth - 1)
                        }
                    }
                } else {
                    newPositions += Position(warpPoints[currentPosition.point]!!, currentPosition.depth + 1)
                }
            }

            if (map[currentPosition.y][currentPosition.x+1] == '.') newPositions += Position(currentPosition.x+1, currentPosition.y, currentPosition.depth)
            if (map[currentPosition.y][currentPosition.x-1] == '.') newPositions += Position(currentPosition.x-1, currentPosition.y, currentPosition.depth)
            if (map[currentPosition.y+1][currentPosition.x] == '.') newPositions += Position(currentPosition.x, currentPosition.y+1, currentPosition.depth)
            if (map[currentPosition.y-1][currentPosition.x] == '.') newPositions += Position(currentPosition.x, currentPosition.y-1, currentPosition.depth)
        }

        prevPositions += currentPositions
        newPositions -= prevPositions
        currentPositions = newPositions
        steps++
    }

    return steps
}

private fun mapWarpPoints(map: List<List<Char>>): Map<Point, Point> {
    val warpIdentifiers = mutableMapOf<String, Point>()
    val warpPoints = mutableMapOf<Point, Point>()

    map.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (c in 'A'..'Z') {
                if (y+1 < map.size && map[y+1][x] in 'A'..'Z') {
                    val identifier = c.toString() + map[y+1][x]
                    when (identifier) {
                        "AA" -> {
                            val point = if (y+2 >= map.size || map[y+2][x] != '.') Point(x, y-1) else Point(x, y+2)
                            warpPoints[Point(0, 0)] = point
                        }
                        "ZZ" -> {
                            val point = if (y+2 >= map.size || map[y+2][x] != '.') Point(x, y-1) else Point(x, y+2)
                            warpPoints[Point(-1, -1)] = point
                        }
                        in warpIdentifiers -> {
                            val warpFrom = warpIdentifiers[identifier]!!
                            val warpTo = if (y+2 >= map.size || map[y+2][x] != '.') Point(x, y-1) else Point(x, y+2)
                            warpIdentifiers.remove(identifier)
                            warpPoints[warpFrom] = warpTo
                            warpPoints[warpTo] = warpFrom
                        }
                        else -> {
                            val point = if (y+2 >= map.size || map[y+2][x] != '.') Point(x, y-1) else Point(x, y+2)
                            warpIdentifiers[identifier] = point
                        }
                    }
                } else if (x+1 < row.size && map[y][x+1] in 'A'..'Z') {
                    val identifier = c.toString() + map[y][x+1]
                    when (identifier) {
                        "AA" -> {
                            val point = if (x+2 >= row.size || map[y][x+2] != '.') Point(x-1, y) else Point(x+2, y)
                            warpPoints[Point(0, 0)] = point
                        }
                        "ZZ" -> {
                            val point = if (x+2 >= row.size || map[y][x+2] != '.') Point(x-1, y) else Point(x+2, y)
                            warpPoints[Point(-1, -1)] = point
                        }
                        in warpIdentifiers -> {
                            val warpFrom = warpIdentifiers[identifier]!!
                            val warpTo = if (x+2 >= row.size || map[y][x+2] != '.') Point(x-1, y) else Point(x+2, y)
                            warpIdentifiers.remove(identifier)
                            warpPoints[warpFrom] = warpTo
                            warpPoints[warpTo] = warpFrom
                        }
                        else -> {
                            val point = if (x+2 >= row.size || map[y][x+2] != '.') Point(x-1, y) else Point(x+2, y)
                            warpIdentifiers[identifier] = point
                        }
                    }
                }
            }
        }
    }

    return warpPoints
}