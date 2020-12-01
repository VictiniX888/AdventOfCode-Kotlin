package aoc2020.day01

import java.nio.file.Files
import java.nio.file.Paths

private const val TARGET_SUM = 2020

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day01/input.txt"))
        .map { it.toInt() }

    val sumTriple = findSumTriple(TARGET_SUM, input) ?: error("Sum triple not found")
    val answer = sumTriple.first * sumTriple.second * sumTriple.third
    println(answer)
}

private fun findSumTriple(target: Int, nums: List<Int>): Triple<Int, Int, Int>? {
    val sortedNumsDescending = nums.sortedDescending()

    for (i in nums.indices) {
        for (j in nums.size - 1 downTo i + 1) {
            for (k in nums.size - 1 downTo j + 1) {
                if (sortedNumsDescending[i] + sortedNumsDescending[j] + sortedNumsDescending[k] == target) {
                    return Triple(sortedNumsDescending[i], sortedNumsDescending[j], sortedNumsDescending[k])
                } else if (sortedNumsDescending[i] + sortedNumsDescending[j] + sortedNumsDescending[k] > target) {
                    break
                }
            }
        }
    }

    return null
}