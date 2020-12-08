package aoc2020.day08

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day08/input.txt"))
        .map { s -> s.split(" ").let { (operation, argument) -> Instruction(operation, argument.toInt()) } }

        /* alternate way to parse input with regex:
        .map { instr ->
            Regex("""(\w+) ([+\-][0-9]+)""").find(instr)?.destructured?.let { (op, arg) ->
                Instruction(op, arg.toInt())
            } ?: error("Input cannot be parsed")
        }
        */

    val answer = findLoop(input)
    println(answer)
}

private fun findLoop(instructions: List<Instruction>): Int {
    val console = Console(instructions)
    val pointerCache = mutableSetOf<Int>()

    while (pointerCache.add(console.pointer)) {
        console.runLine()
    }

    return console.accumulator
}