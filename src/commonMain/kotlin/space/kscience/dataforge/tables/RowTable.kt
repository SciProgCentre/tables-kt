package space.kscience.dataforge.tables

import kotlinx.coroutines.flow.toList
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.getValue
import kotlin.jvm.JvmInline
import kotlin.reflect.KType

@JvmInline
public value class MapRow<C : Any>(public val values: Map<String, C?>) : Row<C> {
    override fun get(column: String): C? = values[column]
}

@JvmInline
public value class MetaRow(public val meta: Meta) : Row<Value> {
    override fun get(column: String): Value? = meta.getValue(column)
}

internal class RowTableColumn<T : Any, R : T>(val table: Table<T>, val header: ColumnHeader<R>) : Column<R> {
    init {
        require(header in table.headers) { "Header $header does not belong to $table" }
    }

    override val name: String get() = header.name
    override val type: KType get() = header.type
    override val meta: Meta get() = header.meta
    override val size: Int get() = table.rows.size

    @Suppress("UNCHECKED_CAST")
    override fun get(index: Int): R? = table[index, name]?.let { it as R }
}

/**
 * A row-based table
 */
public open class RowTable<C : Any>(
    override val rows: List<Row<C>>,
    override val headers: List<ColumnHeader<C>>
) : Table<C> {
    override fun get(row: Int, column: String): C? = rows[row][column]

    override val columns: List<Column<C>> get() = headers.map { RowTableColumn(this, it) }
}

/**
 * Create Row table with given headers
 */
@Suppress("FunctionName")
public inline fun <T : Any> RowTable(vararg headers: ColumnHeader<T>, block: MutableTable<T>.() -> Unit): RowTable<T> =
    MutableTable<T>(arrayListOf(), headers.toMutableList()).apply(block)

public suspend fun <C : Any> Rows<C>.collect(): Table<C> = this as? Table<C> ?: RowTable(rowFlow().toList(), headers)