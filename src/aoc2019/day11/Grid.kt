package aoc2019.day11

class Grid {
    // resizable grid implementation
    // +ve y is downwards, +ve x is rightwards

    // start with a grid with value 0 at point (0, 0), height 1, width 1
    var xRange = 0..0
    var yRange = 0..0
    val gridList = mutableListOf(0)

    fun width() = xRange.count()
    //fun height() = yRange.count()

    fun set(value: Int, x: Int, y: Int) {
        if (x in xRange && y in yRange) {
            gridList[(y-yRange.first)*width() + (x-xRange.first)] = value
        } else {    // add extra rows and/or columns
            if (x > xRange.last) {
                yRange.forEachIndexed { index, _ ->
                    for (i in 0 until (x-xRange.last)) {
                        gridList.add(index*(width()+x-xRange.last) + i + width(), 0)
                    }
                }
                xRange = xRange.first .. x
            } else if (x < xRange.first) {
                yRange.forEachIndexed { index, _ ->
                    repeat(xRange.first - x) {
                        gridList.add(index*(width()+xRange.first-x), 0)
                    }
                }
                xRange = x .. xRange.last
            }

            if (y > yRange.last) {
                repeat(width() * (y-yRange.last)) {
                    gridList.add(0)
                }
                yRange = yRange.first .. y
            } else if (y < yRange.first) {
                repeat(width() * (yRange.first-y)) {
                    gridList.add(0, 0)
                }
                yRange = y .. yRange.last
            }

            // set the new value
            gridList[(y-yRange.first)*width() + (x-xRange.first)] = value
        }
    }

    fun set(value: Int, point: Point) = set(value, point.x, point.y)

    fun get(x: Int, y: Int): Int =
        if (x in xRange && y in yRange) gridList[(y-yRange.first)*width() + (x-xRange.first)] else 0

    fun get(point: Point): Int = get(point.x, point.y)
}