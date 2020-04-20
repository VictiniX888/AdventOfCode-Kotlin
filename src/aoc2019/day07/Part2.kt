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

    var hasTerminated = false
    var outputSignal = 0L

    // initialization of phase setting on each program
    val interpreters = programs.map { (program, phase) ->
        IntcodeInterpreter(program).apply { sendInput(phase.toLong()) }
    }

    // feed output back into input as long as none of the programs have terminated
    while (!hasTerminated) {
        interpreters.forEach {
            it.sendInput(outputSignal)
            it.runProgram()
            outputSignal = it.getOutput()

            if (it.hasTerminatedSuccessfully) {
                hasTerminated = true
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