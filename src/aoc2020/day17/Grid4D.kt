package aoc2020.day17

// redundant
class Grid4D<T>(initGrid: List<List<List<List<T>>>>,
                private val default: T,
                private var offsetX: Int = 0,
                private var offsetY: Int = 0,
                private var offsetZ: Int = 0,
                private var offsetW: Int = 0) {

    private val grid = mutableListOf<MutableList<MutableList<MutableList<T>>>>()

    // private var offsetX = 0     // +ve x = right
    // private var offsetY = 0     // +ve y = down
    // private var offsetZ = 0     // +ve z = "down"

    private var sizeX = initGrid[0][0][0].size
    private var sizeY = initGrid[0][0].size
    private var sizeZ = initGrid[0].size
    private var sizeW = initGrid.size

    val rangeX: IntRange
        get() = offsetX until offsetX + sizeX
    val rangeY: IntRange
        get() = offsetY until offsetY + sizeY
    val rangeZ: IntRange
        get() = offsetZ until offsetZ + sizeZ
    val rangeW: IntRange
        get() = offsetW until offsetW + sizeW

    init {
        // NOTE: initGrid cannot be empty and all lines must be equal length
        initGrid.forEach { dim ->
            grid.add(dim.map { layer -> layer.map { it.toMutableList() }.toMutableList() }.toMutableList())
        }
    }

    operator fun get(x: Int, y: Int, z: Int, w: Int): T {
        return if (x in rangeX && y in rangeY && z in rangeZ && w in rangeW) grid[w-offsetW][z-offsetZ][y-offsetY][x-offsetX] else default
    }

    operator fun set(x: Int, y: Int, z: Int, w: Int, value: T) {
        // resize grid
        if (x < rangeX.first) {
            grid.forEach { dim ->
                dim.forEach { layer ->
                    layer.forEach { row ->
                        for (x1 in x until rangeX.first) {
                            row.add(0, default)
                        }
                    }
                }
            }
            sizeX += rangeX.first - x
            offsetX = x
        } else if (x > rangeX.last) {
            grid.forEach { dim ->
                dim.forEach { layer ->
                    layer.forEach { row ->
                        for (x1 in rangeX.last until x) {
                            row.add(default)
                        }
                    }
                }
            }
            sizeX += x - rangeX.last
        }

        if (y < rangeY.first) {
            grid.forEach { dim ->
                dim.forEach { layer ->
                    for (y1 in y until rangeY.first) {
                        layer.add(0, MutableList(rangeX.count()) { default })
                    }
                }
            }
            sizeY += rangeY.first - y
            offsetY = y
        } else if (y > rangeY.last) {
            grid.forEach { dim ->
                dim.forEach { layer ->
                    for (y1 in rangeY.last until y) {
                        layer.add(MutableList(rangeX.count()) { default })
                    }
                }
            }
            sizeY += y - rangeY.last
        }

        if (z < rangeZ.first) {
            grid.forEach { dim ->
                for (z1 in z until rangeZ.first) {
                    dim.add(0, MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } })
                }
            }
            sizeZ += rangeZ.first - z
            offsetZ = z
        } else if (z > rangeZ.last) {
            grid.forEach { dim ->
                for (z1 in rangeZ.last until z) {
                    dim.add(MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } })
                }
            }
            sizeZ += z - rangeZ.last
        }

        if (w < rangeW.first) {
            for (w1 in w until rangeW.first) {
                grid.add(0, MutableList(rangeZ.count()) { MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } } })
            }
            sizeW += rangeW.first - w
            offsetW = w
        } else if (w > rangeW.last) {
            for (w1 in rangeW.last until w) {
                grid.add(MutableList(rangeZ.count()) { MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } } })
            }
            sizeW += w - rangeW.last
        }

        grid[w-offsetW][z-offsetZ][y-offsetY][x-offsetX] = value
    }

    fun deepcopy(): Grid4D<T> {
        return Grid4D(this.grid.map { dim -> dim.map { layer -> layer.map { row -> row.map { it } } } }, default, offsetX, offsetY, offsetZ, offsetW)
    }
}