package aoc2020.day14

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day14/input.txt"))

    val answer = initializeDocking(input)
    println(answer)
}

private fun initializeDocking(input: List<String>): Long {
    val memory = HashMap<Long, Long>()
    var bitmask = input.first().parseBitmask()

    input.drop(1).forEach { str ->
        if (str.startsWith("ma")) {
            bitmask = str.parseBitmask()
        } else {
            val (address, value) = str.parseMemValue()
            val postmaskAddresses = address.applyBitmask(bitmask)
            postmaskAddresses.forEach { postmaskAddress ->
                memory[postmaskAddress] = value
            }
        }
    }

    return memory.values.sum()
}

private fun Long.applyBitmask(bitmask: String): List<Long> {
    val accs = mutableListOf(0L)
    var power = 1L

    this.toString(2)
        .padStart(36, '0')
        .zip(bitmask)
        .reversed()
        .forEach { (bit, mask) ->
            val iterator = accs.listIterator()
            when (mask) {
                '0' -> {
                    if (bit == '1') iterator.forEach { acc -> iterator.set(acc + power) }
                }
                '1' -> {
                    iterator.forEach { acc -> iterator.set(acc + power) }
                }
                'X' -> {
                    iterator.forEach { acc -> iterator.add(acc + power) }
                }
                else -> error("Unknown bit mask encountered")
            }

            power *= 2
        }

    return accs
}

private fun String.parseMemValue(): Pair<Long, Long> {
    return Regex("""mem\[([0-9]+)] = ([0-9]+)""")
        .matchEntire(this)
        ?.destructured
        ?.let { (address, value) -> Pair(address.toLong(), value.toLong()) }
        ?: error("Memory address and value cannot be parsed")
}

private fun String.parseBitmask(): String {
    return this.drop(7)
}