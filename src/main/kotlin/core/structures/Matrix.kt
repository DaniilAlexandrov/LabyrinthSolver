package core.structures

data class Cell(val row: Int, val column: Int)

interface Matrix<E> {
    val height: Int

    val width: Int

    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)

    fun getRow(y: Int): List<E>
    //fun getColumn(x: Int): List<E>
}

fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {
    require(height > 0 && width > 0)
    return MatrixImpl(height, width, e)
}

class MatrixImpl<E>(override val width: Int, override val height: Int, e: E) : Matrix<E> {

    private val matrixSkeleton = MutableList(height) { MutableList(width) { e } }

    private fun isEligible(row: Int, column: Int) = column in 0 until width && row in 0 until height

    override fun get(row: Int, column: Int): E = if (!isEligible(row, column))
        throw IllegalArgumentException()
    else matrixSkeleton[row][column]

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        require(isEligible(row, column))
        matrixSkeleton[row][column] = value
    }

    override fun set(cell: Cell, value: E) {
        set(cell.row, cell.column, value)
    }

    override fun getRow(y: Int): List<E> {
        val res = mutableListOf<E>()
        for (x in 0 until width)
            res.add(matrixSkeleton[y][x])
        return res
    }

/*    override fun getColumn(x: Int): List<E> {
        val res = mutableListOf<E>()
        for (y in 0 until height)
            res.add(matrixSkeleton[x][y])
        return res
    }*/

    override fun equals(other: Any?) =
            other is MatrixImpl<*> && height == other.height && width == other.width &&
                    matrixSkeleton == other.matrixSkeleton


    override fun toString(): String {
        val builder = StringBuilder()
        //builder.append("[")
        for (row in 0 until height) {
            //builder.append("[")
            for (column in 0 until width) {
                builder.append(this[row, column])
                if (row != height && column != width) builder.append(" ")
            }
            //builder.append("]")
            if (row != height - 1)
            builder.append("\n")
        }
        //builder.append("]")
        return "$builder"
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + matrixSkeleton.hashCode()
        return result
    }
}