package aoc2020.day17

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day17/input.txt"))
        .map { line -> line.map { c -> c == '#' } }

    val answer = simulate(input, 6)
    println(answer)
}

// faster implementation, ~100ms on my machine
private fun simulate(init: List<List<Boolean>>, cycles: Int): Int {
    var active = init.flatMapIndexed { y: Int, row: List<Boolean> ->
        row.withIndex()
            .filter { (_, isActive) -> isActive }
            .map { (x, _) -> Point4D(x, y, 0, 0) }
    }.toSet()

    repeat(cycles) {
        val neighborCount = mutableMapOf<Point4D, Int>()

        active.forEach { point ->
            point.getNeighbors().forEach { neighbor ->
                neighborCount.compute(neighbor) { _, neighbors ->
                    neighbors?.plus(1) ?: 1
                }
            }
        }

        active = neighborCount.filter { (point, neighbors) ->
            neighbors == 3 || (neighbors == 2 && point in active)
        }.keys
    }

    return active.count()
}

private fun Point4D.getNeighbors(): List<Point4D> {
    val neighbors = mutableListOf<Point4D>()
    val x = this.x
    val y = this.y
    val z = this.z
    val w = this.w

    for (w1 in w-1 .. w+1) {
        for (z1 in z - 1..z + 1) {
            for (y1 in y - 1..y + 1) {
                for (x1 in x - 1..x + 1) {
                    if (!(x1 == x && y1 == y && z1 == z && w1 == w)) {
                        neighbors.add(Point4D(x1, y1, z1, w1))
                    }
                }
            }
        }
    }

    return neighbors
}

// previous implementation, ~600ms on my machine
/*
private fun simulate(initialGrid: Grid4D<Boolean>, cycles: Int): Int {
    var grid = initialGrid.deepcopy()

    repeat(cycles) {
        val newGrid = grid.deepcopy()
        for (w in grid.rangeW.first - 1..grid.rangeW.last + 1) {
            for (z in grid.rangeZ.first - 1..grid.rangeZ.last + 1) {
                for (y in grid.rangeY.first - 1..grid.rangeY.last + 1) {
                    for (x in grid.rangeX.first - 1..grid.rangeX.last + 1) {
                        val activeNeighbors = getNeighbors(x, y, z, w).count { (x1, y1, z1, w1) ->
                            grid[x1, y1, z1, w1]
                        }

                        if (grid[x, y, z, w] && activeNeighbors !in 2..3) {
                            newGrid[x, y, z, w] = false
                        } else if (!grid[x, y, z, w] && activeNeighbors == 3) {
                            newGrid[x, y, z, w] = true
                        }
                    }
                }
            }
        }
        grid = newGrid
    }

    return grid.rangeX.sumOf { x ->
        grid.rangeY.sumOf { y ->
            grid.rangeZ.sumOf { z ->
                grid.rangeW.count { w ->
                    grid[x, y, z, w]
                }
            }
        }
    }
}
 */

