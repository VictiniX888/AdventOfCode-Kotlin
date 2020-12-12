package aoc2020.day12

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day12/input.txt"))

    val answer = moveShip(input)
    println(answer)
}

private fun moveShip(instructions: List<String>): Int {
    var x = 0
    var y = 0
    var waypointX = 10 // offset
    var waypointY = 1

    instructions.forEach { instr ->
        val action = instr[0]
        val i = instr.substring(1).toInt()
        when (action) {
            'N' -> waypointY += i
            'S' -> waypointY -= i
            'E' -> waypointX += i
            'W' -> waypointX -= i
            'R' -> when (i) {
                90 -> waypointY = -waypointX.also { waypointX = waypointY }
                180 -> { waypointX = -waypointX; waypointY = -waypointY }
                270 -> waypointY = waypointX.also { waypointX = -waypointY }
            }
            'L' -> when (i) {
                270 -> waypointY = -waypointX.also { waypointX = waypointY }
                180 -> { waypointX = -waypointX; waypointY = -waypointY }
                90 -> waypointY = waypointX.also { waypointX = -waypointY }
            }
            'F' -> {
                x += waypointX * i
                y += waypointY * i
            }
        }
    }

    return x.absoluteValue + y.absoluteValue
}