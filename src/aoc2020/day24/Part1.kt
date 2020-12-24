package aoc2020.day24

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day24/input.txt"))

    val answer = populateHexGrid(input)
    println(answer)
}

private fun populateHexGrid(instructions: List<String>): Int {
    val blackTiles = HashSet<Point>()

    instructions.forEach { instruction ->
        var currentPos = Point(0, 0)
        var i = 0
        while (i in instruction.indices) {
            when (instruction[i]) {
                'e' -> currentPos = currentPos.move(Direction.EAST)
                'w' -> currentPos = currentPos.move(Direction.WEST)

                's' -> {
                    i++
                    when (instruction[i]) {
                        'e' -> currentPos = currentPos.move(Direction.SOUTHEAST)
                        'w' -> currentPos = currentPos.move(Direction.SOUTHWEST)
                    }
                }

                'n' -> {
                    i++
                    when (instruction[i]) {
                        'e' -> currentPos = currentPos.move(Direction.NORTHEAST)
                        'w' -> currentPos = currentPos.move(Direction.NORTHWEST)
                    }
                }
            }

            i++
        }

        if (currentPos in blackTiles) {
            blackTiles.remove(currentPos)
        } else {
            blackTiles.add(currentPos)
        }
    }

    return blackTiles.count()
}