package aoc2019.day20

data class Position(val x: Int, val y: Int, val depth: Int = 0) {
    constructor(point: Point, depth: Int = 0) : this(point.x, point.y, depth)
    
    val point = Point(x, y)
}