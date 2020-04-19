package aoc2019.day10

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI

const val N_DESTROYED = 200

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day10/input.txt"))

    val detector = getPositionWithMostDetectableAsteroids(input)
    if (detector != null) {
        val answer = sortAsteroids(detector, allAsteroids(input))[N_DESTROYED - 1]
        println(answer.x * 100 + answer.y)
    }
}

private fun sortAsteroids(detector: Vector, asteroids: List<Vector>): List<Vector> =
    // This is a monstrosity
    asteroids.minus(detector)
        .groupBy {
            val theta = (detector - it).theta
            when {
                theta == PI/2               -> 0.0
                theta < PI/2 && theta > 0   -> (PI * 3 / 2) + theta
                theta == 0.0                -> PI * 3 / 2
                theta < 0 && theta > -PI/2  -> (PI * 3 / 2) + theta
                theta == -PI/2              -> PI
                theta < -PI/2               -> (PI * 3 / 2) + theta
                theta == PI                 -> PI / 2
                theta > PI/2                -> theta - PI/2
                else -> error("Error: Angle not found")
        } }
        .flatMap {
            if (it.value.size == 1) {
                listOf(Pair(it.value.first(), it.key))
            } else {
                it.value.sortedBy { vector -> (detector - vector).length }
                    .mapIndexed { index, vector -> Pair(vector, (index * 2 * PI) + it.key) }
            }
        }
        .sortedBy { it.second }
        .map { it.first }

private fun allAsteroids(map: List<String>): List<Vector> =
    map.mapIndexed { y, string ->
        string.mapIndexed { x, char ->
            if (char == '#') Vector(x, y) else null
        }
    }.flatten()
        .filterNotNull()

private fun getPositionWithMostDetectableAsteroids(map: List<String>): Vector? {
    return map.mapIndexed { y, string ->
        string.mapIndexed { x, char ->
            if (char == '#') Pair(Vector(x, y), detectableAsteroids(Vector(x, y), map)) else Pair(Vector(x, y), 0)
        }
    }.flatten()
        .maxBy { it.second }
        ?.first
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