package aoc2020.day13

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.max
import kotlin.math.min

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day13/input.txt"))
    val buses = input[1].split(",").map { i -> i.toLongOrNull() ?: -1L }

    val answer = findEarliestTimestamp(buses)
    println(answer)
}

private fun findEarliestTimestamp(buses: List<Long>): Long {
    var minTimestamp = buses.first().toLong()
    var lcm = minTimestamp
    var gap = 1L
    for (busID in buses.drop(1)) {
        if (busID != -1L) {
            while ((minTimestamp + gap) % busID != 0L) {
                minTimestamp += lcm
            }

            lcm = lcm(lcm, busID)
        }

        gap++
    }

    return minTimestamp
}

private fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

private fun gcd(a: Long, b: Long): Long {
    var ax = max(a, b)
    var bx = min(a, b)
    while (bx != 0L) {
        bx = ax % bx.also { ax = bx }
    }

    return ax
}