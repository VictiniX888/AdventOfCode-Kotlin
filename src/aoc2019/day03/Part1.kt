package aoc2019.day03

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day03/input.txt"))
    val wire1 = parsePath(input[0].split(","))
    val wire2 = parsePath(input[1].split(","))

    val nearestIntersection = getNearestIntersectionPoint(getIntersectionPoints(wire1, wire2))
    if (nearestIntersection != null) {
        println(nearestIntersection.manhattanDistance)
    }
}

private fun getNearestIntersectionPoint(intersections: Set<Point>): Point? = intersections.minBy { it.manhattanDistance }

private fun getIntersectionPoints(wire1: Set<Point>, wire2: Set<Point>) = wire1.intersect(wire2)

private fun parsePath(path: List<String>): Set<Point> {

    var currentPoint = Point(0, 0)
    val traversedPoints = mutableSetOf<Point>()

    path.forEach {

        val direction = when (it.first()) {
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            else -> Direction.NONE
        }
        val steps = Integer.parseInt(it.substring(1))

        for (i in 0 until steps) {
            currentPoint = currentPoint.move(direction, 1)
            traversedPoints.add(currentPoint)
        }
    }

    return traversedPoints
}