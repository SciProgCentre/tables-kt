package space.kscience.tables

/**
 * Mutable table with a fixed size, but dynamic columns
 */
public class MutableColumnTable<T>(
    public val rowsSize: UInt,
    private val _columns: MutableList<Column<T>> = ArrayList(),
) : ColumnTable<T>(_columns) {

    override val rows: List<Row<T>>
        get() = (0U until rowsSize).map {
            VirtualRow(this, it.toInt())
        }

    override fun getOrNull(row: Int, column: String): T? = columns[column]?.getOrNull(row)

    /**
     * Add a fixed column to the end of the table
     */
    public fun addColumn(column: Column<T>) {
        require(column.size == this.rowsSize.toInt()) { "Required column size $rowsSize, but found ${column.size}" }
        _columns.add(column)
    }

    /**
     * Insert a column at [index]
     */
    public fun insertColumn(index: Int, column: Column<T>) {
        require(column.size == this.rowsSize.toInt()) { "Required column size $rowsSize, but found ${column.size}" }
        _columns.add(index, column)
    }
}

/**
 * Shallow copy table to a new [MutableColumnTable]
 */
public fun <T> ColumnTable<T>.toMutableColumnTable(): MutableColumnTable<T> =
    MutableColumnTable<T>(rowsSize, columns.toMutableList())


/**
 * Shallow copy and edit [Table] and edit it as [ColumnTable]
 */
public fun <T> Table<T>.editColumns(block: MutableColumnTable<T>.() -> Unit): ColumnTable<T> =
    MutableColumnTable<T>(rowsSize, columns.toMutableList()).apply(block)