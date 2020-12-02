package aoc2020.day02

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day02/input.txt"))
        .map { it.split(" ") }

    val answer = countValidPasswords(input)
    println(answer)
}

private fun countValidPasswords(passwords: List<List<String>>): Int {
    return passwords.count { (intrange, char, password) ->
        isPasswordValid(password, char[0], intrange.split("-").let { it[0].toInt() .. it[1].toInt() })
    }
}

private fun isPasswordValid(password: String, key: Char, times: IntRange): Boolean {
    return password.count { it == key } in times
}