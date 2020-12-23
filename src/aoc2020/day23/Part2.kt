package aoc2020.day23

import java.nio.file.Files
import java.nio.file.Paths

private const val REPEATS = 10_000_000

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day23/input.txt"))
        .map(Character::getNumericValue)

    val answer = moveCups(input)
    println(answer)
}

private fun moveCups(initCups: List<Int>, repeats: Int = REPEATS): Long {
    val cups = HashMap((initCups + (10..1_000_000)).zipWithNext().associate { (a, b) -> a to b })
    cups[1_000_000] = initCups.first()

    var currentCup = initCups.first()
    repeat(repeats) {
        var nextCup = currentCup
        val movedCups = generateSequence { cups[nextCup].also { nextCup = it!! } }.take(3).toList()
        cups[currentCup] = cups[movedCups.last()]

        val destinationCup = findDestination(currentCup, movedCups)
        cups[movedCups.last()] = cups[destinationCup]
        cups[destinationCup] = movedCups.first()

        currentCup = cups[currentCup]!!
    }

    return cups[1]!!.let { it.toLong() * cups[it]!! }
}

private fun findDestination(current: Int, moved: List<Int>): Int {
    var destination = current - 1
    if (destination == 0) destination = 1_000_000
    while (destination in moved) {
        destination--
        if (destination == 0) destination = 1_000_000
    }

    return destination
}