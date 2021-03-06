package aoc2020.day19

import java.nio.file.Files
import java.nio.file.Paths

// The way I initially solved this was by editing the rules map as such:
//      rules[8] = "42 +"
//      rules[11] = "(?<eleven> 42 (?&eleven)? 31 )"
// and then generate the Regex using my convertToRegex function from Part 1.
// However, since Kotlin (actually Java) does not support recursive regexes,
// I copied the expression into an online Regex tester and had it give me the answer.

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day19/input.txt"))
    val (rulesTemp, messages) = input.split("\r\n\r\n").map { it.split("\r\n") }
    val rules = rulesTemp.associate { s ->
        s.split(": ").let { (a, b) ->
            a.toInt() to (b.removeSurrounding("\"").split(" | ").map { it.split(' ') })
        }
    }.toMutableMap()

    rules[8] = listOf(listOf("42"), listOf("42", "8"))
    rules[11] = listOf(listOf("42", "31"), listOf("42", "11", "31"))

    val answer = countValidMessages(messages, rules)
    println(answer)
}

private fun countValidMessages(messages: List<String>, rules: Map<Int, List<List<String>>>): Int {
    return messages.count { it.checkMatch(rules).contains("") }
}

private fun String.checkMatch(rules: Map<Int, List<List<String>>>, currentRule: Int = 0): Set<String> {
    val validStrings = mutableSetOf<String>()

    val nextRuleOptions = rules[currentRule] ?: error("Current rule $currentRule cannot be found")
    for (nextRules in nextRuleOptions) {    // FLATMAP
        var remainingStrings = setOf(this)
        for (nextRule in nextRules) {   // ALL
            if (nextRule.toIntOrNull() != null) {
                remainingStrings = remainingStrings.flatMap { it.checkMatch(rules, nextRule.toInt()) }.toSet()
            } else {    // assuming rules with characters are in the form { n: "c" }
                if (this.isNotEmpty() && this.first() == nextRule.first()) {
                    return setOf(this.drop(1))
                } else {
                    return setOf()
                }
            }
        }

        validStrings += remainingStrings
    }

    return validStrings
}