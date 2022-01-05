package space.kscience.tables

/**
 * @param T boundary type for all columns in the table
 */
public open class ColumnTable<out T>(final override val columns: Collection<Column<T>>) : Table<T> {
    private val rowsNum get() = columns.first().size

    init {
        require(columns.all { it.size == rowsNum }) { "All columns must be of the same size" }
    }

    override val rows: List<Row<T>>
        get() = (0 until rowsNum).map { VirtualRow(this, it) }

    override fun getOrNull(row: Int, column: String): T? = columns[column]?.getOrNull(row)
}

@Suppress("FunctionName")
public fun <T> ColumnTable(vararg columns: Column<T>): ColumnTable<T> = ColumnTable(columns.toList())

@Suppress("FunctionName")
public inline fun <T> ColumnTable(size: UInt, builder: MutableColumnTable<T>.() -> Unit): ColumnTable<T> =
    MutableColumnTable<T>(size).apply(builder)

public val <T> Table<T>.rowsSize: UInt get() = columns.firstOrNull()?.size?.toUInt() ?: 0U

public operator fun <T, R : T> Collection<Column<T>>.get(header: ColumnHeader<R>): Column<R> {
    val res = find { it.name == header.name } ?: error("Column with name ${header.name} not present")
    require( header.type == res.type) { "Column type mismatch. Expected ${header.type}, but found ${res.type}" }
    @Suppress("UNCHECKED_CAST") return res as Column<R>
}

internal class VirtualRow<T>(val table: Table<T>, val index: Int) : Row<T> {
    override fun getOrNull(column: String): T? = table.getOrNull(index, column)
}

