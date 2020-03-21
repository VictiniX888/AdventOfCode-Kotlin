package aoc2019.day06

class Tree<T>(private val value: T) {

    private val children = mutableListOf<Tree<T>>()

    fun insert(value: T) {
        children.add(Tree(value))
    }

    fun insert(child: Tree<T>) {
        children.add(child)
    }

    fun find(value: T): Tree<T>? {          // dfs
        if (this.value == value) {
            return this
        } else {
            for (child in children) {
                child.find(value)?.let { return it }
            }
        }

        return null
    }

    fun valueDepthMap(depth: Int = 0): Map<T, Int> =
        if (children.size == 0) {
            mapOf(value to depth)
        } else {
            children.map { it.valueDepthMap(depth + 1) }
                .fold(mapOf(value to depth), {
                acc, map -> acc.plus(map)
            })
        }

    fun getPathTo(value: T): List<T>? =     // dfs
        if (this.value == value) {
            listOf(this.value)
        } else {
            children.map { it.getPathTo(value) }
                .firstOrNull { it != null }
                ?.let { listOf(this.value).plus(it) }
        }
}