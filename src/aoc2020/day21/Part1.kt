package aoc2020.day21

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2020/day21/input.txt"))
        .map { s ->
            s.dropLast(1)
                .split(" (contains ")
                .map { it.split(", ", " ") }
        }.map { it.last().toSet() to it.first().toSet() }

    val answer = countNoAllergens(input)
    println(answer)
}

private fun countNoAllergens(foodlist: List<Pair<Set<String>, Set<String>>>): Int {
    val allergenMap = mapPotentialAllergens(foodlist)

    val notAllergens = foodlist.map { it.second }
        .sumOf { ingredients ->
            ingredients.count { ingredient -> ingredient !in allergenMap.values }
        }

    return notAllergens
}

private fun mapPotentialAllergens(foodlist: List<Pair<Set<String>, Set<String>>>): Map<String, String> {
    val foodMap = mutableMapOf<String, List<Set<String>>>()
    foodlist.forEach { (allergens, ingredients) ->
        allergens.forEach { allergen ->
            foodMap.compute(allergen) { _, ingrList ->
                if (ingrList == null) {
                    listOf(ingredients)
                } else {
                    ingrList.plusElement(ingredients)
                }
            }
        }
    }

    val potentialIngredients = foodMap.mapValues { (_, ingrList) ->
        ingrList.reduce { acc, ingredients -> acc.intersect(ingredients) }
    }.toMutableMap()

    val confirmedIngredients = mutableSetOf<String>()
    val allergenMap = mutableMapOf<String, String>()

    while (allergenMap.size != foodMap.size) {
        foodMap.keys.forEach { allergen ->
            if (potentialIngredients[allergen] != null) {
                potentialIngredients.computeIfPresent(allergen) { _, ingredients ->
                    ingredients - confirmedIngredients
                }

                if (potentialIngredients[allergen]!!.isEmpty()) {
                    potentialIngredients.remove(allergen)
                } else if (potentialIngredients[allergen]!!.size == 1) {
                    allergenMap[allergen] = potentialIngredients[allergen]!!.first()
                    confirmedIngredients += potentialIngredients[allergen]!!.first()
                    potentialIngredients.remove(allergen)
                }
            }
        }
    }

    return allergenMap
}