package aoc2019.day05

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day05/input.txt"))
        .split(",")
        .map { it.toInt() }

    val output = run(input)
    println(output)
}

private fun run(instructions: List<Int>): List<Int> {
    val program = IntcodeProgram(instructions.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    return interpreter.runProgram(5)
        .also { if (interpreter.hasTerminatedSuccessfully) println("Program terminated successfully") }
}