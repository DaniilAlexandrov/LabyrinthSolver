package view

import core.MazeGen
import core.MazeSolver
import core.structures.Node

class MazeController(private val size: Int) {

    private val solver = MazeSolver(size, size)

    internal var startNode = Node(-1, -1)
    internal var endNode = Node(-1, -1)

    val walkableNodes = solver.getWalkableNodes()
    val sWidth = solver.width
    val sHeight = solver.height

    fun isObstacle(node: Node) = solver.isObstacle(node)
    fun isWall(x: Int, y: Int) = solver.maze[x, y] == MazeGen.wall
    fun findPath() = solver.findPath(startNode, endNode)
    val startAndDestinationExist = startNode != Node(-1, -1) && endNode != Node(-1, -1)
}