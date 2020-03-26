package aoc2019.day08

import java.nio.file.Files
import java.nio.file.Paths

private const val WIDTH = 25
private const val HEIGHT = 6

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day08/input.txt"))

    val answer = displayOutput(parsePixels(input))
    answer.forEach {
        println(it)
    }
}

private fun displayOutput(pixels: List<Char>) = pixels.map {
        when (it) {
            '1' -> '.'
            else -> ' '
        }
    }.chunked(WIDTH)
    .map { it.joinToString("") }

private fun parsePixels(digits: String) = digits.withIndex()
    .groupBy { it.index % (WIDTH * HEIGHT) }
    .values
    .map { it.map { indexedValue -> indexedValue.value } }
    .map { getColor(it) }

private fun getColor(layers: List<Char>) = layers.firstOrNull { c -> c == '0' || c == '1' } ?: '2'