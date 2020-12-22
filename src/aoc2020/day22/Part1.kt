package aoc2020.day22

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val (deck1, deck2) = Files.readString(Paths.get("src/aoc2020/day22/input.txt"))
        .split("\r\n\r\n")
        .map { it.split("\r\n").drop(1).map { i -> i.toInt() } }

    val answer = playCombat(deck1, deck2)
    println(answer)
}

private fun playCombat(deck1: List<Int>, deck2: List<Int>): Int {
    val deck1Mut = deck1.toMutableList()
    val deck2Mut = deck2.toMutableList()

    while (deck1Mut.isNotEmpty() && deck2Mut.isNotEmpty()) {
        if (deck1Mut.first() > deck2Mut.first()) {
            deck1Mut.add(deck1Mut.first())
            deck1Mut.add(deck2Mut.first())
        } else {    // Assumes all cards are unique
            deck2Mut.add(deck2Mut.first())
            deck2Mut.add(deck1Mut.first())
        }

        deck1Mut.removeAt(0)
        deck2Mut.removeAt(0)
    }

    if (deck1Mut.isNotEmpty()) {
        return deck1Mut.reversed().reduceIndexed { index, acc, n -> acc + (index + 1) * n }
    } else {
        return deck2Mut.reversed().reduceIndexed { index, acc, n -> acc + (index + 1) * n }
    }
}