package space.kscience.tables

/**
 * @param T boundary type for all columns in the table
 */
public open class ColumnTable<T>(final override val columns: Collection<Column<T>>) : Table<T> {
    private val rowsNum get() = columns.first().size

    init {
        require(columns.all { it.size == rowsNum }) { "All columns must be of the same size" }
    }

    override val rows: List<Row<T>>
        get() = (0 until rowsNum).map { VirtualRow(this, it) }

    override fun getOrNull(row: Int, column: String): T? = columns[column]?.getOrNull(row)
}

public val <T> Table<T>.rowsSize: UInt get() = columns.firstOrNull()?.size?.toUInt() ?: 0U

internal class VirtualRow<T>(val table: Table<T>, val index: Int) : Row<T> {
    override fun getOrNull(column: String): T? = table.getOrNull(index, column)
}

