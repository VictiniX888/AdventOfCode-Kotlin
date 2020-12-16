package aoc2020.day16

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day16/input.txt"))
    val (rules, ownTicket, otherTickets) = parseInput(input)

    val answer = calcErrorRate(rules, otherTickets)
    println(answer)
}

private fun calcErrorRate(rules: List<TicketRule>, tickets: List<List<Int>>): Int {
    val ruleRanges = rules.flatMap { it.ranges }
    return tickets.sumBy { ticket ->
        ticket.filterNot { value -> ruleRanges.any { range -> value in range } }
            .sum()
    }
}

private fun parseInput(input: String): Triple<List<TicketRule>, List<Int>, List<List<Int>>> {
    val (rulesTemp, ownTicketTemp, otherTicketsTemp) = input.split("\r\n\r\n").map { it.split("\r\n") }

    val rules = rulesTemp.map { line ->
        line.split(": ", " or ").let {
            TicketRule(it[0], it.drop(1).map { range ->
                range.split("-").let { (start, end) -> start.toInt()..end.toInt() }
            })
        }
    }

    val ownTicket = ownTicketTemp[1].split(",").map { it.toInt() }

    val otherTickets = otherTicketsTemp.drop(1).map { line ->
        line.split(",").map { it.toInt() }
    }

    return Triple(rules, ownTicket, otherTickets)
}