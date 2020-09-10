package core.structures

import kotlin.math.pow
import kotlin.math.sqrt

class Vector2(val x: Int, val y: Int) {

    constructor(): this(0,0)

    override fun equals(other: Any?): Boolean =
        other is Vector2 && this.length() == other.length()

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "($x;$y)"
    }
    fun length(): Float {
        return sqrt(x.toFloat().pow(2) + y.toFloat().pow(2))
    }

    operator fun plus(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)
    operator fun times(factor: Int): Vector2 = Vector2(x * factor, y * factor)
}
