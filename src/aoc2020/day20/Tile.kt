package aoc2020.day20

// mutable class
class Tile(val id: Long, var grid: List<List<Boolean>>) {

    // tile connections
    var top: Long? = null
    var right: Long? = null
    var bottom: Long? = null
    var left: Long? = null

    operator fun get(x: Int, y: Int): Boolean = grid[y][x]

    fun removeBorder() {
        grid = grid.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }
    }

    fun getAllBorders(): List<List<Boolean>> {
        val borders = mutableListOf<List<Boolean>>()

        val top = grid.first()
        borders.add(top)
        // borders.add(top.reversed())

        val right = grid.map { it.last() }
        borders.add(right)
        // borders.add(right.reversed())

        val bottom = grid.last().reversed()
        borders.add(bottom)
        // borders.add(bottom.reversed())

        val left = grid.map { it.first() }.reversed()
        borders.add(left)
        // borders.add(left.reversed())

        return borders
    }

    fun rotateRight(times: Int = 1) {   // rotate in place
        repeat(times) {
            grid = grid.flatMap { it.withIndex() }.groupBy({ (i, _) -> i }, { (_, v) -> v }).map { (_, v) -> v.reversed() }
        }
    }

    fun flipHori() {
        grid = grid.map { it.reversed() }
    }

    fun flipVert() {
        grid = grid.reversed()
    }

    fun setConnection(i: Int, tileID: Long) {
        when (i) {
            0 -> top = tileID
            1 -> right = tileID
            2 -> bottom = tileID
            3 -> left = tileID
        }
    }

    fun getConnection(i: Int): Long? {
        return when (i) {
            0 -> top
            1 -> right
            2 -> bottom
            3 -> left
            else -> error("")
        }
    }

    fun print() {
        grid.forEach { it.map { b -> if (b) '#' else '.' }.joinToString("").also { s -> println(s) } }
    }
}