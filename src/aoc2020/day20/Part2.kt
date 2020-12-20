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

    val answer = calcWaterRoughness(arrangeTiles(input))
    println(answer)
}

private fun calcWaterRoughness(image: Tile): Int {
    val monsters = findAllSeaMonsters(image)
    if (monsters != 0) {
        return image.grid.sumOf { r -> r.count { it } } - monsters*15
    } else {
        return -1
    }
}

private fun findAllSeaMonsters(image: Tile): Int {
    repeat(4) {
        countSeaMonsters(image).let { if (it != 0) return it }

        image.flipVert()
        countSeaMonsters(image).let { if (it != 0) return it }

        image.flipHori()
        countSeaMonsters(image).let { if (it != 0) return it }

        image.flipVert()
        countSeaMonsters(image).let { if (it != 0) return it }

        image.flipHori()

        image.rotateRight()
    }

    return 0
}

private fun countSeaMonsters(image: Tile): Int {
    var count = 0
    for (y in 1 until image.grid.size-1) {
        for (x in 0 until image.grid.size-19) {
            if (isSeaMonster(image, x, y)) count++
        }
    }

    return count
}

private fun isSeaMonster(image: Tile, x: Int, y: Int): Boolean {
    return if (image[x, y]) {
        (image[x+18, y-1] && image[x+5, y] && image[x+6, y] && image[x+11, y] && image[x+12, y] && image[x+17, y] && image[x+18, y] && image[x+19, y] && image[x+1, y+1] && image[x+4, y+1] && image[x+7, y+1] && image[x+10, y+1] && image[x+13, y+1] && image[x+16, y+1])
    } else false
}

private fun arrangeTiles(tiles: List<Tile>): Tile {
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

    return Tile(0, image.map { row ->
        row.map { tile ->
            tile.removeBorder()
            tile.grid
        }.flatMap { it.mapIndexed { i, list -> IndexedValue(i, list) } }
            .groupBy({ (i, _) -> i }, { (_, v) -> v })
            .map { (_, v) -> v.reduce { acc, list -> acc + list } }
    }.reduce { acc, list -> acc + list })
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