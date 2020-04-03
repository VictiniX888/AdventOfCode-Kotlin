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
    val phaseSequences = listOf(0, 1, 2, 3, 4).permutations()
    return phaseSequences.map { amplifyAll(it, instructions) }.max()
}

private fun amplifyAll(phaseSettingSequence: List<Int>, instructions: List<Long>): Long =
    phaseSettingSequence.fold(0L) { acc, i -> amplify(i, acc, instructions) }

private fun amplify(phaseSetting: Int, inputSignal: Long, instructions: List<Long>): Long {
    val program = IntcodeProgram(instructions.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    return interpreter.runProgram(phaseSetting.toLong(), inputSignal).first()
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