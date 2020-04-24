package aoc2019.day13

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day13/input.txt"))
        .split(",")
        .map { it.toLong() }

    val answer = countBlocks(drawScreen(input))
    println(answer)
}

private fun countBlocks(grid: Grid): Int = grid.gridList.count { it == 2 }

private fun drawScreen(intcode: List<Long>): Grid {
    val program = IntcodeProgram(intcode.toMutableList())
    val interpreter = IntcodeInterpreter(program)
    interpreter.runProgram()
    val output = interpreter.getAllOutputs()

    val grid = Grid()
    output.chunked(3)
        .forEach {
            grid.set(it[2].toInt(), it[0].toInt(), it[1].toInt())
        }

    return grid
}