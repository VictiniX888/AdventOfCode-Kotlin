package aoc2019.day08

import java.nio.file.Files
import java.nio.file.Paths

private const val WIDTH = 25
private const val HEIGHT = 6

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day08/input.txt"))

    val answer = input.splitLayers()
        .leastZeroes()
        ?.countOfOneTimesTwo()
    println(answer)
}

private fun String.countOfOneTimesTwo(): Int = this.count { it == '1' } * this.count { it == '2' }

private fun List<String>.leastZeroes(): String? = this.minBy { it.count { c -> c == '0' } }

private fun String.splitLayers(width: Int = WIDTH, height: Int = HEIGHT): List<String> = this.chunked(width * height)