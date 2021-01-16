package aoc2019.day19

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val LENGTH = 100L

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day19/input.txt"))
        .split(',')
        .map { it.toLong() }

    val answer = findSquare(input)
    println(answer.x * 10000 + answer.y)
}

private fun findSquare(intcode: List<Long>, length: Long = LENGTH): Point {
    var xStart = 0L         // start inclusive
    var xEnd = 0L           // end exclusive
    var yEnd = length - 1   // inclusive

    var trackedLength = 0

    while (true) {
        val program = IntcodeProgram(intcode.toMutableList())
        val interpreter = IntcodeInterpreter(program)

        interpreter.sendInput(xEnd, yEnd)
        interpreter.runProgram()

        if (interpreter.getOutput() == 1L) {
            xEnd++
            trackedLength++
        } else {
            if (trackedLength == 0) {
                xStart++
                xEnd++
            } else {
                trackedLength = 0
                xEnd = xStart
                yEnd++
            }
        }

        if (trackedLength >= 100) {
            val programNew = IntcodeProgram(intcode.toMutableList())
            val interpreterNew = IntcodeInterpreter(programNew)

            interpreterNew.sendInput(xEnd - 1, yEnd - length + 1)
            interpreterNew.runProgram()

            if (interpreterNew.getOutput() == 1L) {
                return Point(xStart, yEnd - length + 1)
            } else {
                trackedLength = 0
                xEnd = xStart
                yEnd++
            }
        }
    }
}