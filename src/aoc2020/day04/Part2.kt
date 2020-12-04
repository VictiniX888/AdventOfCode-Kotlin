package aoc2020.day04

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day04/input.txt"))
        .split("\r\n\r\n")
        .map { it.split(" ", "\r\n").associate { field -> field.split(":").let { (key, value) -> Pair(key, value) } } }

    val answer = countValidPassports(input)
    println(answer)
}

private fun countValidPassports(passports: List<Map<String, String>>): Int {
    return passports.count { passport -> isValidPassport(passport) }
}

private fun isValidPassport(passport: Map<String, String>): Boolean {
    var validFields = 0
    passport.forEach { (key, value) ->
        val isFieldValid = when (key) {
            "byr" -> value.toInt() in 1920..2002
            "iyr" -> value.toInt() in 2010..2020
            "eyr" -> value.toInt() in 2020..2030
            "hgt" -> value.substring(value.length-2).let {
                when (it) {
                    "cm" -> value.substring(0 until value.length-2).toInt() in 150..193
                    "in" -> value.substring(0 until value.length-2).toInt() in 59..76
                    else -> false
                }
            }
            "hcl" -> value.matches(Regex("#[0-9a-f]{6}"))
            "ecl" -> value.matches(Regex("amb|blu|brn|gry|grn|hzl|oth"))
            "pid" -> value.matches(Regex("[0-9]{9}"))
            else  -> false
        }

        if (isFieldValid) validFields++
    }

    return validFields == 7
}