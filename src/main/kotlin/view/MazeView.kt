package view

import core.structures.Node
import utilities.Utils.numberRegex
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import tornadofx.*


class MazeView: View() {

    override val root = Pane()

    private val pathColour = c("7e75ff")

    private val spaceColour = Color.WHITE
    private val startColour = Color.GREEN
    private val finishColour = Color.RED
    private val wallColour = Color.BLACK

    private lateinit var controller: MazeController
    private lateinit var alertPopup: Label

    private var paintedNodes = mutableListOf<Node>()

    private var rectNodeDictionary = mutableMapOf<Rectangle, Node>()
    private val minMazeSideConstraint = 5
    private val maxMazeSideConstraint = 150
    private val widthOffset = 1
    private val heightOffset = 37
    private val popupOffset = 100
    private val buttonOffset = 10
    private val mazeButtonOffset = 572.0

    init {
        initializeIntroDialogue()
    }

    private fun initializeIntroDialogue(): TextInputDialog {
        var dialogue = TextInputDialog()
        dialogue.title = "Maze size"
        dialogue.headerText = ""
        dialogue.contentText = "Maze size length (random number between $minMazeSideConstraint and $maxMazeSideConstraint)"

        if (dialogue.showAndWait().isPresent) {
            val tmpRes = dialogue.result
            if (tmpRes.matches(numberRegex) && tmpRes.toInt() in minMazeSideConstraint..maxMazeSideConstraint) {
                if (tmpRes.toInt() % 2 == 0)
                    initializeScene(root, tmpRes.toInt() + 1)
                else
                    initializeScene(root, tmpRes.toInt())
            }
            else
                dialogue = initializeIntroDialogue()
        } else {
            Platform.exit()
        }
        return dialogue
    }

    private fun initializeScene(root: Pane, sideSize: Int) {
        controller = MazeController(sideSize)
        with(root) {
            title = "Labyrinth"
            val preferredWidth = 750.0
            val boxWidth = preferredWidth / sideSize
            val preferredHeight = 750.0
            val boxHeight = preferredHeight / sideSize
            prefWidth = sideSize * boxWidth - widthOffset
            prefHeight = sideSize * boxHeight + heightOffset
            var xPos = -boxWidth
            var yPos: Double
            for (x in 0 until controller.sWidth) {
                yPos = -boxHeight
                xPos += boxWidth
                for (y in 0 until controller.sHeight) {
                    yPos += boxHeight
                    val rect = rectangle {
                        width = boxWidth
                        height = boxHeight
                        layoutX = xPos
                        layoutY = yPos
                        fill = if (controller.isWall(x, y))
                            wallColour
                        else
                            spaceColour
                    }
                    rectNodeDictionary[rect] = Node(x, y)
                    generateCallbacks(rect)
                }
            }
            alertPopup = label {
                font = Font.font(25.0)
                layoutY = sideSize * boxHeight
                layoutX += popupOffset
                text = ""
            }
            add(alertPopup)
            hbox {
                layoutY = preferredHeight
                button("Find Path") {
                    layoutY = sideSize * boxHeight + buttonOffset
                    action {
                        val tmpTime = System.currentTimeMillis()
                        val path = controller.findPath()
                        val pathTime = System.currentTimeMillis() - tmpTime
                        when {
                            path.size  < 2 -> setAlertPopup("Specify start and destination points")
                            path.size == 2 -> setAlertPopup("Select nonadjacent points")
                            path.isEmpty() && controller.startNodeIsChanged()
                                    && controller.endNodeIsChanged() -> setAlertPopup("Path cannot be found")
                            else -> {
                                markPath(path)
                                setAlertPopup("Path of ${path.size - 2} elements calculated for $pathTime ms")
                            }
                        }
                    }
                }
                spacing = mazeButtonOffset
                button("Regenerate Maze") {
                    action {
                        reinitializeScene(sideSize)
                    }
                }
            }
        }
    }

    private fun reinitializeScene(sideSize: Int) {
        root.clear()
        rectNodeDictionary = mutableMapOf()
        paintedNodes = mutableListOf()
        controller = MazeController(sideSize)
        initializeScene(root, sideSize)
    }

    private fun setAlertPopup(text: String) {
            alertPopup.text = text
    }

    private fun generateCallbacks(rect: Rectangle) {
        with(rect) {
            setOnMousePressed {
                if (it.button == MouseButton.PRIMARY && !controller.isObstacle(rectNodeDictionary[rect]!!)) {
                    tagAsStart(rect)
                }
                else if (it.button == MouseButton.SECONDARY && !controller.isObstacle(rectNodeDictionary[rect]!!))
                    tagAsFinish(rect)
            }
        }
    }

    private fun getRect(node: Node): Rectangle {
        for (i in rectNodeDictionary) {
            if (i.value == node)
                return i.key
        }
        return Rectangle()
    }

    private fun tagAsStart(rect: Rectangle) {
        getRect(controller.startNode).fill = spaceColour
        paintedNodes.forEach { getRect(it).fill = spaceColour }
        rect.fill = startColour
        controller.startNode = rectNodeDictionary[rect] ?: Node()
    }

    private fun tagAsFinish(rect: Rectangle) {
        getRect(controller.endNode).fill = spaceColour
        paintedNodes.forEach {  getRect(it).fill = spaceColour }
        rect.fill = finishColour
        controller.endNode = rectNodeDictionary[rect] ?: Node()
    }

    private fun markPath(path: List<Node>) {
        paintedNodes.clear()
        path.filter { it != path.first() && it != path.last() }.forEach {
            paintedNodes.add(it)
            getRect(it).fill = pathColour
        }
    }
}
