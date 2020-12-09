package aoc2020.day09

import java.nio.file.Files
import java.nio.file.Paths

private const val PREAMBLE_LENGTH = 25

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day09/input.txt"))
        .map { it.toLong() }

    val answer = findException(input)
    println(answer)
}

private fun findException(nums: List<Long>, preambleLength: Int = PREAMBLE_LENGTH): Long {
    val complements = HashSet<Long>()

    return nums.windowed(preambleLength)
        .zip(nums.drop(preambleLength))
        .first { (il, sum) ->
            il.map { i -> if (i in complements) true else !complements.add(sum - i) }
                .reduce { acc, bool -> acc || bool }
                .not()
                .also { complements.clear() }
        }.second
}
