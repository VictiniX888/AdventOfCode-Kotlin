package aoc2019.intcode

class IntcodeProgram(val instructions: MutableList<Int>) {

    // add and multiply do not take mode3 param as parameters that an instruction writes to will never be in "mode 1"
    fun add(parameter1: Int, parameter2: Int, parameter3: Int, mode1: Int, mode2: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        val sum = if (int1 != null && int2 != null) int1 + int2 else 0.also { error("Error: Mode not recognized") }
        instructions[parameter3] = sum
    }

    fun multiply(parameter1: Int, parameter2: Int, parameter3: Int, mode1: Int, mode2: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        val product = if (int1 != null && int2 != null) int1 * int2 else 0.also { error("Error: Mode not recognized") }
        instructions[parameter3] = product
    }

    fun input(parameter1: Int) {
        print("Input: ")
        val int = readLine()?.toIntOrNull()
        if (int != null) instructions[parameter1] = int else error("Error: Input not recognized")
    }

    fun output(parameter1: Int, mode1: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        println(int1)
    }

    // jump functions return index that pointer should jump to, else returns the original position of pointer (as a function)
    fun jumpIfTrue(parameter1: Int, parameter2: Int, mode1: Int, mode2: Int): (Int) -> Int {
        val check = getValueSpecified(parameter1, mode1)
        if (check != 0) {
            val position = getValueSpecified(parameter2, mode2)
            // return position - 3 to counteract the mandatory increase in position after the function is run
            if (position != null) return { position - 3 } else error("Error: Mode not recognized")
        }

        return { it }
    }

    fun jumpIfFalse(parameter1: Int, parameter2: Int, mode1: Int, mode2: Int): (Int) -> Int {
        val check = getValueSpecified(parameter1, mode1)
        if (check == 0) {
            val position = getValueSpecified(parameter2, mode2)
            if (position != null) return { position - 3 } else error("Error: Mode not recognized")
        }

        return { it }
    }

    fun lessThan(parameter1: Int, parameter2: Int, parameter3: Int, mode1: Int, mode2: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        if (int1 != null && int2 != null) {
            if (int1 < int2) instructions[parameter3] = 1 else instructions[parameter3] = 0
        } else error("Error: Mode not recognized")
    }

    fun equals(parameter1: Int, parameter2: Int, parameter3: Int, mode1: Int, mode2: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        if (int1 != null && int2 != null) {
            if (int1 == int2) instructions[parameter3] = 1 else instructions[parameter3] = 0
        } else error("Error: Mode not recognized")
    }

    private fun getValueSpecified(parameter: Int, mode: Int): Int? = when (mode) {
        0 -> instructions[parameter]
        1 -> parameter
        else -> null
    }
}