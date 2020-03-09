package aoc2019.day01

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files
        .readAllLines(Paths.get("src/aoc2019/day01/input.txt"))
        .map { it.toInt() }

    val answer = sumOfFuelNeeded(input)
    println(answer)
}

private fun sumOfFuelNeeded(masses: List<Int>): Int = masses.sumBy { fuelNeeded(it) }

private tailrec fun fuelNeeded(mass: Int, totalFuelNeeded: Int = 0): Int {

    val additionalFuelNeeded = (mass / 3) - 2

    return if (additionalFuelNeeded <= 0) totalFuelNeeded
    else fuelNeeded(additionalFuelNeeded, totalFuelNeeded + additionalFuelNeeded)
}