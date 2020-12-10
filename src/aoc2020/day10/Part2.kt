package aoc2020.day10

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day10/input.txt"))
        .map { it.toInt() }

    val answer = countArrangementsDP(input)
    println(answer)
}

// dp implementation, ~40ms on my machine
private fun countArrangementsDP(adapters: List<Int>): Long {
    val cache = HashMap<Int, Long>()

    val adaptersDescending = adapters.sortedDescending().plus(0)
    cache[adaptersDescending.first()] = 1
    adaptersDescending.drop(1).forEach { jolts ->
        var arrangements: Long = 0
        for (i in jolts+1..jolts+3) {
            arrangements += cache[i] ?: 0
        }

        cache[jolts] = arrangements
    }

    return cache[0] ?: -1
}

// recursive implementation, ~40ms on my machine
private fun countArrangementsRecursion(adapters: List<Int>): Long {
    val adaptersSorted = adapters.sortedDescending().plus(0).reversed()
    var index = 0

    val cache = HashMap<Int, Long>()
    cache[adaptersSorted.size-1] = 1L

    val recursiveStack = Stack<Int>()
    recursiveStack.push(0)

    val path = Stack<Int>()

    while (recursiveStack.isNotEmpty()) {
        index = recursiveStack.pop()

        val newArrangements = cache[index]
        if (newArrangements != null) {
            var parent = path.peek()
            if (parent != 0) {
                if (parent == index) {
                    path.pop()
                    parent = path.peek()
                }

                cache.computeIfPresent(parent) { _, value -> value + newArrangements }
            }
        } else {
            recursiveStack.push(index)
            path.push(index)

            cache[index] = 0

            for (i in index+1 until adaptersSorted.size) {
                if (adaptersSorted[i] - adaptersSorted[index] > 3) {
                    break
                } else {
                    recursiveStack.push(i)
                }
            }
        }
    }

    return cache[0] ?: -1
}