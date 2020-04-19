package aoc2019.day10

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day10/input.txt"))

    val answer = getMostDetectableAsteroids(input)
    println(answer)
}

private fun getMostDetectableAsteroids(map: List<String>): Int? {
    return map.mapIndexed { y, string ->
        string.mapIndexed { x, char ->
            if (char == '#') detectableAsteroids(Vector(x, y), map) else 0
        }
    }.flatten()
        .max()
}

private fun blockedPoints(detector: Vector, blocker: Vector, width: Int, height: Int): List<Vector> {
    val gradient = (blocker - detector).simplify()
    var currentPoint = blocker + gradient
    val blocked = mutableListOf<Vector>()
    while (currentPoint.x in 0 until width && currentPoint.y in 0 until height) {
        blocked.add(currentPoint)
        currentPoint += gradient
    }

    return blocked
}

private fun detectableAsteroids(detector: Vector, map: List<String>): Int {
    val visible = map.toMutableList().apply {
        this[detector.y] = this[detector.y].replaceRange(detector.x..detector.x, ".")
    }
    for (y in visible.indices) {
        for (x in visible[y].indices) {
            if (visible[y][x] == '#') {
                blockedPoints(detector, Vector(x, y), visible[y].length, visible.size).forEach {
                    visible[it.y] = visible[it.y].replaceRange(it.x..it.x, ".")
                }
            }
        }
    }

    return visible.sumBy { it.count { c -> c == '#' } }
}