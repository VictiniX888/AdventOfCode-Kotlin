package aoc2019.day13

data class Point(val x: Int, val y: Int) {

    operator fun minus(point: Point) = Point(x - point.x, y - point.y)
}