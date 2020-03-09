package aoc2019.day01

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files
        .readAllLines(Paths.get("src/aoc2019/day01/input.txt"))
        .map { it.toInt() }

    val answer = totalFuelNeeded(input)
    println(answer)
}

private fun totalFuelNeeded(masses: List<Int>): Int = masses.sumBy { fuelNeeded(it) }

private fun fuelNeeded(mass: Int): Int = (mass / 3) - 2