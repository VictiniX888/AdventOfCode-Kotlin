package aoc2019.intcode

class IntcodeInterpreter(private val program: IntcodeProgram) {

    // create a map of opcode to number of parameters it should take
    private val opcodeParamMap = mapOf(
        1 to 3, 2 to 3, 3 to 1, 4 to 1, 5 to 2, 6 to 2, 7 to 3, 8 to 3, 99 to 0
    )

    fun runProgram(): Boolean {
        var i = 0
        var hasTerminatedSuccessfully = false
        while (i < program.instructions.size) {     // loop until the end of program
            val currentInstruction = program.instructions[i].toString()
            val opcode = currentInstruction.takeLast(2)
                .toInt()
            val paramCount = opcodeParamMap.getOrElse(opcode, { error("Error: Opcode not recognized") })
            val paramModes = currentInstruction.dropLast(2)
                .padStart(paramCount, '0')
                .reversed()
                .map { it.toInt() - 48 }            // char.toInt() gives ASCII value of the character

            when (opcode) {
                1 -> program.add(
                    program.instructions[i + 1], program.instructions[i + 2], program.instructions[i + 3],
                    paramModes[0], paramModes[1]
                )

                2 -> program.multiply(
                    program.instructions[i + 1], program.instructions[i + 2], program.instructions[i + 3],
                    paramModes[0], paramModes[1]
                )

                3 -> program.input(
                    program.instructions[i+1]
                )

                4 -> program.output(
                    program.instructions[i+1],
                    paramModes[0]
                )

                5 -> i = i.let(program.jumpIfTrue(
                    program.instructions[i + 1], program.instructions[i + 2],
                    paramModes[0], paramModes[1]
                ))

                6 -> i = i.let(program.jumpIfFalse(
                    program.instructions[i + 1], program.instructions[i + 2],
                    paramModes[0], paramModes[1]
                ))

                7 -> program.lessThan(
                    program.instructions[i + 1], program.instructions[i + 2], program.instructions[i + 3],
                    paramModes[0], paramModes[1]
                )

                8 -> program.equals(
                    program.instructions[i + 1], program.instructions[i + 2], program.instructions[i + 3],
                    paramModes[0], paramModes[1]
                )

                99 -> {                             // or terminate at opcode 99
                    i = program.instructions.size
                    hasTerminatedSuccessfully = true
                }
            }

            i += paramCount + 1
        }

        return hasTerminatedSuccessfully
    }
}