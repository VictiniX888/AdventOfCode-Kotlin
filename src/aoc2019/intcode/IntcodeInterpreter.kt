package aoc2019.intcode

class IntcodeInterpreter(private val program: IntcodeProgram) {

    fun runProgram(): IntcodeProgram {

        var i = 0
        while (i < program.instructions.size) {     // loop until the end of program
            when (program.instructions[i]) {
                1   -> program.add(program.instructions[i+1], program.instructions[i+2], program.instructions[i+3])
                2   -> program.multiply(program.instructions[i+1], program.instructions[i+2], program.instructions[i+3])
                99  -> i = program.instructions.size    // or terminate at opcode 99
            }

            i += 4
        }

        return program
    }
}