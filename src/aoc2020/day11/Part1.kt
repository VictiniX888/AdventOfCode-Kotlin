package aoc2020.day11

import java.lang.IndexOutOfBoundsException
import java.nio.file.Files
import java.nio.file.Paths

private val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day11/input.txt"))
        .map { it.toList() }

    val answer = simulateSeats(input)
    println(answer)
}

private fun simulateSeats(seats: List<List<Char>>): Int {
    val currentState = seats.map { it.toCharArray() }.toTypedArray()
    val prevState = Array(currentState.size) { CharArray(seats[0].size) }

    do {
        currentState.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                prevState[y][x] = c
            }
        }

        prevState.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val neighbors = countNeighbors(prevState, x, y)
                when {
                    c == 'L' && neighbors == 0 -> currentState[y][x] = '#'
                    c == '#' && neighbors >= 4 -> currentState[y][x] = 'L'
                }
            }
        }
    } while (!currentState.contentDeepEquals(prevState))

    return prevState.sumBy { row -> row.count { c -> c == '#' } }
}

private fun countNeighbors(seats: Array<CharArray>, posX: Int, posY: Int): Int {
    return directions.count { (dirX, dirY) -> hasNeighbor(seats, posX, posY, dirX, dirY) }
}

private fun hasNeighbor(seats: Array<CharArray>, posX: Int, posY: Int, dirX: Int, dirY: Int): Boolean {
    return try {
        seats[posY + dirY][posX + dirX] == '#'
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}