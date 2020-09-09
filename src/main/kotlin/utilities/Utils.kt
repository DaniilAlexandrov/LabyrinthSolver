package utilities

import core.MazeGen
import java.io.File
import kotlin.random.Random

object Utils {

    fun <T> List<T>.randomElement(): T {
        val rnd = Random.nextInt(0, this.size)
        return this[rnd]
    }

    fun writeIntoFile(path: String, contents: MazeGen) {
        val file = File(path)
        file.writeText(contents.toString())
    }

    fun readFromFile(path: String): List<String> {
        val file = File(path)
        return file.readLines().toMutableList()
    }
}