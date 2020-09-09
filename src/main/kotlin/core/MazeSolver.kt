package core

import core.structures.Matrix
import core.structures.Node

class MazeSolver(private val width: Int, private val height: Int) {

    val maze = MazeGen(width, height, 0, 3, 4, 50, false)
            .generateMaze()
    private val obstacles = generateObstacles(maze)

    private val pathfinding = Pathfinding(width, height, obstacles)

    private fun generateObstacles(maze: Matrix<Char>):MutableList<Node> {
        val resList = mutableListOf<Node>()
        for (x in 0 until maze.width)
            for (y in 0 until maze.height)
                if (maze[x,y] == '#')
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

}

fun main() {
    val a = MazeSolver(51, 51)
    a.configure()
    println(a.maze)
}
