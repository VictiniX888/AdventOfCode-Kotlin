package aoc2019.day02

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val REQUIRED_OUTPUT = 19690720

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day02/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = findNounVerb(input)

    if (answer != null) {
        println(answer.first*100 + answer.second)
    }
}

private typealias NounVerb = Pair<Int, Int>
private fun findNounVerb(instructions: List<Long>): NounVerb? {

    for (noun in 0..99) {
        for (verb in 0..99) {
            val program = IntcodeProgram(instructions.toMutableList().apply {
                this[1] = noun.toLong()
                this[2] = verb.toLong()
            })
            val interpreter = IntcodeInterpreter(program)

            interpreter.runProgram()
            val output = program.instructions[0]
            return if (output == REQUIRED_OUTPUT.toLong()) NounVerb(noun, verb) else continue
        }
    }

    return null
}