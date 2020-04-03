package aoc2019.day09

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get("src/aoc2019/day09/input.txt"))
        .split(",")
        .map { it.toLong() }

    println(run(input))
}

private fun run(instructions: List<Long>): List<Long> {
    val program = IntcodeProgram(instructions.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    return interpreter.runProgram(1)
}