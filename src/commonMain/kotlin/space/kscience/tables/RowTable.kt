package space.kscience.tables

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MetaRepr
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.getValue
import kotlin.jvm.JvmInline
import kotlin.reflect.KType

@JvmInline
public value class MapRow<C>(public val values: Map<String, C?>) : Row<C> {
    override fun getOrNull(column: String): C? = values[column]
}

/**
 * Create a [MapRow] using pairs of [ColumnHeader] and values
 */
@Suppress("FunctionName")
public fun <T> Row(vararg pairs: Pair<ColumnHeader<T>, T>): MapRow<T> =
    MapRow(pairs.associate { it.first.name to it.second })

@JvmInline
public value class MetaRow(public val meta: Meta) : Row<Value> {
    override fun getOrNull(column: String): Value? = meta.getValue(column)
}

/**
 * Represent [MetaRepr] as a [Row]
 */
public fun MetaRepr.asRow(): MetaRow = MetaRow(toMeta())

internal class RowTableColumn<T, R : T>(val table: Table<T>, val header: ColumnHeader<R>) : Column<R> {
    init {
        require(header in table.headers) { "Header $header does not belong to $table" }
    }

    override val name: String get() = header.name
    override val type: KType get() = header.type
    override val meta: Meta get() = header.meta
    override val size: Int get() = table.rows.size

    @Suppress("UNCHECKED_CAST")
    override fun getOrNull(index: Int): R? = table.getOrNull(index, name)?.let { it as R }
}

/**
 * A row-based table
 */
public open class RowTable<C>(
    override val rows: List<Row<C>>,
    override val headers: List<ColumnHeader<C>>,
) : Table<C> {
    override fun getOrNull(row: Int, column: String): C? = rows[row].getOrNull(column)

    override val columns: List<Column<C>> get() = headers.map { RowTableColumn(this, it) }
}

/**
 * Create Row table with given headers
 */
@Suppress("FunctionName")
public inline fun <T> RowTable(vararg headers: ColumnHeader<T>, block: MutableRowTable<T>.() -> Unit): RowTable<T> =
    MutableRowTable<T>(arrayListOf(), headers.toMutableList()).apply(block)

public suspend fun <C> Rows<C>.collect(): Table<C> = this as? Table<C> ?: RowTable(rowSequence().toList(), headers)