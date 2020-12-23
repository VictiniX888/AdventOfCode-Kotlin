package aoc2020.day23

import java.nio.file.Files
import java.nio.file.Paths

private const val REPEATS = 100

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day23/input.txt"))
        .map(Character::getNumericValue)

    val answer = moveCups(input)
    println(answer)
}

private fun moveCups(cups: List<Int>, times: Int = REPEATS): String {
    val highest = cups.maxOrNull() ?: error("Cannot find highest cup")
    val lowest = cups.minOrNull() ?: error("Cannot find lowest cup")

    val cupsMut = cups.toMutableList()
    var current = cupsMut.first()

    repeat(times) {
        val indexTake = cupsMut.indexOf(current)
        val moved = if (indexTake+3 < cupsMut.size) {
            cupsMut.slice(indexTake+1..indexTake+3)
        } else {
            val left = 3 - (cupsMut.size - indexTake - 1)
            cupsMut.slice(indexTake+1 until cupsMut.size) + cupsMut.slice(0 until left)
        }
        repeat(moved.size) {
            if (indexTake+1 < cupsMut.size) {
                cupsMut.removeAt(indexTake + 1)
            } else {
                cupsMut.removeAt(0)
            }
        }

        var destination = if (current == lowest) highest else current - 1
        while (destination !in cupsMut) {
            destination = if (destination == lowest) highest else destination - 1
        }
        val indexPut = cupsMut.indexOf(destination)
        cupsMut.addAll(indexPut+1, moved)

        current = cupsMut[(cupsMut.indexOf(current) + 1) % cupsMut.size]
    }

    return cupsMut.joinToString("").split('1').let { (a, b) -> b + a }
}