package space.kscience.tables

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.values.Value
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public class MutableRowTable<C>(
    override val rows: MutableList<Row<C>>,
    override val headers: MutableList<ColumnHeader<C>>,
) : RowTable<C>(rows, headers) {

    public fun <T : C> addColumn(header: ColumnHeader<C>) {
        headers.add(header)
    }

    /**
     * Create a new column header for the table
     */
    @PublishedApi
    internal fun <T : C> newColumn(name: String, type: KType, meta: Meta, index: Int?): ColumnHeader<T> {
        val header = SimpleColumnHeader<T>(name, type, meta)
        if (index == null) {
            headers.add(header)
        } else {
            headers.add(index, header)
        }
        return header
    }

    public inline fun <reified T : C> newColumn(
        name: String,
        index: Int? = null,
        noinline columnMetaBuilder: ColumnScheme.() -> Unit = {},
    ): ColumnHeader<T> = newColumn(name, typeOf<T>(), ColumnScheme(columnMetaBuilder).toMeta(), index)

    public inline fun <reified T : C> column(
        index: Int? = null,
        noinline columnMetaBuilder: ColumnScheme.() -> Unit = {},
    ): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, ColumnHeader<T>>> =
        PropertyDelegateProvider { _, property ->
            val res = newColumn<T>(property.name, index, columnMetaBuilder)
            ReadOnlyProperty { _, _ -> res }
        }

    public fun addRow(row: Row<C>): Row<C> {
        rows.add(row)
        return row
    }

    public fun row(map: Map<String, C?>): Row<C> {
        val row = MapRow(map)
        rows.add(row)
        return row
    }

    public fun <T : C> row(vararg pairs: Pair<ColumnHeader<T>, T>): Row<C> = addRow(Row(*pairs))
}

public fun MutableRowTable<Value>.valueRow(vararg pairs: Pair<ColumnHeader<Value>, Any?>): Row<Value> =
    row(pairs.associate { it.first.name to Value.of(it.second) })

/**
 * Add a row represented by Meta
 */
public fun MutableRowTable<Value>.row(meta: Meta): Row<Value> {
    val row = MetaRow(meta)
    rows.add(row)
    return row
}

/**
 * Shallow copy table to a new [MutableRowTable]
 */
public fun <T> RowTable<T>.toMutableRowTable(): MutableRowTable<T> =
    MutableRowTable(rows.toMutableList(), headers.toMutableList())

/**
 * Shallow copy and edit [Table] and edit it as [RowTable]
 */
public fun <T> Table<T>.editRows(block: MutableRowTable<T>.() -> Unit): RowTable<T> =
    MutableRowTable(rows.toMutableList(), headers.toMutableList()).apply(block)