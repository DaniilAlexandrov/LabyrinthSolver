import core.Pathfinding
import core.structures.Node
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PathfindingTests {

    private val pf = Pathfinding(5, 5, emptyList())

    @Test
    fun sameStartDestinationPoint() {
        val path = pf.findPath(0, 0, 0, 0)
        assertEquals(path.size, 1)
        assertEquals(path.first(), Node(0,0))
    }

    @Test
    fun shortPathWithoutObstacles() {
        val path = pf.findPath(0, 0, 4, 4)
        val expectedPath = listOf(Node(0,0), Node(1, 0), Node(1,1),
                Node(2, 1), Node(2,2), Node(3, 2), Node(3, 3), Node(4, 3), Node(4,4))
        assertEquals(path, expectedPath)
    }

    @Test
    fun storyPathWithObstacles() {
        val pathfinding = Pathfinding(5, 5, listOf(Node(1, 1), Node(1, 0)))
        val path = pathfinding.findPath(0,0, 2, 2)
        val expectedPath = listOf(Node(0,0), Node(0, 1), Node(0, 2), Node(1, 2),
                Node(2, 2))
        assertEquals(path, expectedPath)
    }
}