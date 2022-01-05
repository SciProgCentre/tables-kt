package space.kscience.tables

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.misc.DFExperimental
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.typeOf

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
     * Add or insert at [index] a fixed column
     */
    public fun addColumn(column: Column<T>, index: Int? = null) {
        require(column.size == this.rowsSize.toInt()) { "Required column size $rowsSize, but found ${column.size}" }
        require(_columns.find { it.name == column.name } == null) { "Column with name ${column.name} already exists" }
        if (index == null) {
            _columns.add(column)
        } else {
            _columns.add(index, column)
        }
    }

    public fun removeColumn(name: String) {
        _columns.removeAll { it.name == name }
    }

    /**
     * Add or replace existing column with given header. Column is always added to the end of the table
     */
    @DFExperimental
    public operator fun Collection<Column<T>>.set(header: ColumnHeader<T>, data: Iterable<T>) {
        removeColumn(header.name)
        val column = ListColumn(header.name, data.toList(), header.type, header.meta)
        addColumn(column)
    }

    public fun <R : T> ColumnHeader<R>.fill(index: Int? = null, dataBuilder: (Int) -> R?): Column<R> {
        //TODO use specialized columns if possible
        val column = ListColumn(this, rowsSize.toInt(), dataBuilder)
        addColumn(column, index)
        return column
    }

    public var <R : T> ColumnHeader<R>.values: Collection<R?>
        get() = columns[this].listValues()
        set(value) {
            val newColumn = ListColumn(this, value.toList())
            removeColumn(name)
            addColumn(newColumn)
        }
}

public fun <T, R : T> MutableColumnTable<T>.newColumn(
    nameOverride: String? = null,
    index: Int? = null,
    builder: (String) -> Column<R>,
): ReadOnlyProperty<MutableColumnTable<T>, Column<R>> = ReadOnlyProperty { _, property ->
    val name = nameOverride ?: property.name
    val column = builder(name)
    addColumn(column, index)
    column
}

public inline fun <T, reified R : T> MutableColumnTable<T>.column(
    meta: Meta = Meta.EMPTY,
    nameOverride: String? = null,
    cache: Boolean = false,
    index: Int? = null,
    noinline mapper: (Row<T>) -> R?,
): ReadOnlyProperty<MutableColumnTable<T>, Column<R>> = newColumn(nameOverride, index) {
    rowsToColumn(it, meta, cache, mapper)
}

public inline fun <T, reified R : T> MutableColumnTable<T>.column(
    values: Iterable<R>,
    nameOverride: String? = null,
    meta: Meta = Meta.EMPTY,
    index: Int? = null,
): ReadOnlyProperty<MutableColumnTable<T>, Column<R>> = newColumn(nameOverride, index) { name ->
    ListColumn(name, values.toList(), typeOf<R>(), meta)
}

public inline fun <T, reified R : T> MutableColumnTable<T>.fillColumn(
    nameOverride: String? = null,
    meta: Meta = Meta.EMPTY,
    index: Int? = null,
    crossinline producer: (Int) -> R?,
): ReadOnlyProperty<MutableColumnTable<T>, Column<R>> = newColumn(nameOverride, index) { name ->
    ListColumn(name, List(rowsSize.toInt(), producer), typeOf<R>(), meta)
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