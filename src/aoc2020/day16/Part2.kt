package aoc2020.day16

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readString(Paths.get("src/aoc2020/day16/input.txt"))
    val (rules, ownTicket, otherTickets) = parseInput(input)

    val answer = calcDeparture(mapFields(rules, otherTickets), ownTicket)
    println(answer)
}

private typealias Ticket = List<Int>

private fun calcDeparture(fieldMap: Map<String, Int>, ticket: Ticket): Long {
    return fieldMap.filterKeys { field -> field.startsWith("departure") }
        .map { (_, index) -> ticket[index].toLong() }
        .reduce { acc, i -> acc * i }
}

private fun mapFields(rules: List<TicketRule>, tickets: List<Ticket>): Map<String, Int> {
    val ruleRanges = rules.flatMap { it.ranges }
    val validTickets = tickets.filter { ticket ->
        ticket.all { value -> ruleRanges.any { range -> value in range } }
    }

    val possibleFields = validTickets.flatMap { ticket -> ticket.withIndex() }
        .groupBy({ it.index }, { it.value })
        .mapValues { (_, values) ->
            rules.filter { (_, ranges) ->
                values.all { value -> ranges.any { range -> value in range } }
            }.map { it.field }
        }

    val finalFields = mutableMapOf<String, Int>()
    val usedFields = mutableListOf<String>()
    for (i in 1..possibleFields.values.maxOf { it.size }) {
        possibleFields.filterValues { fields -> fields.size == i }
            .forEach { (index, fields) ->
                val field = (fields - fields.intersect(usedFields)).first()
                finalFields[field] = index
                usedFields.add(field)
            }
    }

    return finalFields
}

private fun parseInput(input: String): Triple<List<TicketRule>, Ticket, List<Ticket>> {
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