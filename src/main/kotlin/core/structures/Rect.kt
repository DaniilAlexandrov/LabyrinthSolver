package core.structures

data class Rect(val x: Int, val y:Int, val width: Int, val height:Int) {

    private val bottomLeft = Coordinate(x, y)
    private val topLeft = Coordinate(x, y + height)
    private val bottomRight = Coordinate(x + width, y)
    private val topRight = Coordinate(x + width, y + height)

    fun overlaps(other: Rect): Boolean {
        if (this.topRight.y < other.bottomLeft.y || this.bottomLeft.y > other.topRight.y)
            return false
        if (this.topRight.x < other.bottomLeft.x || this.bottomLeft.x > other.topRight.x)
            return false
        return true
    }
    val xMax = x + width
    val yMax = y + height
}