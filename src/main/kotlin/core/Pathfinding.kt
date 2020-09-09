package core

import core.structures.Node
import core.structures.createMatrix
import kotlin.math.abs
import kotlin.math.min

// A* algorithm with static obstacles.
class Pathfinding(private val width: Int, private val height: Int, private val obstacles:List<Node>) {

    private val grid = createMatrix(width, height, Node())
    private val moveStraightCost = 10
    private val moveDiagonallyCost = 14
    private var openList = mutableListOf<Node>()
    private var closedList = mutableListOf<Node>()

    init {
        for (x in 0 until width)
            for (y in 0 until height) {
                grid[x, y] = Node(x, y)
            }
    }

    fun findPath(startX: Int, startY: Int, endX: Int, endY: Int): List<Node> {
        val startNode = getNode(startX, startY)
        val endNode = getNode(endX, endY)

        manageUnpassability(obstacles)

        openList = mutableListOf(startNode)
        closedList = mutableListOf()
        for (x in 0 until width)
            for(y in 0 until  height) {
                val node = getNode(x, y)
                node.gCost = Int.MAX_VALUE
                node.calculateFCost()
                node.previousNode = null
            }
        startNode.gCost = 0
        startNode.hCost = calculateDistanceCost(startNode, endNode)
        startNode.calculateFCost()

        while(openList.count() > 0) {
            val currentNode = getLowestFCostNode(openList)
            if (currentNode == endNode)
                return calculatePath(endNode)
            openList.remove(currentNode)
            closedList.add(currentNode)

            for (neighbourNode in getNeighbours(currentNode)) {
                if (closedList.contains(neighbourNode)) continue
                if (!neighbourNode.isWalkable/*obstacles.contains(neighbourNode)*/) {
                    closedList.add(neighbourNode)
                    continue
                }
                val tempGCost = currentNode.gCost + calculateDistanceCost(currentNode, neighbourNode)
                if (tempGCost < neighbourNode.gCost) {
                    neighbourNode.previousNode = currentNode
                    neighbourNode.gCost = tempGCost
                    neighbourNode.hCost = calculateDistanceCost(neighbourNode, endNode)
                    neighbourNode.calculateFCost()
                    if (!openList.contains(neighbourNode)) {
                        openList.add(neighbourNode)
                    }
                }
            }
        }
        return emptyList()
    }

    private fun manageUnpassability(obstacles: List<Node>) {
        for (node in obstacles) {
            getNode(node).isWalkable = false
        }
    }

    private fun getNeighbours(currentNode: Node): List<Node> {
        val neighbourList = mutableListOf<Node>()
        if (currentNode.x - 1 >= 0) {
            neighbourList.add(getNode(currentNode.x - 1, currentNode.y)) // LEFT
            //if (currentNode.y - 1 >= 0) neighbourList.add(getNode(currentNode.x - 1, currentNode.y - 1)) // LEFT DOWN
            //if (currentNode.y + 1 < height) neighbourList.add(getNode(currentNode.x - 1, currentNode.y + 1)) // LEFT UP
        }
        if (currentNode.x + 1 < width) {
            neighbourList.add(getNode(currentNode.x + 1, currentNode.y)) // RIGHT
            //if (currentNode.y - 1 >= 0) neighbourList.add(getNode(currentNode.x + 1, currentNode.y - 1)) // RIGHT DOWN
            //if (currentNode.y + 1 < height) neighbourList.add(getNode(currentNode.x + 1, currentNode.y + 1)) // RIGHT UP
        }
        if (currentNode.y - 1 >= 0) neighbourList.add(getNode(currentNode.x, currentNode.y - 1)) // DOWN
        if (currentNode.y + 1 < height) neighbourList.add(getNode(currentNode.x, currentNode.y + 1)) // UP
        return neighbourList
    }

    private fun getNode(x: Int, y: Int) = grid[x, y]
    private fun getNode(node: Node) = grid[node.x, node.y]

    private fun calculateDistanceCost(a: Node, b: Node): Int {
        val xDistance = abs(a.x - b.x)
        val yDistance = abs(a.y - b.y)
        val remaining = abs(xDistance - yDistance)
        return moveDiagonallyCost * min(xDistance, yDistance) + moveStraightCost * remaining
    }

    private fun calculatePath(endNode: Node): List<Node> {
        val path = mutableListOf(endNode)
        var currentNode = endNode
        while (currentNode.previousNode != null) {
            currentNode.previousNode?.let { path.add(it) }
            currentNode = currentNode.previousNode!!
        }
        path.reverse()
        return path
    }

    private fun getLowestFCostNode(nodes: List<Node>): Node { // LAMBDA ONELINER?
        var res = nodes[0]
        for (i in nodes.indices) {
            if (nodes[i].fCost < res.fCost)
                res = nodes[i]
        }
        return res
    }
}