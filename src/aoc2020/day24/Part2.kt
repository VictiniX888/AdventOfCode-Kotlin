package aoc2020.day24

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day24/input.txt"))

    val answer = flipTiles(populateHexGrid(input), 100)
    println(answer)
}

private fun flipTiles(blackTilesInit: HashSet<Point>, times: Int): Int {

    var blackTiles = HashSet(blackTilesInit)

    repeat(times) {
        val adjacentBlackMap = HashMap<Point, Int>()
        blackTiles.forEach { tile ->
            tile.neighbors().forEach { neighbor ->
                adjacentBlackMap.compute(neighbor) { _, i ->
                    if (i == null) {
                        1
                    } else {
                        i+1
                    }
                }
            }
        }

        val newBlackTiles = HashSet(blackTiles)
        blackTiles.forEach { point ->
            if (!point.neighbors().any { it in blackTiles }) {
                newBlackTiles.remove(point)
            }
        }

        adjacentBlackMap.forEach { (point, neighbors) ->
            if (point in blackTiles) {
                if (neighbors > 2) {
                    newBlackTiles.remove(point)
                }
            }
            else {
                if (neighbors == 2) {
                    newBlackTiles.add(point)
                }
            }
        }

        blackTiles = newBlackTiles
    }

    return blackTiles.count()
}

private fun Point.neighbors(): List<Point> {
    return listOf(Point(x+1, y), Point(x, y-1), Point(x-1, y-1), Point(x-1, y), Point(x, y+1), Point(x+1, y+1))
}

private fun populateHexGrid(instructions: List<String>): HashSet<Point> {
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

    return blackTiles
}