package aoc2019.intcode

class IntcodeProgram(val instructions: MutableList<Long>) {

    private var relativeBase = 0

    // add and multiply do not take mode3 param as parameters that an instruction writes to will never be in "mode 1"
    fun add(parameter1: Long, parameter2: Long, parameter3: Long, mode1: Int, mode2: Int, mode3: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        val sum = int1 + int2
        instructions.setValue(parameter3, mode3, sum)
    }

    fun multiply(parameter1: Long, parameter2: Long, parameter3: Long, mode1: Int, mode2: Int, mode3: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        val product = int1 * int2
        instructions.setValue(parameter3, mode3, product)
    }

    fun input(parameter1: Long, mode1: Int, input: Long) {
        instructions.setValue(parameter1, mode1, input)
    }

    fun output(parameter1: Long, mode1: Int): Long {
        return getValueSpecified(parameter1, mode1)
    }

    // jump functions return index that pointer should jump to, else returns the original position of pointer (as a function)
    fun jumpIfTrue(parameter1: Long, parameter2: Long, mode1: Int, mode2: Int): (Int) -> Int {
        val check = getValueSpecified(parameter1, mode1)
        if (check != 0L) {
            val position = getValueSpecified(parameter2, mode2).toInt()
            // return position - 3 to counteract the mandatory increase in position after the function is run
            return { position - 3 }
        }

        return { it }
    }

    fun jumpIfFalse(parameter1: Long, parameter2: Long, mode1: Int, mode2: Int): (Int) -> Int {
        val check = getValueSpecified(parameter1, mode1)
        if (check == 0L) {
            val position = getValueSpecified(parameter2, mode2).toInt()
            return { position - 3 }
        }

        return { it }
    }

    fun lessThan(parameter1: Long, parameter2: Long, parameter3: Long, mode1: Int, mode2: Int, mode3: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        if (int1 < int2) instructions.setValue(parameter3, mode3, 1) else instructions.setValue(parameter3, mode3, 0)
    }

    fun equals(parameter1: Long, parameter2: Long, parameter3: Long, mode1: Int, mode2: Int, mode3: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        val int2 = getValueSpecified(parameter2, mode2)
        if (int1 == int2) instructions.setValue(parameter3, mode3, 1) else instructions.setValue(parameter3, mode3, 0)
    }

    fun relativeBaseOffset(parameter1: Long, mode1: Int) {
        val int1 = getValueSpecified(parameter1, mode1)
        relativeBase += int1.toInt()
    }

    private fun getValueSpecified(parameter: Long, mode: Int): Long = when (mode) {
        // does not take into account negative indexes, which should throw an error (it already does this)
        0 -> if (parameter < instructions.size) instructions[parameter.toInt()] else 0
        1 -> parameter
        2 -> if (parameter + relativeBase < instructions.size) instructions[parameter.toInt() + relativeBase] else 0
        else -> error("Error: Mode not recognized")
    }

    //add the element to the list at index, extending the list if index > size of list
    private fun MutableList<Long>.setValue(parameter: Long, mode: Int, element: Long) {

        val index = when (mode) {
            0 -> parameter
            1 -> error("Error: Mode 1 in write")
            2 -> parameter + relativeBase
            else -> error("Error: Mode not recognized")
        }

        when {
            index < 0           -> error("Error: Tried to add to list at negative index")
            index < this.size   -> this[index.toInt()] = element
            else -> {
                this.addAll(List(index.toInt() - this.size) { 0L })
                this.add(element)
            }
        }
    }
}