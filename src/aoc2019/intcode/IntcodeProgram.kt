package aoc2019.intcode

class IntcodeProgram(val instructions: MutableList<Int>) {

    fun add(readPos1: Int, readPos2: Int, writePos: Int) {
        val sum = instructions[readPos1] + instructions[readPos2]
        instructions[writePos] = sum
    }

    fun multiply(readPos1: Int, readPos2: Int, writePos: Int) {
        val product = instructions[readPos1] * instructions[readPos2]
        instructions[writePos] = product
    }
}