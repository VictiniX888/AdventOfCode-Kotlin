package aoc2019.day17

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

/*
    Requires manual input. For my particular case, input, on separate lines:
    A,B,A,C,A,B,A,C,B,C
    R,4,L,12,L,8,R,4
    L,8,R,10,R,10,R,6
    R,4,R,10,L,12

    This changes based on the puzzle input.
 */

fun main() {
    val input = Files.readString(Paths.get("src/aoc2019/day17/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = runIntcode(input)
    println(answer)
}

private fun generatePath(grid: Grid): Pair<List<Char>, List<Int>> {
    val directionList = mutableListOf<Char>()
    val stepList = mutableListOf<Int>()
    var currSteps = 0

    var prevDirection = Direction.NORTH
    val pos = grid.pointOf('^'.toInt())
        ?: grid.pointOf('v'.toInt()).also { prevDirection = Direction.SOUTH }
        ?: grid.pointOf('<'.toInt()).also { prevDirection = Direction.WEST }
        ?: grid.pointOf('>'.toInt()).also { prevDirection = Direction.EAST }
        ?: error("Vacuum robot not found")

    var currentDirection: Direction?
    // get next direction
    currentDirection = when {
        grid.get(pos.x + 1, pos.y) == 35 -> Direction.EAST
        grid.get(pos.x - 1, pos.y) == 35 -> Direction.WEST
        grid.get(pos.x, pos.y + 1) == 35 -> Direction.NORTH
        grid.get(pos.x, pos.y - 1) == 35 -> Direction.SOUTH
        else -> error("Starting direction not found")
    }
    directionList.add(calcTurnDirection(prevDirection, currentDirection))
    prevDirection = currentDirection

    while (currentDirection != null) {
        if (currentDirection != prevDirection) {
            stepList.add(currSteps)
            currSteps = 0
            directionList.add(calcTurnDirection(prevDirection, currentDirection))
        }

        pos.move(currentDirection)
        currSteps++
        prevDirection = currentDirection
        currentDirection = findNextMove(grid, pos, currentDirection, 35)

    }

    stepList.add(currSteps)

    return Pair(directionList, stepList)
}

private fun calcTurnDirection(prevDirection: Direction, currDirection: Direction): Char {
    if (Direction.values()[(prevDirection.ordinal + 1) % 4] == currDirection) {
        return 'L'
    } else if (Direction.values()[(currDirection.ordinal + 1) % 4] == prevDirection) {
        return 'R'
    } else {
        return 'X'
    }
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

private fun runIntcode(intcode: List<Long>): Long {
    val program = IntcodeProgram(intcode.toMutableList())
    program.instructions[0] = 2
    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()
    val mapOutput = interpreter.getAllOutputs()
    val map = createMap(mapOutput)

    map.printAsChars()
    val (dirs, steps) = generatePath(map)
    dirs.zip(steps).forEach { print("${it.first},${it.second},") }
    print("\n")

    repeat(4) {
        var input = readLine()
        while (input == null) {
            input = readLine()
        }
        interpreter.sendInput(*input.map { it.toLong() }.toLongArray())
        interpreter.sendInput('\n'.toLong())

        interpreter.runProgram()
        interpreter.getAllOutputs().forEach { print(it.toChar()) }
    }

    interpreter.sendInput('n'.toLong(), '\n'.toLong())
    interpreter.runProgram()
    println(interpreter.hasTerminatedSuccessfully)

    val finalOutput = interpreter.getAllOutputs()
    finalOutput.dropLast(1).forEach { print(it.toChar()) }
    print("\n")

    return finalOutput.last()
}

private fun createMap(input: List<Long>): Grid {
    val grid = Grid(0)
    val pos = MutablePoint(0,0)

    input.forEach { item ->
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