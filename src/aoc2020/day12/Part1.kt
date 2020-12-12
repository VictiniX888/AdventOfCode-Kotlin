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
    var facing = 0 // 0 = EAST, clockwise ++

    instructions.forEach { instr ->
        val action = instr[0]
        val i = instr.substring(1).toInt()
        when (action) {
            'N' -> y += i
            'S' -> y -= i
            'E' -> x += i
            'W' -> x -= i
            'L' -> facing = (360 + facing - i) % 360
            'R' -> facing = (facing + i) % 360
            'F' -> when (facing) {
                0 -> x += i
                90 -> y -= i
                180 -> x -= i
                270 -> y += i
                else -> error("Unknown degree found")
            }
        }
    }

    return x.absoluteValue + y.absoluteValue
}