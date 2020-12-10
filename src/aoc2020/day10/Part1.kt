package aoc2020.day10

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day10/input.txt"))
        .map { it.toInt() }

    val answer = calcDiff(input)
    println(answer)
}

private fun calcDiff(adapters: List<Int>): Int {
    return adapters.sortedDescending()
        .plus(0)
        .reversed()
        .let { it.plus(it.last() + 3) }
        .zipWithNext()
        .fold(Pair(0, 0)) { acc, (first, second) ->
            if (second - first == 1) acc.copy(acc.first + 1)
            else if (second - first == 3) acc.copy(second = acc.second + 1)
            else acc.copy()
        }.let { (first, second) -> first * second }
}