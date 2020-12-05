package aoc2020.day05

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day05/input.txt"))

    val answer = findHighestSeat(input)
    println(answer)
}

private fun findHighestSeat(boardingPasses: List<String>): Int {
    return boardingPasses.maxOf { calcSeatID(it) }
}

private fun calcSeatID(boardingPass: String): Int {
    var row = 0
    var column = 0

    for (i in 0..6) {
        if (boardingPass[i] == 'B') {
            row += 2.0.pow(6 - i).toInt()
        }
    }

    for (i in 7..9) {
        if (boardingPass[i] == 'R') {
            column += 2.0.pow(9 - i).toInt()
        }
    }

    return row * 8 + column
}