package aoc2020.day06

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day06/input.txt"))
        .split("\r\n\r\n")
        .map { it.split("\r\n") }

    val answer = input.sumBy { countQuestions(it) }
    println(answer)
}

private fun countQuestions(group: List<String>): Int {
    return group.flatMap { it.toList() }
        .distinct()
        .size
}