package aoc2019.day14

import java.nio.file.Files
import java.nio.file.Paths

private const val ORE_AMOUNT = 1_000_000_000_000

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day14/input.txt"))
        .map { it.split(" => ").map { str -> str.split(", ").map { s -> s.split(" ") } } }
        .associate { Pair(it[1][0][1], Pair(it[1][0][0].toInt(), it[0].associate { list -> Pair(list[1], list[0].toInt()) })) }
    // structure:  PRODUCT = (AMOUNT_PRODUCT, (REACTANT = AMOUNT_REACTANT))

    val answer = maxFuelProduced(input)
    println(answer)
}

// binary search algorithm
private fun maxFuelProduced(reactions: Map<String, Pair<Int, Map<String, Int>>>, reactantAmount: Long = ORE_AMOUNT): Long {
    var lowerBound = reactantAmount / ingredientsNeeded(reactions, 1).getValue("ORE")
    var upperBound = lowerBound

    // set upper bound by multiplying it by 2 until it goes over limit
    while (ingredientsNeeded(reactions, upperBound).getValue("ORE") <= reactantAmount) {
        upperBound *= 2
    }

    while (upperBound - lowerBound > 1) {
        val midpoint = ((upperBound - lowerBound) / 2) + lowerBound
        if (ingredientsNeeded(reactions, midpoint).getValue("ORE") <= reactantAmount) {
            lowerBound = midpoint
        } else {
            upperBound = midpoint
        }
    }

    // return lower bound since upperbound exceeds limit
    return lowerBound
}

private fun ingredientsNeeded(reactions: Map<String, Pair<Int, Map<String, Int>>>, fuelNeeded: Long): Map<String, Long> {
    var ingredients = mapOf(Pair("FUEL", fuelNeeded))

    while (!ingredients.containsKey("ORE") || ingredients.size > 1) {
        val tempIngredients = mutableMapOf<String, Long>()
        ingredients.forEach { (product, productNeeded) ->
            reactions[product]?.let { (amountProduct, reactants) ->
                if (amountProduct <= productNeeded) {
                    reactants.forEach { (reactant, amount) ->
                        tempIngredients[reactant] =
                            tempIngredients[reactant]?.plus(amount * (productNeeded / amountProduct)) ?: amount * (productNeeded / amountProduct)
                    }
                }
                if (productNeeded % amountProduct != 0L) {
                    tempIngredients[product] = tempIngredients[product]?.plus(productNeeded % amountProduct) ?: productNeeded % amountProduct
                }
            } ?: tempIngredients.put(product, tempIngredients[product]?.plus(productNeeded) ?: productNeeded)
        }

        if (tempIngredients == ingredients) {
            ingredients.filter { (product, _) -> !hasParentInIngredients(product, ingredients.keys.minus(product), reactions) }
                .forEach { (product, productNeeded) ->
                    reactions[product]?.let { (_, reactants) ->
                        reactants.forEach { (reactant, amount) ->
                            tempIngredients[reactant] = tempIngredients[reactant]?.plus(amount) ?: amount.toLong()
                        }
                        tempIngredients.remove(product)
                    } ?: tempIngredients.put(product, tempIngredients[product]?.plus(productNeeded) ?: productNeeded)
                }
        }

        ingredients = tempIngredients
    }

    return ingredients
}

private fun hasParentInIngredients(ingredient: String, otherIngredients: Set<String>, reactions: Map<String, Pair<Int, Map<String, Int>>>): Boolean {
    val usingIngredient = reactions.filterValues { (_, map) -> map.containsKey(ingredient) }
    if (usingIngredient.isEmpty()) {
        return false
    } else {
        for (i in usingIngredient.keys) {
            if (otherIngredients.contains(i)) {
                return true
            } else {
                if (hasParentInIngredients(i, otherIngredients.minus(i), reactions)) {
                    return true
                }
            }
        }
    }

    return false
}