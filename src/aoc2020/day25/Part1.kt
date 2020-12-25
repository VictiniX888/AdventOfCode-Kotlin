package aoc2020.day25

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val (key, door) = Files.readAllLines(Paths.get("src/aoc2020/day25/input.txt"))
        .map { it.toLong() }

    val answer = calcEncryptionKey(key, door)
    println(answer)
}

private fun calcEncryptionKey(keyKey: Long, doorKey: Long): Long {
    val keyLoopSize = findLoopSize(keyKey)
    var encryptionKey = 1L
    repeat(keyLoopSize) {
        encryptionKey = transformSubjectKey(encryptionKey, doorKey)
    }

    return encryptionKey
}

private fun findLoopSize(publicKey: Long): Int {
    var loopSize = 0
    var currentNumber = 1L

    while (currentNumber != publicKey) {
        loopSize++
        currentNumber = transformSubjectKey(currentNumber)
    }

    return loopSize
}

private fun transformSubjectKey(currentNumber: Long, initialSubjectNumber: Long = 7): Long {
    return (currentNumber * initialSubjectNumber) % 20201227
}