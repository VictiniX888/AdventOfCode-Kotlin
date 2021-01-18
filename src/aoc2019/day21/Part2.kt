package aoc2019.day21

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day21/input.txt"))
        .split(',')
        .map { it.toLong() }

    val answer = assessHullDamage(input)
    println(answer)
}

// Requires manual input into the console. Input can be found in springscript2.txt
private fun assessHullDamage(intcode: List<Long>): Long {
    val program = IntcodeProgram(intcode.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()
    val prompt = interpreter.getAllOutputs()
    prompt.forEach { print(it.toChar()) }

    while (!interpreter.hasTerminatedSuccessfully) {
        val input = readLine()!!
        interpreter.sendInput(*input.map { it.toLong() }.toLongArray(), 10)
        interpreter.runProgram()
    }

    val output = interpreter.getAllOutputs()
    if (output.last() > 128) {
        output.dropLast(1).forEach { print(it.toChar()) }
        return output.last()
    } else {
        output.forEach { print(it.toChar()) }
        return -1
    }
}