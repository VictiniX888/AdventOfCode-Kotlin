package aoc2019.day04

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2019/day04/input.txt"))
        .split("-")
        .map { Integer.parseInt(it) }
    val range = IntProgression.fromClosedRange(input[0], input[1], 1)

    val passwords = findAllPasswords(range)
    println(passwords.size)
}

private fun findAllPasswords(range: IntProgression): List<Int> = range.filter { it.isPassword() }

private fun Int.isPassword(): Boolean {

    val intAsString = this.toString()
    val hasDoubleDigits = Regex("""(.)\1""").containsMatchIn(intAsString)
    val hasDecrease = intAsString
        .zipWithNext()
        .any { (first, second) -> first > second }

    return hasDoubleDigits && !hasDecrease
}