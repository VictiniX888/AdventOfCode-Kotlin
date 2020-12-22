package aoc2020.day22

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val (deck1, deck2) = Files.readString(Paths.get("src/aoc2020/day22/input.txt"))
        .split("\r\n\r\n")
        .map { it.split("\r\n").drop(1).map { i -> i.toInt() } }

    val answer = playCombat(deck1, deck2).second
    println(answer)
}

// Plays a game of Combat and returns the winning player and their score
private fun playCombat(deck1: List<Int>, deck2: List<Int>): Pair<Int, Int> {
    val deck1Mut = deck1.toMutableList()
    val deck2Mut = deck2.toMutableList()

    val previousRounds = mutableSetOf<Pair<List<Int>, List<Int>>>()

    while (deck1Mut.isNotEmpty() && deck2Mut.isNotEmpty()) {

        if ((deck1Mut to deck2Mut) in previousRounds) {
            return Pair(1, calcScore(deck1Mut))
        }

        previousRounds.add(deck1Mut.toList() to deck2Mut.toList())

        val card1 = deck1Mut.removeAt(0)
        val card2 = deck2Mut.removeAt(0)

        val roundWinner = if (deck1Mut.size >= card1 && deck2Mut.size >= card2) {
            playCombat(deck1Mut.take(card1), deck2Mut.take(card2)).first
        } else {
            if (card1 > card2) 1 else 2
        }

        if (roundWinner == 1) {
            deck1Mut.add(card1)
            deck1Mut.add(card2)
        } else {
            deck2Mut.add(card2)
            deck2Mut.add(card1)
        }

    }

    if (deck1Mut.isNotEmpty()) {
        return Pair(1, calcScore(deck1Mut))
    } else {
        return Pair(2, calcScore(deck2Mut))
    }
}

private fun calcScore(deck: List<Int>): Int {
    return deck.reversed().reduceIndexed { index, acc, n -> acc + (index + 1) * n }
}