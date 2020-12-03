package aoc2020.day03

import java.nio.file.Files
import java.nio.file.Paths

private val SLOPES = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day03/input.txt"))

    val answer = checkAllSlopes(SLOPES, input)
    println(answer)
}

private fun checkAllSlopes(slopes: List<Pair<Int, Int>>, map: List<String>): Int {
    return slopes.map { countCollisions(it, map) }.reduce { acc, num -> acc * num }
}

private fun countCollisions(slope: Pair<Int, Int>, map: List<String>): Int {
    val width = map[0].length
    val (slopeX, slopeY) = slope
    var x = 0
    var collisions = 0

    for (y in slopeY until map.size step slopeY) {
        x = (x + slopeX) % width
        if (map[y][x] == '#') {
            collisions++
        }
    }

    return collisions
}