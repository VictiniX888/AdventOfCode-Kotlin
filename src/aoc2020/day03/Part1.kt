package aoc2020.day03

import java.nio.file.Files
import java.nio.file.Paths

private val SLOPE = Pair(3, 1)

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day03/input.txt"))

    val answer = countCollisions(SLOPE, input)
    println(answer)
}

private fun countCollisions(slope: Pair<Int, Int>, map: List<String>): Int {
    val width = map[0].length
    val (slopeX, slopeY) = slope
    var x = 0
    var collisions = 0

    for (y in 1 until map.size step slopeY) {
        x = (x + slopeX) % width
        if (map[y][x] == '#') {
            collisions++
        }
    }

    return collisions
}