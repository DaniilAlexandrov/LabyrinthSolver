package core

import core.structures.*
import java.lang.Exception
import java.util.*
import kotlin.random.Random
import utilities.Utils.randomElement

class MazeGen(private val stageWidth: Int, private val stageHeight: Int, private val numberRoomTries: Int,
              private val roomExtraSize: Int, private val extraConnectorChance: Int,
              private val windingPercent: Int, private val removeDeadEnds: Boolean) {

    private val rooms = mutableListOf<Rect>()

    private val maze = createMatrix(stageWidth, stageHeight, ' ')

    companion object {
        const val wall = '#'
        const val floor = ' '
        const val corridor = ' '
        const val door = ' '
        const val player = '@'
        const val light = '*'
        val cardinals = arrayOf(
                Vector2(0, -1), // Up
                Vector2(0, 1),  // Down
                Vector2(-1, 0),  // Left
                Vector2(1, 0)   // Right
        )
        val up = Vector2(0, -1)
        val down = Vector2(0, 1)
        val left = Vector2(-1, 0)
        val right = Vector2(1, 0)
    }

    fun generateMaze(): Matrix<Char> {
        require (stageWidth % 2 != 0 || stageHeight % 2 != 0)
        fill(wall)
        addRooms()
        for (i in 1 until stageHeight step 2)
            for (j in 1 until stageWidth step 2)
                growMaze(j, i)
        connectRegions()
        if (removeDeadEnds)
            removeDeadEnds()

        generateUnpassableBounds()

        return maze
    }

    private fun addRooms() {
        for (i in 0 until numberRoomTries) {
            val size = Random.nextInt(1, 3 + roomExtraSize)
            val rectangularity = Random.nextInt(0, 1 + size / 2) * 2
            var width = size
            var height = size

            if (Random.nextDouble(0.0, 1.0) > 0.5)
                width += rectangularity
            else
                height += rectangularity

            val x = Random.nextInt(0, (stageWidth - width) / 2) * 2 + 1
            val y = Random.nextInt(0, (stageHeight - width) / 2) * 2 + 1

            val room = Rect(x, y, width, height)
            var overlaps = false

            for (j in 0 until rooms.count()) {
                if (room.overlaps(rooms[j])) {
                    overlaps = true
                    break
                }
            }
            if (overlaps) continue

            rooms.add(room)
            for (pos in 0 until width * height)
                carve((pos % width) + x, (pos / width) + y)
        }
    }

    private fun setTile(x: Int, y: Int, type: Char) {
        maze[x,y] = type
    }

    private fun setTile(pos: Vector2, type: Char) {
        setTile(pos.x, pos.y, type)
    }

    private fun getTile(pos: Vector2): Char = maze[pos.x, pos.y]

    private fun fill(type: Char) {
        for (i in 0 until stageWidth)
            for (j in 0 until stageHeight)
                maze[i, j] = type
    }

    private fun canCarve(nextCell: Vector2) =
            (nextCell.x > 0 && nextCell.y > 0 && nextCell.x < stageWidth && nextCell.y < stageHeight &&
                    maze[nextCell.x, nextCell.y] == wall)


    private fun carve(posX: Int, posY: Int, type: Char = '0') {
        var temp = ' '
        if (type == '0')
            temp = floor
        try {
            setTile(posX, posY, temp)
        } catch (e: Exception) { // Worth thinking
            //println("Mayday")
        }
    }

    private fun growMaze(startX: Int, startY: Int) {
        val cells = LinkedList<Vector2>()
        var lastDirection = Vector2()

        carve(startX, startY, corridor)

        cells.addLast(Vector2(startX, startY))

        var loopAvoidance = 0

        while (cells.count() != 0 && loopAvoidance < 10000) {
            loopAvoidance++
            val cell = cells.last()
            val unmadeCells = mutableListOf<Vector2>()
            for (element in cardinals) {

                val tmp1 = Vector2(cell.x + element.x, cell.y + element.y) // FIX OPERATOR OVERLOAD
                val tmp2 = Vector2(cell.x + element.x * 2, cell.y + element.y * 2)

                if (canCarve(tmp1) && canCarve(tmp2))
                    unmadeCells.add(element)
            }
                if (unmadeCells.count() != 0) {
                    val dir: Vector2 = if (unmadeCells.contains(lastDirection) && Random.nextInt(0, 100) < 100 - windingPercent)
                        lastDirection
                    else {
                        unmadeCells.randomElement()
                    }

                    val tmp3 = Vector2(cell.x + dir.x, cell.y + dir.y) // FIX OPERATOR OVERLOAD
                    val tmp4 = Vector2(cell.x + dir.x * 2, cell.y + dir.y * 2) // FIX OPERATOR OVERLOAD

                    carve(tmp3.x, tmp3.y, corridor)
                    carve(tmp4.x, tmp4.y, corridor)

                    cells.addLast(tmp4)
                    lastDirection = dir
                }
                else {
                    cells.removeLast()
                    lastDirection = Vector2()
                }
            }
        }
    private fun connectRegions() {
        for (i in 0 until rooms.count()) {
            var connections = 0
            var lastSide = -1
            var doorTries = 0
            val moreDoors = if (Random.nextInt(0, 100) > 100 - extraConnectorChance) 1 else 0

            while (connections < 2 + moreDoors && doorTries < 100) {
                doorTries++
                var side = Random.nextInt(0, 15) % 4
                if (side == lastSide)
                    side = (side + Random.nextInt(0, 3)) % 4

                val line: Line
                val direction: Vector2

                when (side) {
                    0 -> {
                        line = Line(rooms[i].x + 1, rooms[i].y, rooms[i].xMax - 1, rooms[i].y)
                        direction = up
                    }
                    1 -> {
                        line = Line(rooms[i].xMax + 1, rooms[i].y + 2, rooms[i].xMax + 1, rooms[i].yMax)
                        direction = right
                    }
                    2 -> {
                        line = Line(rooms[i].x + 2, rooms[i].yMax + 1, rooms[i].xMax, rooms[i].yMax + 1)
                        direction = down
                    } else -> {
                    line = Line(rooms[i].x, rooms[i].y + 1, rooms[i].x, rooms[i].yMax - 1)
                    direction = left
                }
                }
                val randomX = if (line.end.x > line.start.x) Random.nextInt(line.start.x, line.end.x) - 1 else line.start.x
                val randomY = if (line.end.y > line.start.y )Random.nextInt(line.start.y, line.end.y) - 1 else line.start.y

                if (canPlaceDoor(randomX, randomY, direction)) {
                    maze[randomX, randomY] = door
                    connections++
                    if (connections >= 2)
                        lastSide = -1
                }
            }
        }
    }

    private fun canPlaceDoor(randomX: Int, randomY: Int, direction: Vector2): Boolean =
         randomY + direction.y >= 0 && randomX + direction.x >= 0 &&
                randomY + direction.y < maze.height && randomX + direction.x < maze.width &&
                noDoorsAround(randomX, randomY) && maze[randomX + direction.x, randomY + direction.y] == floor


    private fun noDoorsAround(x: Int, y: Int): Boolean {
        if (maze[x,y] == door)
            return false
        for (i in cardinals.indices) {
            if (x + cardinals[i].x >= 0 && y + cardinals[i].y >= 0 &&
                    x + cardinals[i].x < maze.width && y + cardinals[i].y < maze.height &&
                    maze[x + cardinals[i].x, y + cardinals[i].y] == door)
                return false
        }
        return true
    }

    private fun removeDeadEnds() {
        var done = false
        while (!done) {
            done = true
            for (i in 0 until stageWidth * stageHeight) {
                val pos = Vector2(i % stageWidth, i / stageWidth)
                if (getTile(pos) == wall)
                    continue
                var exits = 0
                for (element in cardinals) {
                    val dir = element
                    val tmp6 = Vector2(pos.x + dir.x, pos.y + dir.y)

                    if (getTile(tmp6) != wall)
                        exits++

                    if (exits != 1)
                        continue
                    done = false
                    setTile(pos, wall)
                }
            }
        }
    }

    private fun generateUnpassableBounds() {
        for (x in 0 until maze.width) {
            setTile(x, 0, wall)
            setTile(x, maze.height - 1, wall)
        }
        for (y in 0 until maze.height) {
            setTile(0, y, wall)
            setTile(maze.width - 1, y, wall)
        }

    }
    override fun toString(): String {
        return maze.toString()
    }
}
