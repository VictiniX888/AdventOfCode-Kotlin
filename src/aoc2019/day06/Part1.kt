package aoc2019.day06

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val input = Files.readAllLines(Paths.get("src/aoc2019/day06/input.txt"))
        .associate { it.split(")").let { list -> Pair(list[1], list[0]) } }  // convert list into map, in which key orbits the value

    val answer = countTotalOrbits(sortOrbits(input))
    println(answer)
}

private fun countTotalOrbits(orbitTree: Tree<String>): Int {
    var orbits = 0
    orbitTree.valueDepthMap().forEach { (_, depth) ->
        orbits += depth
    }

    return orbits
}

private fun sortOrbits(mapData: Map<String, String>): Tree<String> {

    val addedNodes = mutableSetOf<String>()
    val orbitTrees = mutableListOf<Tree<String>>()

    mapData.forEach { (orbiter, orbited) ->
        val isOrbitedAdded = addedNodes.contains(orbited)
        val isOrbiterAdded = addedNodes.contains(orbiter)

        if (isOrbitedAdded && isOrbiterAdded) {
            val orbitedTree = orbitTrees.find(orbited)!!
            val orbiterTree = orbitTrees.find(orbiter)!!
            orbitTrees.remove(orbiterTree)
            orbitedTree.insert(orbiterTree)
        }
        else if (isOrbitedAdded) {
            val orbitedTree = orbitTrees.find(orbited)!!
            orbitedTree.insert(orbiter)
            addedNodes.add(orbiter)
        }
        else if (isOrbiterAdded) {
            val orbiterTree = orbitTrees.find(orbiter)!!
            orbitTrees.remove(orbiterTree)
            val orbitedTree = Tree(orbited).apply { insert(orbiterTree) }
            orbitTrees.add(orbitedTree)
            addedNodes.add(orbited)
        }
        else {
            val orbitedTree = Tree(orbited).apply { insert(orbiter) }
            orbitTrees.add(orbitedTree)
            addedNodes.add(orbiter)
            addedNodes.add(orbited)
        }
    }

    return if (orbitTrees.size == 1) orbitTrees.first() else error("Failed to construct a tree")
}

private fun <T> List<Tree<T>>.find(value: T): Tree<T>? {

    for (tree in this) {
        tree.find(value)?.let { return it }
    }

    return null
}