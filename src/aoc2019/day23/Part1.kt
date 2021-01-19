package aoc2019.day23

import aoc2019.intcode.IntcodeInterpreter
import aoc2019.intcode.IntcodeProgram
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private const val TARGET_ADDRESS = 255L

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day23/input.txt"))
        .split(',')
        .map { it.toLong() }
    
    val answer = runNetwork(input)
    println(answer)
}

private fun runNetwork(intcode: List<Long>, targetAddress: Long = TARGET_ADDRESS): Long {
    val computers = Array(50) { address ->
        val program = IntcodeProgram(intcode.toMutableList())
        val interpreter = IntcodeInterpreter(program)
        interpreter.sendInput(address.toLong())
        return@Array interpreter
    }
    
    val packets = mutableListOf<Packet>()
    
    while (true) {
        for ((address, x, y) in packets) {
            computers[address].sendInput(x, y)
        }
        packets.clear()
        
        computers.forEach { computer ->
            computer.apply { 
                stepProgram()
                if (isWaitingForInput) {
                    sendInput(-1)
                    isWaitingForInput = false
                    stepProgram()
                }
                
                if (output.size >= 3) {
                    if (output[0] == targetAddress) return output[2]
                    else packets.add(Packet(getOutput().toInt(), getOutput(), getOutput()))
                }
            }
        }
    }
}