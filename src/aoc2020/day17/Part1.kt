package aoc2020.day17

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day17/input.txt"))
        .map { line -> line.map { c -> c == '#' } }
    val grid = Grid3D(listOf(input), false)

    val answer = simulate(grid, 6)
    println(answer)
}

private fun simulate(initialGrid: Grid3D<Boolean>, cycles: Int): Int {
    var grid = initialGrid.deepcopy()

    repeat(cycles) {
        val newGrid = grid.deepcopy()
        for (z in grid.rangeZ.first - 1..grid.rangeZ.last + 1) {
            for (y in grid.rangeY.first - 1..grid.rangeY.last + 1) {
                for (x in grid.rangeX.first - 1..grid.rangeX.last + 1) {
                    val activeNeighbors = getNeighbors(x, y, z).count { (x1, y1, z1) ->
                        grid[x1, y1, z1]
                    }

                    if (grid[x, y, z] && activeNeighbors !in 2..3) {
                        newGrid[x, y, z] = false
                    } else if (!grid[x, y, z] && activeNeighbors == 3) {
                        newGrid[x, y, z] = true
                    }
                }
            }
        }
        grid = newGrid
    }

    return grid.rangeX.sumOf { x ->
        grid.rangeY.sumOf { y ->
            grid.rangeZ.count { z ->
                grid[x, y, z]
            }
        }
    }
}

private fun getNeighbors(x: Int, y: Int, z: Int): List<Triple<Int, Int, Int>> {
    val neighbors = mutableListOf<Triple<Int, Int, Int>>()

    for (z1 in z-1 .. z+1) {
        for (y1 in y-1 .. y+1) {
            for (x1 in x-1 .. x+1) {
                if (!(x1 == x && y1 == y && z1 == z)) {
                    neighbors.add(Triple(x1, y1, z1))
                }
            }
        }
    }

    return neighbors
}