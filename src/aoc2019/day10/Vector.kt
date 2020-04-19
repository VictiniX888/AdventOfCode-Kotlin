package aoc2019.day10

import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.sqrt

data class Vector(val x: Int, val y: Int) {

    operator fun plus(increment: Vector): Vector = Vector(x + increment.x, y + increment.y)

    operator fun minus(increment: Vector): Vector = Vector(x - increment.x, y - increment.y)

    private tailrec fun gcd(int1: Int, int2: Int): Int {       //using Euclid's algorithm
        return when {
            int1 == 0 && int2 == 0 -> 1
            int1 == 0 -> int2
            int2 == 0 -> int1
            int1 == 1 || int2 == 1 -> 1
            int1 == int2 -> int1
            int1 >  int2 -> gcd(int1 - int2, int2)
            int1 <  int2 -> gcd(int1, int2 - int1)
            else -> error("Error: Greatest common denominator could not be found")
        }
    }

    fun simplify(): Vector {
        val gcd = gcd(x.absoluteValue, y.absoluteValue)
        return Vector(x/gcd, y/gcd)
    }

    val length by lazy { sqrt((x*x + y*y).toDouble()) }

    // If we assume the vector as a complex number, theta gives the argument of the complex number
    val theta by lazy { atan2(y.toDouble(), x.toDouble()) }
}