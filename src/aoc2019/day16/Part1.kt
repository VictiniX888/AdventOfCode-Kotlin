package aoc2019.day16

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day16/input.txt"))
        .map { it.toInt() - 48 }

    val answer = fft(input, 100).take(8).joinToString("")
    println(answer)
}

private fun fft(inputSignal: List<Int>, phases: Int): List<Int> {
    var outputSignal = inputSignal
    repeat(phases) {
        outputSignal = phase(outputSignal)
    }

    return outputSignal
}

private fun phase(inputSignal: List<Int>): List<Int> {
    return (inputSignal.indices).map { indexO ->
        var sum = 0
        // sum all the terms that should be multiplied by 1
        for (indexI in indexO until inputSignal.size step (4 * (indexO + 1))) {
            for (i in indexI .. indexI + indexO) {
                sum += inputSignal.getOrNull(i) ?: break
            }
        }

        // deduct all the terms that should be multiplied by -1
        for (indexI in (indexO + (2 * (indexO + 1))) until inputSignal.size step (4 * (indexO + 1))) {
            for (i in indexI .. indexI + indexO) {
                sum -= inputSignal.getOrNull(i) ?: break
            }
        }

        sum.absoluteValue % 10
    }
}