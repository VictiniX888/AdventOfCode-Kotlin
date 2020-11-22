package aoc2019.day17

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get("src/aoc2019/day17/input.txt"))
        .split(",")
        .map { it.toLong() }

    val map = createMap(input)
    val answer = calibrate(map)
    println(answer)
}

private fun calibrate(grid: Grid): Int {
    val pos = grid.pointOf(60)
        ?: grid.pointOf(62)
        ?: grid.pointOf(94)
        ?: grid.pointOf(118)
        ?: error("Vacuum robot not found")

    val traversed = mutableListOf(pos.copy())
    val intersections = mutableListOf<MutablePoint>()
    var currentDirection: Direction?
    // get initial direction
    currentDirection = when {
        grid.get(pos.x + 1, pos.y) == 35 -> Direction.EAST
        grid.get(pos.x - 1, pos.y) == 35 -> Direction.WEST
        grid.get(pos.x, pos.y + 1) == 35 -> Direction.NORTH
        grid.get(pos.x, pos.y - 1) == 35 -> Direction.SOUTH
        else -> error("Starting direction not found")
    }

    while (currentDirection != null) {
        pos.move(currentDirection)
        if (traversed.contains(pos)) {
            intersections.add(pos.copy())
        } else {
            traversed.add(pos.copy())
        }
        currentDirection = findNextMove(grid, pos, currentDirection, 35)
    }

    return intersections.sumBy { it.x * it.y }
}

private fun findNextMove(grid: Grid, pos: MutablePoint, direction: Direction, target: Int): Direction? {
    if (grid.get(pos.copy().apply { move(direction) }) == target) {
        return direction
    } else if (grid.get(pos.copy().apply { move(Direction.values()[(direction.ordinal + 1) % 4]) }) == target) {
        return Direction.values()[(direction.ordinal + 1) % 4]
    } else if (grid.get(pos.copy().apply { move(Direction.values()[(direction.ordinal + 3) % 4]) }) == target) {
        return Direction.values()[(direction.ordinal + 3) % 4]
    } else {
        return null
    }
}

private fun createMap(intcode: List<Long>): Grid {
    val grid = Grid(0)
    val pos = MutablePoint(0,0)
    val program = IntcodeProgram(intcode.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    val output = interpreter.runProgram()
    output.forEach { item ->
        if (item == 10L) {
            pos.y++
            pos.x = 0
        } else {
            grid.set(item.toInt(), pos)
            pos.x++
        }
    }

    return grid
}