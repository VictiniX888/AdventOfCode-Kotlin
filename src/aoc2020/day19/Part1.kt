package aoc2020.day19

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day19/input.txt"))
    val (rulesTemp, messages) = input.split("\r\n\r\n").map { it.split("\r\n") }
    val rules = rulesTemp.associate { it.split(": ").let { (a, b) -> a.toInt() to b } }

    val answer = countValidMessages(messages, rules)
    println(answer)
}

private fun countValidMessages(messages: List<String>, rules: Map<Int, String>, matchTo: Int = 0): Int {
    return convertToRegex(rules, matchTo).let { regex -> messages.count { regex.matches(it) } }
}

private fun convertToRegex(rules: Map<Int, String>, start: Int = 0): Regex {
    var regexStr = rules[start]?.removeSurrounding("\"")?.split(" ")?.let { listOf("(") + it + ")" } ?: return Regex("")

    while (regexStr.any { s -> s.toIntOrNull() != null }) {
        regexStr = regexStr.flatMap { s ->
            s.toIntOrNull()?.let { n ->
                rules[n]?.removeSurrounding("\"")?.split(" ")?.let { listOf("(") + it + ")" } ?: return Regex("")
            } ?: listOf(s)
        }
    }

    return regexStr.joinToString("").toRegex()
}