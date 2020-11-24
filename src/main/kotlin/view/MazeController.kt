package view

import core.MazeGen
import core.MazeSolver
import core.structures.Node

class MazeController(size: Int) {

    private val solver = MazeSolver(size, size)

    internal var startNode = Node(-1, -1)
    internal var endNode = Node(-1, -1)

    internal val sWidth = solver.width
    internal val sHeight = solver.height

    internal fun startNodeIsChanged(): Boolean {
        return startNode != Node(-1, -1)
    }

    internal fun endNodeIsChanged(): Boolean {
        return endNode != Node(-1, -1)
    }

    internal fun isObstacle(node: Node) = solver.isObstacle(node)
    internal fun isWall(x: Int, y: Int) = solver.maze[x, y] == MazeGen.wall
    internal fun findPath() = if (startNode != Node(-1, -1) && endNode != Node(-1, -1))
        solver.findPath(startNode, endNode) else emptyList()
}