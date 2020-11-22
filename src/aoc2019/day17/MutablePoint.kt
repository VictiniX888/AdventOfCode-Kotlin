package aoc2019.day17

data class MutablePoint(var x: Int, var y: Int) {

    fun move(direction: Direction, steps: Int = 1) =
        when (direction) {
            Direction.WEST  -> x -= steps
            Direction.EAST  -> x += steps
            Direction.NORTH -> y -= steps
            Direction.SOUTH -> y += steps
        }
}