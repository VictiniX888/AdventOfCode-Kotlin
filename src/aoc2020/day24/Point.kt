package aoc2020.day24

data class Point(val x: Int, val y: Int) {

    fun move(direction: String): Point {
        return when (direction) {
            "e" -> move(Direction.EAST)
            "se" -> move(Direction.SOUTHEAST)
            "sw" -> move(Direction.SOUTHWEST)
            "w" -> move(Direction.WEST)
            "nw" -> move(Direction.NORTHWEST)
            "ne" -> move(Direction.NORTHEAST)
            else -> this
        }
    }

    fun move(direction: Direction): Point {
        return when (direction) {
            Direction.EAST -> Point(x+1, y)
            Direction.SOUTHEAST -> Point(x, y-1)
            Direction.SOUTHWEST -> Point(x-1, y-1)
            Direction.WEST -> Point(x-1, y)
            Direction.NORTHWEST -> Point(x, y+1)
            Direction.NORTHEAST -> Point(x+1, y+1)
        }
    }
}