package aoc2019.day02

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val REQUIRED_OUTPUT = 19690720

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day02/input.txt"))
        .split(",")
        .map { Integer.parseInt(it) }

    val answer = findNounVerb(input)
    answer?.let {
        println(answer.first*100 + answer.second)
    }
}

private typealias NounVerb = Pair<Int, Int>
private fun findNounVerb(instructions: List<Int>): NounVerb? {

    for (noun in 0..99) {
        for (verb in 0..99) {
            val program = IntcodeProgram(instructions.toMutableList().apply {
                this[1] = noun
                this[2] = verb
            })
            val interpreter = IntcodeInterpreter(program)

            val output = interpreter.runProgram().instructions[0]
            return if (output == REQUIRED_OUTPUT) NounVerb(noun, verb) else continue
        }
    }

    return null
}