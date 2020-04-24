package aoc2019.day12

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day12/input.txt"))
        .map { it.substring(1 .. it.length-2)
            .split(", ")
            .map { s -> s.substring(2).toInt() } }
        .map { Vector(it[0], it[1], it[2]) }

    val answer = stepsToRepeatAll(input)
    println(answer)
}

// steps for the initial position and velocities in all 3 dimensions to repeat
// motion in each dimension is independent of each other, and
// each position-velocity state has a unique parent state therefore the first repeat will be the initial state
// there we first find the steps needed for the initial position and velocities to repeat in each dimension
// then find the LCM
private fun stepsToRepeatAll(positions: List<Vector>): Long {
    return positions.flatMap { listOf(it.x, it.y, it.z) }
        .withIndex()
        .groupBy { it.index % 3 }
        .map { stepsToRepeat(it.value.map { indexedValue -> indexedValue.value }) }
        .let { lcm(it[0], it[1], it[2]) }
}

// steps for the initial positions and velocities in one direction to repeat
private fun stepsToRepeat(positions: List<Int>): Long {
    var currentPositions = positions
    var currentVelocities = listOf(0, 0, 0, 0)
    var steps = 0L

    do {
        currentVelocities = applyGravity(currentPositions, currentVelocities)
        currentPositions = applyVelocity(currentPositions, currentVelocities)
        steps++
    } while (currentPositions != positions || currentVelocities != listOf(0, 0, 0, 0))

    return steps
}

private fun applyVelocity(positions: List<Int>, velocities: List<Int>): List<Int> =
    positions.zip(velocities) { pos, vel -> pos + vel }

private fun applyGravity(positions: List<Int>, velocities: List<Int>): List<Int> {
    val mutableVelocities = velocities.toMutableList()

    positions.forEachIndexed { i, pos1 ->
        positions.slice(i+1 until positions.size).forEachIndexed { j, pos2 ->
            val posDiff = pos2 - pos1

            val velAdd1 = when {
                posDiff < 0 -> -1
                posDiff > 0 -> 1
                else -> 0
            }

            val velAdd2 = when {
                posDiff < 0 -> 1
                posDiff > 0 -> -1
                else -> 0
            }

            mutableVelocities[i] += velAdd1
            mutableVelocities[i+j+1] += velAdd2
        }
    }

    return mutableVelocities
}

private fun lcm(i1: Long, i2: Long, i3: Long): Long = lcm(lcm(i1, i2), i3)

private fun lcm(i1: Long, i2: Long): Long = i1 * i2 / gcd(i1, i2)

private tailrec fun gcd(i1: Long, i2: Long): Long =     // using Euclid's algorithm
    when {
        i1 == 1L || i2 == 1L -> 1
        i1 == i2 -> i1
        i1 >  i2 -> gcd(i1 - i2, i2)
        i1 <  i2 -> gcd(i1, i2 - i1)
        else -> error("Error: Greatest common denominator could not be found")
    }