package aoc2019.day15

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val STATUS_WALL = 0
private const val STATUS_MOVABLE = 1
private const val STATUS_OXYGEN = 2
private const val STATUS_DEAD_END = 8
private const val STATUS_UNEXPLORED = 9

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day15/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = getShortestPath(createMap(input))
    println(answer)
}

private fun getShortestPath(map: Grid): Int {
    var steps = 0
    var currentPos = Point(0, 0)
    while (true) {
        val neighbouringPoints = getNeighbouring(map, currentPos)
        if (neighbouringPoints.any { (_, status) -> status == STATUS_OXYGEN }) {
            return steps + 1
        } else {
            map.set(STATUS_DEAD_END, currentPos)
            currentPos = currentPos.move(neighbouringPoints.first { (_, status) -> status == STATUS_MOVABLE }.first)
            steps++

        }
    }
}

private fun createMap(intcode: List<Long>): Grid {
    val grid = Grid(STATUS_UNEXPLORED)
        .apply { set(STATUS_MOVABLE, 0, 0) }
    val program = IntcodeProgram(intcode.toMutableList())
    val interpreter = IntcodeInterpreter(program)
    var currentPos = Point(0, 0)
    var currentDir = Direction.NORTH
    var currentStatus = STATUS_WALL

    while (currentStatus != STATUS_OXYGEN) {
        val neighbouringPoints = getNeighbouring(grid, currentPos)
        val unexploredPoints = neighbouringPoints.filter { (_, status) -> status == STATUS_UNEXPLORED }
        currentDir = if (unexploredPoints.isNotEmpty()) {
            unexploredPoints.first().first
        } else {
            val movablePoints = neighbouringPoints.filter { (_, status) -> status == STATUS_MOVABLE }.map { it.first }
            if (movablePoints.size == 1) {
                grid.set(STATUS_DEAD_END, currentPos)
                movablePoints.first()
            } else {
                movablePoints.filterNot { it == currentDir.getOpposite() }.first()
            }
        }

        interpreter.sendInput(currentDir.getMovementCommand())
        interpreter.runProgram()
        currentStatus = interpreter.getOutput().toInt()
        val movedPos = currentPos.move(currentDir)
        grid.set(currentStatus, movedPos)
        if (currentStatus == STATUS_MOVABLE || currentStatus == STATUS_OXYGEN) {
            currentPos = movedPos
        }
    }

    return grid
}

private fun Direction.getOpposite(): Direction =
    when (this) {
        Direction.NORTH -> Direction.SOUTH
        Direction.SOUTH -> Direction.NORTH
        Direction.WEST  -> Direction.EAST
        Direction.EAST  -> Direction.WEST
    }

private fun Direction.getMovementCommand(): Long =
    when (this) {
        Direction.NORTH -> 1L
        Direction.SOUTH -> 2L
        Direction.WEST  -> 3L
        Direction.EAST  -> 4L
    }

private fun getNeighbouring(map: Grid, currentPos: Point): List<Pair<Direction, Int>> =
    Direction.values()
        .map { Pair(it, map.get(currentPos.move(it))) }