package aoc2019.day11

import aoc2019.day11.Direction.*
import aoc2019.day11.Rotation.*
import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day11/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = moveRobot(input).count()
    println(answer)
}

private fun moveRobot(instructions: List<Long>): Set<Point> { // returns the Set of painted Points
    val program = IntcodeProgram(instructions.toMutableList())
    val interpreter = IntcodeInterpreter(program)

    val painted = mutableSetOf<Point>()

    val grid = Grid()
    var currentPoint = Point(0, 0)
    var currentDirection = UP

    while (!interpreter.hasTerminatedSuccessfully) {
        val input = grid.get(currentPoint)
        interpreter.sendInput(input.toLong())
        interpreter.runProgram()

        val color = interpreter.getOutput()
        grid.set(color.toInt(), currentPoint)
        painted.add(currentPoint)

        val turning = interpreter.getOutput()
        currentDirection = turn(currentDirection, if (turning.toInt() == 0) ANTICLOCKWISE else CLOCKWISE)
        currentPoint = currentPoint.move(currentDirection)
    }

    return painted
}



private fun turn(facing: Direction, turning: Rotation): Direction =
    when (facing) {
        UP    -> if (turning == CLOCKWISE) RIGHT else LEFT
        DOWN  -> if (turning == CLOCKWISE) LEFT  else RIGHT
        LEFT  -> if (turning == CLOCKWISE) UP    else DOWN
        RIGHT -> if (turning == CLOCKWISE) DOWN  else UP
    }