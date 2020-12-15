package aoc2020.day15

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day15/input.txt"))
        .split(",")
        .map { it.toInt() }

    val answer = memoryGame(input, 30000000)
    println(answer)
}

private fun memoryGame(nums: List<Int>, limit: Int): Int {
    // initialize map
    val memory = HashMap<Int, Int>()
    nums.dropLast(1).forEachIndexed { index, num -> memory[num] = index + 1 }

    var nextNum = nums.last()
    for (i in nums.size until limit) {
        nextNum = (if (nextNum in memory) i - memory.getValue(nextNum) else 0)
            .also { memory[nextNum] = i }
    }

    return nextNum
}