package aoc2020.day04

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day04/input.txt"))
        .split("\r\n\r\n")
        .map { it.split(" ", "\r\n") }

    val answer = countValidPassports(input)
    println(answer)
}

private fun countValidPassports(passports: List<List<String>>): Int {
    return passports.count { passport -> isValidPassport(passport) }
}

private fun isValidPassport(passport: List<String>): Boolean {
    return passport.fold(0) { acc, field -> if (field.contains(Regex("(byr|iyr|eyr|hgt|hcl|ecl|pid)"))) acc+1 else acc } == 7
}