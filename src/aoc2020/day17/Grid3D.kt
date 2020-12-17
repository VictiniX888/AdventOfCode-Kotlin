package aoc2020.day17

class Grid3D<T>(initGrid: List<List<List<T>>>,
                private val default: T,
                private var offsetX: Int = 0,
                private var offsetY: Int = 0,
                private var offsetZ: Int = 0) {
    private val grid = mutableListOf<MutableList<MutableList<T>>>()

    // private var offsetX = 0     // +ve x = right
    // private var offsetY = 0     // +ve y = down
    // private var offsetZ = 0     // +ve z = "down"

    private var sizeX = initGrid[0][0].size
    private var sizeY = initGrid[0].size
    private var sizeZ = initGrid.size

    val rangeX: IntRange
        get() = offsetX until offsetX + sizeX
    val rangeY: IntRange
        get() = offsetY until offsetY + sizeY
    val rangeZ: IntRange
        get() = offsetZ until offsetZ + sizeZ

    init {
        // NOTE: initGrid cannot be empty and all lines must be equal length
        initGrid.forEach { layer ->
            grid.add(layer.map { it.toMutableList() }.toMutableList())
        }
    }

    operator fun get(x: Int, y: Int, z: Int): T {
        return if (x in rangeX && y in rangeY && z in rangeZ) grid[z-offsetZ][y-offsetY][x-offsetX] else default
    }

    operator fun set(x: Int, y: Int, z: Int, value: T) {
        // resize grid
        if (x < rangeX.first) {
            grid.forEach { layer ->
                layer.forEach { row ->
                    for (x1 in x until rangeX.first) {
                        row.add(0, default)
                    }
                }
            }
            sizeX += rangeX.first - x
            offsetX = x
        } else if (x > rangeX.last) {
            grid.forEach { layer ->
                layer.forEach { row ->
                    for (x1 in rangeX.last until x) {
                        row.add(default)
                    }
                }
            }
            sizeX += x - rangeX.last
        }

        if (y < rangeY.first) {
            grid.forEach { layer ->
                for (y1 in y until rangeY.first) {
                    layer.add(0, MutableList(rangeX.count()) { default })
                }
            }
            sizeY += rangeY.first - y
            offsetY = y
        } else if (y > rangeY.last) {
            grid.forEach { layer ->
                for (y1 in rangeY.last until y) {
                    layer.add(MutableList(rangeX.count()) { default })
                }
            }
            sizeY += y - rangeY.last
        }

        if (z < rangeZ.first) {
            for (z1 in z until rangeZ.first) {
                grid.add(0, MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } })
            }
            sizeZ += rangeZ.first - z
            offsetZ = z
        } else if (z > rangeZ.last) {
            for (z1 in rangeZ.last until z) {
                grid.add(MutableList(rangeY.count()) { MutableList(rangeX.count()) { default } })
            }
            sizeZ += z - rangeZ.last
        }

        grid[z-offsetZ][y-offsetY][x-offsetX] = value
    }

    fun deepcopy(): Grid3D<T> {
        return Grid3D(this.grid.map { layer -> layer.map { row -> row.map { it } } },
            default, offsetX, offsetY, offsetZ)
    }

    fun print() {
        grid.forEach { layer ->
            layer.forEach { row ->
                println(row)
            }
            println()
        }
    }
}