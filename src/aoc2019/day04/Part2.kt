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

    // this is a mess that I still cannot understand despite writing it myself but I will attempt to comment on it
    // the initial (.)* captures any digit before the "desired" group
    // the first lookforward checks to see whether the first digit of the "desired" group (the double digit) is the same as the digit before it
    // the second lookforward checks to see whether the digit after the "desired" group is the same as the first digit of the "desired" group
    // the last .* is required after the regex to ensure that it passes even if there are still digits after the "desired" group
    val hasDoubleDigits = Regex("""(.)*(?!\1)(.)\2(?!\2).*""").matches(intAsString)

    val hasDecrease = intAsString
        .zipWithNext()
        .any { (first, second) -> first > second }

    return hasDoubleDigits && !hasDecrease
}