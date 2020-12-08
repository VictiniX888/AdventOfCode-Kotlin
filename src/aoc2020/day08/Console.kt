package aoc2020.day08

class Console(private val code: List<Instruction>) {

    var accumulator = 0
    var pointer = 0

    fun runLine(): Boolean {
        val instruction = code[pointer]
        when (instruction.operation) {
            "acc" -> acc(instruction.argument)
            "jmp" -> jmp(instruction.argument)
            "nop" -> nop(instruction.argument)
        }

        pointer++

        return pointer == code.size // if true, the program has terminated
    }

    private fun acc(inc: Int) {
        accumulator += inc
    }

    private fun jmp(offset: Int) {
        pointer += offset - 1
    }

    private fun nop(arg: Int) {}
}