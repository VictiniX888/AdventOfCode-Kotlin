package aoc2019.day02

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day02/input.txt"))
        .split(",")
        .map { Integer.parseInt(it) }

    val answer = run(input).instructions[0]
    println(answer)
}

private fun run(instructions: List<Int>): IntcodeProgram {

    val modifiedInstructions = instructions.toMutableList().apply {
        this[1] = 12
        this[2] = 2
    }
    val program = IntcodeProgram(modifiedInstructions)
    val interpreter = IntcodeInterpreter(program)
    interpreter.runProgram()

    return program
}