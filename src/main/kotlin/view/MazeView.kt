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
    private val size = 51
    private val pathColour = c("7e75ff")

    private lateinit var controller: MazeController

    private val rectNodeDictionary = mutableMapOf<Rectangle, Node>()
    private lateinit var alertPopup: Label

    private val minMazeSideConstraint = 5
    private val maxMazeSideConstraint = 101
    private val widthOffset = 1
    private val heightOffset = 35
    private val popupOffset = 125
    private val buttonOffset = 10

    init {
        initializeIntroDialogue()
    }

    private fun initializeIntroDialogue(): TextInputDialog {
        var dialogue = TextInputDialog()
        dialogue.title = "Maze size"
        dialogue.headerText = ""
        dialogue.contentText = "Maze size length (odd number between $minMazeSideConstraint and $maxMazeSideConstraint)"

        if (dialogue.showAndWait().isPresent) {
            val tmpRes = dialogue.result
            if (tmpRes.matches(numberRegex) && tmpRes.toInt() % 2 == 1 &&
                    tmpRes.toInt() in minMazeSideConstraint..maxMazeSideConstraint)
                initializeScene(root, tmpRes.toInt())
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
            val preferredWidth = 600.0
            val boxWidth = (preferredWidth / controller.sWidth).toInt().toDouble()
            val preferredHeight = 600.0
            val boxHeight = (preferredHeight / controller.sHeight).toInt().toDouble()
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
                            Color.BLACK
                        else
                            Color.WHITE
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
            button("Find Path") {
                layoutY = sideSize * boxHeight + buttonOffset
                action {
                    val path = controller.findPath()
                    if (path.isEmpty())
                        setAlertPopup(true, "Specify start and destination points")
                    else
                        setAlertPopup(false, "")
                    markPath(path)
                }
            }
        }
    }
    private fun setAlertPopup(active: Boolean, text: String) {
        if (!active)
            alertPopup.text = ""
        else
            alertPopup.text = text
    }

    private fun generateCallbacks(rect: Rectangle) {
        with(rect) {
            setOnMousePressed {
                if (it.button == MouseButton.PRIMARY && !controller.isObstacle(rectNodeDictionary[rect]!!))
                    tagAsStart(rect)
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
        controller.walkableNodes.filter { it != controller.endNode }.forEach { getRect(it).fill = Color.WHITE }
        rect.fill = Color.GREEN
        controller.startNode = rectNodeDictionary[rect] ?: Node()
    }

    private fun tagAsFinish(rect: Rectangle) {
        controller.walkableNodes.filter { it != controller.startNode }.forEach { getRect(it).fill = Color.WHITE }
        rect.fill = Color.RED
        controller.endNode = rectNodeDictionary[rect] ?: Node()
    }

    private fun markPath(path: List<Node>) {
        path.filter { it != path.first() && it != path.last() }.forEach { getRect(it).fill = pathColour }
    }
}
