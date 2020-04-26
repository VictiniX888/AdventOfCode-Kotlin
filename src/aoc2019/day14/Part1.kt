package aoc2019.day14

import java.nio.file.Files
import java.nio.file.Paths

private const val FUEL_NEEDED = 1

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day14/input.txt"))
        .map { it.split(" => ").map { str -> str.split(", ").map { s -> s.split(" ") } } }
        .associate { Pair(it[1][0][1], Pair(it[1][0][0].toInt(), it[0].associate { list -> Pair(list[1], list[0].toInt()) })) }
    // structure:  PRODUCT = (AMOUNT_PRODUCT, (REACTANT = AMOUNT_REACTANT))

    val answer = ingredientsNeeded(input)["ORE"]
    println(answer)
}

private fun ingredientsNeeded(reactions: Map<String, Pair<Int, Map<String, Int>>>, fuelNeeded: Int = FUEL_NEEDED): Map<String, Int> {
    var ingredients = mapOf(Pair("FUEL", fuelNeeded))

    while (!ingredients.containsKey("ORE") || ingredients.size > 1) {
        val tempIngredients = mutableMapOf<String, Int>()
        ingredients.forEach { (product, productNeeded) ->
            reactions[product]?.let { (amountProduct, reactants) ->
                if (amountProduct <= productNeeded) {
                    reactants.forEach { (reactant, amount) ->
                        tempIngredients[reactant] =
                            tempIngredients[reactant]?.plus(amount * (productNeeded / amountProduct)) ?: amount * (productNeeded / amountProduct)
                    }
                }
                if (productNeeded % amountProduct != 0) {
                    tempIngredients[product] = tempIngredients[product]?.plus(productNeeded % amountProduct) ?: productNeeded % amountProduct
                }
            } ?: tempIngredients.put(product, tempIngredients[product]?.plus(productNeeded) ?: productNeeded)
        }

        if (tempIngredients == ingredients) {
            ingredients.filter { (product, _) -> !hasParentInIngredients(product, ingredients.keys.minus(product), reactions) }
                .forEach { (product, productNeeded) ->
                    reactions[product]?.let { (_, reactants) ->
                        reactants.forEach { (reactant, amount) ->
                            tempIngredients[reactant] = tempIngredients[reactant]?.plus(amount) ?: amount
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