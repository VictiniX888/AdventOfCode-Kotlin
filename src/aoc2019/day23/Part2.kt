package aoc2019.day23

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day23/input.txt"))
        .split(',')
        .map { it.toLong() }

    val answer = runNetwork(input)
    println(answer)
}

private fun runNetwork(intcode: List<Long>): Long {
    val computers = Array(50) { address ->
        val program = IntcodeProgram(intcode.toMutableList())
        val interpreter = IntcodeInterpreter(program)
        interpreter.sendInput(address.toLong())
        return@Array interpreter
    }
    val idle = BooleanArray(50) { false }
    var idleTime = 0
    
    val packets = mutableListOf<Packet>()
    var natX: Long? = null
    var natY: Long? = null
    var prevY: Long? = null

    while (true) {
        for ((address, x, y) in packets) {
            if (address == 255) {
                natX = x
                natY = y
            } else {
                computers[address].sendInput(x, y)
            }
        }
        packets.clear()
        
        if (idle.all { it }) {
            idleTime++
        } else if (idleTime > 0) {
            idleTime = 0
        }
        
        if (idleTime > 100) {   // arbitrary limit
            if (prevY == natY) {
                return natY!!
            } else {
                computers[0].sendInput(natX!!, natY!!)
                prevY = natY
            }
        }

        computers.forEachIndexed { index, computer ->
            computer.apply {
                if (input.isNotEmpty() && idle[index]) {
                    idle[index] = false
                }
                stepProgram()
                if (isWaitingForInput) {
                    sendInput(-1)
                    isWaitingForInput = false
                    idle[index] = true
                    stepProgram()
                }

                if (output.size >= 3) {
                    packets.add(Packet(getOutput().toInt(), getOutput(), getOutput()))
                }
            }
        }
    }
}