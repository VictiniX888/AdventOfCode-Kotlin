package aoc2019.day16

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day16/input.txt"))
        .map { it.toInt() - 48 }

    val inputRepeated = input.toMutableList()
    repeat(9999) {
        inputRepeated += input
    }
    val inputCut = inputRepeated.drop(inputRepeated.take(7).joinToString("").toInt())

    val answer = fft(inputCut, 100).take(8).joinToString("")
    println(answer)
}

private fun fft(inputSignal: List<Int>, phases: Int): List<Int> {
    var outputSignal = inputSignal
    repeat(phases) {
        outputSignal = phase(outputSignal.toMutableList())
    }

    return outputSignal
}

private fun phase(inputSignal: MutableList<Int>): List<Int> {
    for (i in (inputSignal.size - 2) downTo 0) {
        inputSignal[i] = (inputSignal[i] + inputSignal[i+1]) % 10
    }

    return inputSignal
}