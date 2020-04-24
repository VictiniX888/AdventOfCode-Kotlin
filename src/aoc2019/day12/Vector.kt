package aoc2019.day12

data class Vector(val x: Int, val y: Int, val z: Int) {

    operator fun plus(increment: Vector): Vector = Vector(x + increment.x, y + increment.y, z + increment.z)

    operator fun minus(increment: Vector): Vector = Vector(x - increment.x, y - increment.y, z - increment.z)
}