package aoc2020.day07

import java.nio.file.Files
import java.nio.file.Paths

private const val TARGET_COLOR = "shiny gold"
private val parents = mutableSetOf<String>()

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day07/input.txt"))
        .map { it.removeSuffix(" bags.",).removeSuffix(" bag.").split(" bags contain ", " bags, ", " bag, ") }

    val answer = countParents(parseInput(input), TARGET_COLOR)
    println(answer)
}

private fun countParents(bags: Set<Bag>, color: String): Int {
    bags.filter { bag ->
        bag.contents.containsKey(color)
    }.forEach { bag ->
        val exists = !parents.add(bag.color)
        if (!exists) {
            countParents(bags, bag.color)
        }
    }

    return parents.size
}

private fun parseInput(input: List<List<String>>): Set<Bag> {
    return input.map { list ->
        Bag(list[0], list.drop(1).let { contents ->
            if (contents[0].take(2) == "no") {
                emptyMap()
            }
            else {
                contents.associate { str ->
                    str.split(" ", limit = 2).let { (count, color) -> Pair(color, count.toInt()) }
                }
            }
        })
    }.toSet()
}