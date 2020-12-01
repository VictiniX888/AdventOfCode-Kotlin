package aoc2020.day01

import java.nio.file.Files
import java.nio.file.Paths

private const val TARGET_SUM = 2020

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day01/input.txt"))
        .map { it.toInt() }

    val sumPair = findSumPair(TARGET_SUM, input) ?: error("Sum pair not found")
    val answer = sumPair.first * sumPair.second
    println(answer)
}

private fun findSumPair(target: Int, nums: List<Int>): Pair<Int, Int>? {
    val sortedNumsDescending = nums.sortedDescending()

    for (i in nums.indices) {
        for (j in nums.size - 1 downTo i + 1) {
            if (sortedNumsDescending[i] + sortedNumsDescending[j] == target) {
                return Pair(sortedNumsDescending[i], sortedNumsDescending[j])
            } else if (sortedNumsDescending[i] + sortedNumsDescending[j] > target) {
                break
            }
        }
    }

    return null
}