package aoc2019.day15

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

private const val STATUS_MOVABLE = 1
private const val STATUS_OXYGEN = 2
private const val STATUS_DEAD_END = 8
private const val STATUS_UNEXPLORED = 9

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day15/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = timeTaken(createMap(input))
    println(answer)
}

private fun timeTaken(map: Grid): Int {
    var oxygen = listOf(map.pointOf(STATUS_OXYGEN))
    var time = 0
    while (true) {
        val newOxygen = mutableListOf<Point>()
        oxygen.forEach {
            val neighbouringPoints = getNeighbouring(map, it)
            newOxygen.addAll(neighbouringPoints.filter { (_, status) -> status == STATUS_DEAD_END }
                .map { (dir, _) -> it.move(dir) }
                .also { _ -> map.set(STATUS_OXYGEN, it) })
        }

        if (newOxygen.isEmpty()) {
            return time
        } else {
            oxygen = newOxygen
            time++
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
    var currentStatus: Int

    while (true) {
        val neighbouringPoints = getNeighbouring(grid, currentPos)
        val unexploredPoints = neighbouringPoints.filter { (_, status) -> status == STATUS_UNEXPLORED }
        currentDir = if (unexploredPoints.isNotEmpty()) {
            unexploredPoints.first().first
        } else {
            val movablePoints = neighbouringPoints.filter { (_, status) -> status == STATUS_MOVABLE }.map { it.first }
            if (movablePoints.isEmpty()) {
                grid.set(STATUS_DEAD_END, currentPos)
                return grid
            }
            if (movablePoints.size == 1) {
                if (grid.get(currentPos) != STATUS_OXYGEN) {
                    grid.set(STATUS_DEAD_END, currentPos)
                }
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