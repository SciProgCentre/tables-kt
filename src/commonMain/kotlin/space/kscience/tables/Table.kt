package space.kscience.tables

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Finite or infinite row set. Rows are produced in a lazy suspendable [Flow].
 * Each row must contain at least all the fields mentioned in [headers].
 */
public interface Rows<out T> {
    public val headers: TableHeader<T>
    public fun rowFlow(): Flow<Row<T>>
}

public interface Table<out T> : Rows<T> {
    public fun getOrNull(row: Int, column: String): T?
    public val columns: Collection<Column<T>>
    override val headers: TableHeader<T> get() = columns.toList()
    public val rows: List<Row<T>>
    override fun rowFlow(): Flow<Row<T>> = rows.asFlow()

    /**
     * Apply typed query to this table and return lazy [Flow] of resulting rows. The flow could be empty.
     */
    //fun select(query: Any): Flow<Row> = error("Query of type ${query::class} is not supported by this table")
    public companion object {}
}

public operator fun <T> Table<T>.get(row: Int, column: String): T =
    getOrNull(row, column) ?: error("Element with column $column and row $row not found in $this")

public operator fun <T> Collection<Column<T>>.get(name: String): Column<T>? = find { it.name == name }

public inline operator fun <T, reified R : T> Table<T>.get(row: Int, column: ColumnHeader<R>): R? {
    require(headers.contains(column)) { "Column $column is not in table headers" }
    return getOrNull(row, column.name) as? R
}

public interface Column<out T> : ColumnHeader<T> {
    public val size: Int

    public fun getOrNull(index: Int): T?
}

public operator fun <T> Column<T>.get(index: Int): T =
    getOrNull(index) ?: error("Element with index $index not found in $this")

public val Column<*>.indices: IntRange get() = (0 until size)

public operator fun <T> Column<T>.iterator(): Iterator<T?> = iterator {
    for (i in indices) {
        yield(getOrNull(i))
    }
}

public fun <T> Column<T>.sequence(): Sequence<T?> = sequence {
    for (i in indices) {
        yield(getOrNull(i))
    }
}


public interface Row<out T> {
    public fun getOrNull(column: String): T?
}

public operator fun <T> Row<T>.get(column: String): T =
    getOrNull(column) ?: error("Element with column name $column not found in $this")

public inline operator fun <T, reified R : T> Row<T>.get(column: ColumnHeader<R>): R? = get(column.name) as? R