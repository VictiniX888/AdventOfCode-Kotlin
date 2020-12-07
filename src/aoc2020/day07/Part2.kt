package aoc2020.day07

import java.nio.file.Files
import java.nio.file.Paths

private const val TARGET_COLOR = "shiny gold"

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day07/input.txt"))
        .map { it.removeSuffix(" bags.",).removeSuffix(" bag.").split(" bags contain ", " bags, ", " bag, ") }

    val answer = countChildren(parseInput(input), TARGET_COLOR)
    println(answer)
}

private fun countChildren(bags: Set<Bag>, color: String): Int {
    return bags.filter { bag ->
        bag.color == color
    }.sumBy { (_, children) ->
        children.map { (childColor, childCount) -> childCount + childCount * countChildren(bags, childColor) }.sum()
    }
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