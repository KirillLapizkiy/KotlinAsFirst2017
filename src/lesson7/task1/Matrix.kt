@file:Suppress("UNUSED_PARAMETER", "unused")
package lesson7.task1

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E
    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)
    operator fun set(cell: Cell, value: E)
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width, e)

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {


    private val cellMap = mutableMapOf<Cell, E>()
    init {
        if (height <= 0 || width <= 0)
            throw  IllegalArgumentException()
        for (i in 0 until height) {
            for (j in 0 until width) {
                this[i, j] = e
            }
        }
    }

    override fun get(cell: Cell): E = cellMap[cell] ?: throw IllegalArgumentException("")

    override fun get(row: Int, column: Int): E = get(Cell(row, column))

    override fun set(cell: Cell, value: E) {cellMap.put(cell, value)}

    override fun set(row: Int, column: Int, value: E) = set(Cell(row, column), value)

    override fun equals(other: Any?) =
            other is MatrixImpl<*> &&
                    height == other.height &&
                    width == other.width // && elements comparison

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + cellMap.hashCode()
        return result
    }
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        for (row in 0..height - 1) {
            sb.append("[")
            val tempList = mutableListOf<Cell>()
            for (column in 0..width - 1) {
                tempList.add(Cell(row, column))
                sb.append(tempList.joinToString(separator = ", ", postfix = ""))
            }
            sb.append("]")
        }
        sb.append("]")
        return "$sb"
    }
}

