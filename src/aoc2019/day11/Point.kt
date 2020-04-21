package aoc2019.day11

import aoc2019.day11.Direction.*

data class Point(val x: Int, val y: Int) {

    fun move(direction: Direction): Point =
        when (direction) {
            LEFT  -> Point(x-1, y)
            RIGHT -> Point(x+1, y)
            UP    -> Point(x, y-1)
            DOWN  -> Point(x, y+1)
        }
}