package aoc2019.day19

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val RANGE = 50L

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day19/input.txt"))
        .split(',')
        .map { it.toLong() }

    val answer = findBeamedPoints(input).count()
    println(answer)
}

private fun findBeamedPoints(intcode: List<Long>, range: Long = RANGE): Set<Point> {
    val beamedPoints = mutableSetOf<Point>()

    for (x in 0L until range) {
        for (y in 0L until range) {
            val program = IntcodeProgram(intcode.toMutableList())
            val interpreter = IntcodeInterpreter(program)

            interpreter.sendInput(x, y)
            interpreter.runProgram()
            if (interpreter.getOutput() == 1L) {
                beamedPoints.add(Point(x, y))
            }
        }
    }

    return beamedPoints
}