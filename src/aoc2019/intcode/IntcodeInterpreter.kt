package aoc2019.intcode

class IntcodeInterpreter(private val program: IntcodeProgram, private var pointer: Int = 0) {

    // create a map of opcode to number of parameters it should take
    private val opcodeParamMap = mapOf(
        1 to 3, 2 to 3, 3 to 1, 4 to 1, 5 to 2, 6 to 2, 7 to 3, 8 to 3, 9 to 1, 99 to 0
    )

    private val inputList = mutableListOf<Long>()
    private val outputList = mutableListOf<Long>()
    
    val input
        get() = inputList
    val output
        get() = outputList

    var hasTerminatedSuccessfully = false
    var isWaitingForInput = false

    fun runProgram(): List<Long> {
        isWaitingForInput = false

        while (pointer < program.instructions.size && !hasTerminatedSuccessfully && !isWaitingForInput) {     // loop until the end of program
            stepProgram()
        }

        return outputList
    }
    
    fun stepProgram() {
        val currentInstruction = program.instructions[pointer].toString()
        val opcode = currentInstruction.takeLast(2)
            .toInt()
        val paramCount = opcodeParamMap.getOrElse(opcode, { error("Error: Opcode not recognized") })
        val paramModes = currentInstruction.dropLast(2)
            .padStart(paramCount, '0')
            .reversed()
            .map { it.toInt() - 48 }            // char.toInt() gives ASCII value of the character

        when (opcode) {
            1 -> program.add(
                program.instructions[pointer + 1], program.instructions[pointer + 2], program.instructions[pointer + 3],
                paramModes[0], paramModes[1], paramModes[2]
            )

            2 -> program.multiply(
                program.instructions[pointer + 1], program.instructions[pointer + 2], program.instructions[pointer + 3],
                paramModes[0], paramModes[1], paramModes[2]
            )

            3 -> {
                if (inputList.isEmpty()) {
                    isWaitingForInput = true   // if there is no more input but input is needed, "pause" the program
                    return
                }
                else {
                    program.input(
                        program.instructions[pointer + 1],
                        paramModes[0],
                        inputList.removeAt(0)    // removeAt returns the removed element
                    )
                }
            }

            4 -> outputList.add(program.output(
                program.instructions[pointer+1],
                paramModes[0]
            ))

            5 -> pointer = pointer.let(program.jumpIfTrue(
                program.instructions[pointer + 1], program.instructions[pointer + 2],
                paramModes[0], paramModes[1]
            ))

            6 -> pointer = pointer.let(program.jumpIfFalse(
                program.instructions[pointer + 1], program.instructions[pointer + 2],
                paramModes[0], paramModes[1]
            ))

            7 -> program.lessThan(
                program.instructions[pointer + 1], program.instructions[pointer + 2], program.instructions[pointer + 3],
                paramModes[0], paramModes[1], paramModes[2]
            )

            8 -> program.equals(
                program.instructions[pointer + 1], program.instructions[pointer + 2], program.instructions[pointer + 3],
                paramModes[0], paramModes[1], paramModes[2]
            )

            9 -> program.relativeBaseOffset(
                program.instructions[pointer + 1],
                paramModes[0]
            )

            99 -> {                             // or terminate at opcode 99
                hasTerminatedSuccessfully = true
                return
            }
        }

        pointer += paramCount + 1
    }

    fun sendInput(vararg input: Long) = inputList.addAll(input.toList())

    fun getOutput() = if (outputList.isNotEmpty()) outputList.removeAt(0) else error("Error: No output found")

    fun getAllOutputs() = outputList.toList().also { outputList.clear() }
}