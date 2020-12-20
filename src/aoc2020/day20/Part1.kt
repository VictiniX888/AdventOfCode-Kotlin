package aoc2020.day20

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day20/input.txt"))
        .trimEnd()
        .split("\r\n\r\n")
        .map { it.split("\r\n") }
        .map { Tile(it.first().split(" ").last().dropLast(1).toLong(), it.drop(1).map { s -> s.map { c -> c == '#' } }) }

    val answer = multiplyCorners(arrangeTiles(input))
    println(answer)
}

private fun multiplyCorners(image: Array<Array<Tile>>): Long {
    return image.first().first().id * image.first().last().id * image.last().first().id * image.last().last().id
}

private fun arrangeTiles(tiles: List<Tile>): Array<Array<Tile>> {
    val allTiles = tiles.associateBy { it.id }
    val checkedTiles = mutableListOf<Tile>()
    val remainingTiles = tiles.toMutableList()

    val tileStack = Stack<Tile>()
    tileStack.push(tiles.first())

    // first pass: assign connections to each tile
    while (tileStack.isNotEmpty()) {
        val currentTile = tileStack.pop()
        remainingTiles -= currentTile
        val currentBorders = currentTile.getAllBorders()

        remainingTiles.forEach { tile ->
            tile.getAllBorders().forEachIndexed { iOther, border ->
                if (border in currentBorders) {
                    val iCurrent = currentBorders.indexOf(border)
                    val rotations = ((iCurrent + 4) - iOther.opposite()) % 4
                    if (currentTile.getConnection(iCurrent) == null) {
                        allTiles[tile.id]!!.rotateRight(rotations)
                        if (iCurrent == 0 || iCurrent == 2) allTiles[tile.id]!!.flipHori()
                        else allTiles[tile.id]!!.flipVert()
                        currentTile.setConnection(iCurrent, tile.id)
                        tile.setConnection(iCurrent.opposite(), currentTile.id)
                        tileStack.push(tile)
                    }
                } else if (border.reversed() in currentBorders) {
                    val iCurrent = currentBorders.indexOf(border.reversed())
                    val rotations = ((iCurrent + 4) - iOther.opposite()) % 4
                    if (currentTile.getConnection(iCurrent) == null) {
                        allTiles[tile.id]!!.rotateRight(rotations)
                        currentTile.setConnection(iCurrent, tile.id)
                        tile.setConnection(iCurrent.opposite(), currentTile.id)
                        tileStack.push(tile)
                    }
                }
            }
        }

        checkedTiles += currentTile
    }

    // second pass: generate a 2d array of tiles using the connections determined
    val image = Array(sqrt(tiles.size.toDouble()).roundToInt()) { Array<Tile>(sqrt(tiles.size.toDouble()).roundToInt()) { Tile(-1L, listOf()) } }
    var left: Tile? = allTiles.values.find { it.left == null && it.top == null } ?: error("Top left corner tile not found")

    for (y in image.indices) {
        var right: Tile? = left!!
        for (x in image[y].indices) {
            image[y][x] = right!!
            right = allTiles[right.right]
        }
        left = allTiles[left.bottom]
    }

    return image
}

private fun Int.opposite(): Int {
    return when (this) {
        0 -> 2
        1 -> 3
        2 -> 0
        3 -> 1
        else -> error("Cannot find opposite of $this")
    }
}