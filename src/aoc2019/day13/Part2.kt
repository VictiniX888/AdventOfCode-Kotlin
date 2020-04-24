package aoc2019.day13

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day13/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = startGame(input)
    println(answer)
}

private fun countBlocks(grid: Grid): Int = grid.gridList.count { it == 2 }

private fun startGame(intcode: List<Long>): Int {
    val program = IntcodeProgram(intcode.toMutableList().apply { set(0, 2L) })
    val interpreter = IntcodeInterpreter(program)
    interpreter.runProgram()
    val screen = interpreter.getAllOutputs()

    var score = 0
    val grid = Grid()
    screen.map { it.toInt() }
        .chunked(3)
        .forEach {
            if (it[0] >= 0) grid.set(it[2], it[0], it[1]) else score = it[2]
        }

    var blocksRemaining = countBlocks(grid)

    while (blocksRemaining > 0) {
        /* for (j in grid.yRange) {
            for (i in grid.xRange) {
                print(grid.gridList[(j-grid.yRange.first)*grid.width() + (i-grid.xRange.first)])
            }
            print("\n")
        } */

        val ballDirection = grid.pointOf(3) - grid.pointOf(4)
        if (ballDirection.y < 1) {
            error("Game over")
        }
        interpreter.sendInput(when {
            ballDirection.x > 0 -> -1
            ballDirection.x < 0 -> 1
            else -> 0
        })
        interpreter.runProgram()

        val output = interpreter.getAllOutputs()
        output.map { it.toInt() }
            .chunked(3)
            .forEach {
                if (it[0] >= 0) grid.set(it[2], it[0], it[1]) else score = it[2]
            }

        blocksRemaining = countBlocks(grid)
    }

    return score
}