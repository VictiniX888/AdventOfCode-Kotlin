package aoc2019.day03

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day03/input.txt"))
    val wire1 = parsePath(input[0].split(","))
    val wire2 = parsePath(input[1].split(","))

    val nearestIntersection = getStepsOfNearestIntersectionPoint(getIntersectionPoints(wire1, wire2))
    if (nearestIntersection != null) {
        println(nearestIntersection)
    }
}

private fun getStepsOfNearestIntersectionPoint(intersections: Map<Point, Int>): Int? = intersections.minBy { (_, steps) -> steps }?.value

private fun getIntersectionPoints(wire1: Map<Point, Int>, wire2: Map<Point, Int>): Map<Point, Int> {

    val intersectionSet = wire1.keys.intersect(wire2.keys)
    return intersectionSet.associateWith { wire1.getValue(it) + wire2.getValue(it) }
}

private fun parsePath(path: List<String>): Map<Point, Int> {

    var currentPoint = Point(0, 0)
    var currentStep = 0
    val traversedPoints = mutableMapOf<Point, Int>()

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
            currentStep++
            traversedPoints[currentPoint] = currentStep
        }
    }

    return traversedPoints
}