package aoc2019.day22

import java.nio.file.Files
import java.nio.file.Paths

private const val DECK_SIZE = 10007

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day22/input.txt"))
        .map { it.split(" ") }
    
    val answer = shuffle(input).findCard(2019)
    println(answer)
}

private fun List<Int>.findCard(n: Int): Int {
    return this.indexOf(n)
}

private fun shuffle(techniques: List<List<String>>, deckSize: Int = DECK_SIZE): List<Int> {
    return techniques.fold((0 until deckSize).toList()) { acc, technique -> 
        when (technique[0]) {
            "deal" -> when (technique[2]) {
                "new" -> acc.dealNew()
                "increment" -> acc.dealIncrement(technique[3].toInt())
                else -> error("Technique $technique not found")
            }
            
            "cut" -> acc.cut(technique[1].toInt())
            
            else -> error("Technique $technique not found")
        }
    }
}

private fun List<Int>.dealNew(): List<Int> {
    return this.reversed()
}

private fun List<Int>.cut(n: Int): List<Int> {
    return if (n >= 0) {
        this.slice(n until this.size) + this.slice(0 until n)
    } else {
        this.slice(this.size+n until this.size) + this.slice(0 until this.size+n)
    }
}

private fun List<Int>.dealIncrement(n: Int): List<Int> {
    val newList = MutableList(this.size) { -1 }
    var iNew = 0
    repeat(this.size) { iOld ->
        newList[iNew] = this[iOld]
        iNew += n
        iNew %= this.size
    }
    
    return newList
}