package aoc2020.day18

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day18/input.txt"))
        .map { it.filterNot { c -> c == ' ' }.toList() }

    val answer = evalAll(input)
    println(answer)
}

private fun evalAll(expressions: List<List<Char>>): Long {
    return expressions.sumOf { ExpressionTreeBasic(it).eval() }
}