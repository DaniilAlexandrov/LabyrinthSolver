package core.structures

import kotlin.math.pow
import kotlin.math.sqrt

class Line(private val x1: Int, private val y1: Int, private val x2: Int, private val y2: Int) {

    val start = Vector2(x1, y1)

    val end = Vector2(x2, y2)

    fun length(): Float {
        val dX = x2- x1
        val dY = y2 - y1
        return sqrt(dX.toFloat().pow(2) + dY.toFloat().pow(2))
    }

    override fun equals(other: Any?): Boolean =
        other is Line && this.length() == other.length()


    override fun hashCode(): Int {
        var result = x1.hashCode()
        result = 31 * result + y1.hashCode()
        result = 31 * result + x2.hashCode()
        result = 31 * result + y2.hashCode()
        return result
    }

    override fun toString(): String {
        return "($x1;$y1) -> ($x2;$y2)"
    }
}