package aoc2019.day03

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {

    val manhattanDistance by lazy { abs(x) + abs(y) }

    fun move(direction: Direction, steps: Int): Point =
        when (direction) {
            Direction.LEFT  -> Point(x-steps, y)
            Direction.RIGHT -> Point(x+steps, y)
            Direction.UP    -> Point(x, y+steps)
            Direction.DOWN  -> Point(x, y-steps)
            Direction.NONE  -> Point(x, y)
        }
}