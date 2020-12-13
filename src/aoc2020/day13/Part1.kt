package aoc2020.day13

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day13/input.txt"))
    val (time, buses) = input.map { it.split(",").mapNotNull { i -> i.toIntOrNull() } }

    val answer = findEarliestBus(time.first(), buses)
    println(answer)
}

private fun findEarliestBus(time: Int, buses: List<Int>): Int {
    return buses.map { interval -> Pair(((time / interval) + 1) * interval - time, interval) }
        .minByOrNull { (first, _) -> first }
        ?.let { (first, second) -> first * second } ?: -1
}