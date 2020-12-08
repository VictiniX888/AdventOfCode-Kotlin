package aoc2020.day08

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day08/input.txt"))
        .map { s -> s.split(" ").let { (operation, argument) -> Instruction(operation, argument.toInt()) } }

    val answer = makeTerminate(input)
    println(answer)
}

private fun makeTerminate(instructions: List<Instruction>): Int {
    instructions.forEachIndexed { index, instruction ->
        if (instruction.operation != "acc") {
            val newInstructions = instructions.toMutableList()
            when (instruction.operation) {
                "nop" -> newInstructions[index] = instruction.copy(operation = "jmp")
                "jmp" -> newInstructions[index] = instruction.copy(operation = "nop")
            }

            val console = Console(newInstructions)
            val pointerCache = mutableSetOf<Int>()

            while (pointerCache.add(console.pointer)) {
                console.runLine().let { hasTerminated -> if (hasTerminated) return console.accumulator }
            }
        }
    }

    error("No terminating sequence found")
}