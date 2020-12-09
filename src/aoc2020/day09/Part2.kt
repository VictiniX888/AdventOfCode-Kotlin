package aoc2020.day09

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private const val PREAMBLE_LENGTH = 25

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day09/input.txt"))
        .map { it.toLong() }

    val answer = calcEncryptionWeakness(input)
    println(answer)
}

private fun calcEncryptionWeakness(nums: List<Long>, preambleLength: Int = PREAMBLE_LENGTH): Long {
    val exception = findException(nums, preambleLength)

    var acc = 0L
    val contiguous: Queue<Long> = LinkedList()
    for (num in nums) {
        acc += num
        contiguous.add(num)

        while (acc > exception) {
            acc -= contiguous.remove()
        }

        if (acc == exception) {
            val contiguousMax = contiguous.maxOrNull()
            val contiguousMin = contiguous.minOrNull()
            return if (contiguousMax != null && contiguousMin != null) {
                contiguousMax + contiguousMin
            } else {
                -1
            }
        }
    }

    error("No encryption weakness found")
}

private fun findException(nums: List<Long>, preambleLength: Int): Long {
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
