package core.structures

data class Node(var x: Int, var y: Int): Comparable<Node> {

    var gCost = 0
    var hCost = 0
    var fCost = 0
    var previousNode: Node? = null
    var isWalkable = true

    fun calculateFCost() {
        fCost = hCost + gCost
    }
    constructor(): this(0,0)

    override fun compareTo(other: Node): Int {
        return if (this.fCost < other.fCost)
            -1
        else
            1
    }
}