package aoc2019.day07

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day07/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = getHighestSignal(input)
    println(answer)
}

private fun getHighestSignal(instructions: List<Long>): Long? {
    val phaseSequences = listOf(5, 6, 7, 8, 9).permutations()
    return phaseSequences.map { runFeedbackLoop(it, instructions) }.max()
}

private fun runFeedbackLoop(phaseSettingSequence: List<Int>, instructions: List<Long>): Long {

    val programs = listOf(
        IntcodeProgram(instructions.toMutableList()),
        IntcodeProgram(instructions.toMutableList()),
        IntcodeProgram(instructions.toMutableList()),
        IntcodeProgram(instructions.toMutableList()),
        IntcodeProgram(instructions.toMutableList())
    ).zip(phaseSettingSequence)

    val pointers = mutableListOf(0, 0, 0, 0, 0)

    var hasTerminated = false
    var outputSignal = 0L

    // initialization of phase setting on each program
    for (i in programs.indices) {
        programs[i].apply {
            val interpreter = IntcodeInterpreter(first)
            interpreter.runProgram(second.toLong())
            pointers[i] = interpreter.pointer
        }
    }

    // feed output back into input as long as none of the programs have terminated
    while (!hasTerminated) {
        for (i in programs.indices) {
            val program = programs[i].first
            val interpreter = IntcodeInterpreter(program, pointers[i])
            outputSignal = interpreter.runProgram(outputSignal).first()
            pointers[i] = interpreter.pointer

            // check whether amplifier E (last program) has terminated
            if (i == programs.size - 1) {
                hasTerminated = interpreter.hasTerminatedSuccessfully
            }
        }
    }

    return outputSignal
}

private fun <T> List<T>.permutations(): List<List<T>> {
    return if (this.isEmpty() || this.size == 1) {
        listOf(this)
    } else {
        this.map { (this - it).permutations() }
            .flatten()
            .map { it + (this - it) }
    }
}