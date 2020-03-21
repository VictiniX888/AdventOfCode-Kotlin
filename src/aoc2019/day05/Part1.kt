package aoc2019.day05

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

// Code works for both Part 1 and Part 2 -> only difference is the manual input required
fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day05/input.txt"))
        .split(",")
        .map { it.toInt() }

    run(input)
    // For Part 1: Type "1" when prompted for input
    // For Part 2: Type "5" when prompted for input
}

private fun run(instructions: List<Int>) {
    val program = IntcodeProgram(instructions.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()
        .also { if (it) println("Program finished running successfully") }
}