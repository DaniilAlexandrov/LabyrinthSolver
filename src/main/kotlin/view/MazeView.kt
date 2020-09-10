package view

import core.structures.Node
import javafx.scene.control.Label
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

    private var controller = MazeController(size)

    private val preferredWidth = 600.0
    private val boxWidth = (preferredWidth / controller.sWidth).toInt().toDouble()
    private val preferredHeight = 600.0
    private val boxHeight = (preferredHeight / controller.sHeight).toInt().toDouble()


    private val rectNodeDictionary = mutableMapOf<Rectangle, Node>()
    private var alertPopup: Label

    //var test = ""

    init {
        /*dialog {
            label {
                font = Font.font(20.0)
                text = "Enter maze side length"
            }
            spacing = 10.0
            val field = textfield()
            add(field)
            button {
                text = "OK"
                action {
                    test = field.text
                    println(test.toInt())
                    controller = initializeController(test.toInt())
                }
            }
        }*/
        title = "Labyrinth"
        with(root) {
            prefWidth = size * boxWidth - 1
            prefHeight = size * boxHeight + 50
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
                layoutY = size * boxHeight
                layoutX += 125.0
                text = ""
            }
            add(alertPopup)
            button("Find Path") {
                layoutY = size * boxHeight + 10
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

    //private fun initializeController(size: Int): MazeController = MazeController(size)

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
        path.filter { it != path.first() }.filter { it != path.last() }.forEach { getRect(it).fill = pathColour }
    }
}
