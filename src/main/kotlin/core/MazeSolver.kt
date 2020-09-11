package core

import core.structures.Matrix
import core.structures.Node

class MazeSolver(val width: Int, val height: Int) {

    private val gen =
            MazeGen(width, height, 0, 3, 4, 70)
    internal val maze = gen.generateMaze()

    private val obstacles = generateObstacles(maze)

    private val pathfinding = Pathfinding(width, height, obstacles)

    private fun generateObstacles(maze: Matrix<Char>):MutableList<Node> {
        val resList = mutableListOf<Node>()
        for (x in 0 until maze.width)
            for (y in 0 until maze.height)
                if (maze[x,y] == MazeGen.wall)
                    resList.add(Node(x, y))
        return resList
    }

    private fun locateTopLeftWalkableNode(maze: Matrix<Char>): Node {
        for (y in 0 until maze.height)
            for (x in 0 until maze.width)
                if (!obstacles.contains(Node(x, y)))
                    return Node(x, y)
        return Node(0, 0)
    }

    private fun locateBottomRightWalkableNode(maze: Matrix<Char>): Node {
        for (x in maze.width - 2 downTo 0)
            for (y in maze.height - 2 downTo  0)
                if (!obstacles.contains(Node(x, y)))
                    return Node(x, y)
        return Node(0, 0)
    }

    private fun locateBottomLeftWalkableNode(maze: Matrix<Char>): Node {
        for (x in maze.width - 2 downTo 0)
            for (y in 0 until maze.height)
                if (!obstacles.contains(Node(x, y)))
                    return Node(x, y)
        return Node(0, 0)
    }

    private fun locateTopRightWalkableNode(maze: Matrix<Char>): Node {
        for (x in 0 until maze.width)
            for (y in maze.height - 2 downTo  0)
                if (!obstacles.contains(Node(x, y)))
                    return Node(x, y)
        return Node(0, 0)
    }

    internal fun locateCornerNode(top: Boolean, left: Boolean): Node {
        return when (top to left) {
            true to true -> locateTopLeftWalkableNode(maze)
            true to false -> locateTopRightWalkableNode(maze)
            false to true -> locateBottomLeftWalkableNode(maze)
            else -> locateBottomRightWalkableNode(maze)
        }
    }
    fun configure() {
        val start = locateTopLeftWalkableNode(maze)
        val end = locateBottomRightWalkableNode(maze)
        val path = pathfinding.findPath(start.x, start.y, end.x, end.y)
        if (path.isNotEmpty()){
            for (i in path.indices) {
                maze[path[i].x, path[i].y] = '*'
            }
            maze[end.x, end.y] = 'F'
            maze[start.x, start.y] = 'S'
        }
        else println("Path could not be found")
    }

    internal fun findPath(start: Node, end: Node): List<Node> = pathfinding.findPath(start.x, start.y, end.x, end.y)

    internal fun isObstacle(node: Node) = obstacles.contains(node)

    internal fun getWalkableNodes(): List<Node> {
        val resList = mutableListOf<Node>()
        for (x in 0 until maze.width)
            for (y in 0 until maze.height) {
                val node = Node(x, y)
                if (!obstacles.contains(node))
                    resList.add(node)
            }
        return resList
    }
}