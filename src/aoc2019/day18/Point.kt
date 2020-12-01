package aoc2019.day18

data class Point(val x: Int, val y: Int) {

    fun move(direction: Direction, steps: Int = 1): Point =
        when (direction) {
            Direction.WEST  -> Point(x - steps, y)
            Direction.EAST  -> Point(x + steps, y)
            Direction.NORTH -> Point(x, y - steps)
            Direction.SOUTH -> Point(x, y + steps)
        }
}