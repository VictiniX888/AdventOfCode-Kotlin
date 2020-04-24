package aoc2019.day12

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue

private const val STEPS = 1000

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day12/input.txt"))
        .map { it.substring(1 .. it.length-2)
            .split(", ")
            .map { s -> s.substring(2).toInt() } }
        .map { Vector(it[0], it[1], it[2]) }

    val answer = totalEnergy(move(input))
    println(answer)
}

private fun totalEnergy(moons: List<Pair<Vector, Vector>>): Int =
    moons.sumBy { (pos, vel) -> energy(pos) * energy(vel) }

private fun energy(vector: Vector): Int =
    vector.x.absoluteValue + vector.y.absoluteValue + vector.z.absoluteValue

private fun move(positions: List<Vector>, times: Int = STEPS): List<Pair<Vector, Vector>> {

    var currentPositions = positions.toList()
    var currentVelocities = listOf(Vector(0, 0, 0), Vector(0, 0, 0), Vector(0, 0, 0), Vector(0, 0, 0))

    repeat(times) {
        currentVelocities = applyGravity(currentPositions, currentVelocities)
        currentPositions = applyVelocity(currentPositions, currentVelocities)
    }

    return currentPositions.zip(currentVelocities)
}

private fun applyVelocity(positions: List<Vector>, velocities: List<Vector>): List<Vector> =
    positions.zip(velocities) { pos, vel -> pos + vel }

private fun applyGravity(positions: List<Vector>, velocities: List<Vector>): List<Vector> {
    val mutableVelocities = velocities.toMutableList()

    positions.forEachIndexed { i, pos1 ->
        positions.slice(i+1 until positions.size).forEachIndexed { j, pos2 ->
            val posDiff = pos2 - pos1

            val velAdd1 = Vector(
                when {
                    posDiff.x < 0 -> -1
                    posDiff.x > 0 -> 1
                    else -> 0
                },
                when {
                    posDiff.y < 0 -> -1
                    posDiff.y > 0 -> 1
                    else -> 0
                },
                when {
                    posDiff.z < 0 -> -1
                    posDiff.z > 0 -> 1
                    else -> 0
                }
            )

            val velAdd2 = Vector(
                when {
                    posDiff.x < 0 -> 1
                    posDiff.x > 0 -> -1
                    else -> 0
                },
                when {
                    posDiff.y < 0 -> 1
                    posDiff.y > 0 -> -1
                    else -> 0
                },
                when {
                    posDiff.z < 0 -> 1
                    posDiff.z > 0 -> -1
                    else -> 0
                }
            )

            mutableVelocities[i] += velAdd1
            mutableVelocities[i+j+1] += velAdd2
        }
    }

    return mutableVelocities
}