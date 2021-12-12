//package space.kscience.dataforge.dataframe
//
//import org.jetbrains.kotlinx.dataframe.*
//import org.jetbrains.kotlinx.dataframe.aggregation.AggregateGroupedBody
//import org.jetbrains.kotlinx.dataframe.api.cast
//import org.jetbrains.kotlinx.dataframe.api.count
//import org.jetbrains.kotlinx.dataframe.columns.ColumnKind
//import org.jetbrains.kotlinx.dataframe.columns.ColumnPath
//import org.jetbrains.kotlinx.dataframe.columns.ColumnReference
//import org.jetbrains.kotlinx.dataframe.impl.aggregation.GroupByReceiverImpl
//import space.kscience.dataforge.meta.Meta
//import space.kscience.dataforge.tables.Table
//import space.kscience.dataforge.tables.get
//import space.kscience.dataforge.tables.indices
//import kotlin.reflect.KType
//import space.kscience.dataforge.tables.Column as TableColumn
//
//private class ColumnAsDataColumn<T>(
//    val column: TableColumn<T>,
//    val indexList: List<Int> = column.indices.toList(),
//    val nameOverride: String = column.name,
//) : DataColumn<T> {
//
//    override fun get(indices: Iterable<Int>): DataColumn<T> {
//        val newIndices = indices.map { indexList[it] }
//        return ColumnAsDataColumn<T>(column, newIndices, nameOverride)
//    }
//
//    override fun get(range: IntRange): DataColumn<T> {
//        val newIndices = indices.map { indexList[it] }
//        return ColumnAsDataColumn<T>(column, newIndices, nameOverride)
//    }
//
//    override fun rename(newName: String): DataColumn<T> = ColumnAsDataColumn<T>(column, indexList, newName)
//
//    override fun distinct(): DataColumn<T> {
//        val newIndices = indexList.distinctBy { column.getOrNull(it) }
//        return ColumnAsDataColumn<T>(column, newIndices, nameOverride)
//    }
//
//    override fun countDistinct(): Int = distinct().count()
//
//    override fun defaultValue(): T? = null
//
//    override fun get(index: Int): T = column[indexList[index]]
//
//    override fun get(columnName: String): AnyCol =
//        if (columnName == nameOverride) this else error("Sub-columns are not allowed")
//
//    override fun kind(): ColumnKind = ColumnKind.Value
//
//    override fun size(): Int = indexList.size
//
//    override fun toSet(): Set<T> = indexList.map { column[it] }.toSet()
//
//    override fun type(): KType = column.type
//
//    override fun values(): Iterable<T> = indexList.asSequence().map { column[it] }.asIterable()
//
//    override fun name(): String = nameOverride
//
//}
//
//private class RowAsDataRow<T>(val frame: TableAsDataFrame<T>, val index: Int) : DataRow<T> {
//    override fun df(): DataFrame<T> = frame
//
//    override fun get(columnIndex: Int): Any? = frame.table.rows[index][frame.columnNames()[columnIndex]]
//
//    override fun get(name: String): Any? = frame.table.rows[index][name]
//
//    override fun <R> get(column: ColumnReference<R>): R {
//        TODO("Not yet implemented")
//    }
//
//    override fun index(): Int = index
//
//    override fun tryGet(name: String): Any? = frame.table.rows[index][name]
//
//    override fun values(): List<Any?> = frame.table.rows[index].let { row ->
//        frame.columnNames().map { row.getOrNull(it) }
//    }
//
//}
//
//
//internal class TableAsDataFrame<T>(val table: Table<T>) : DataFrame<T> {
//    private val columnList = table.columns.toList()
//
//    override fun containsColumn(name: String): Boolean = columnList.any { it.name == name }
//
//    override fun getColumnOrNull(index: Int): AnyCol? = if (index !in columnList.indices) {
//        null
//    } else {
//        ColumnAsDataColumn(columnList[index])
//    }
//
//    override fun getColumnOrNull(name: String): AnyCol? = table.columns[name]?.let { ColumnAsDataColumn(it) }
//
//    override fun <R> getColumnOrNull(column: ColumnSelector<T, R>): DataColumn<R>? {
//        TODO("Not supported")
//    }
//
//    override fun getColumnOrNull(path: ColumnPath): AnyCol? {
//        if (path.size != 1) TODO("Hierarchical columns are not supported")
//        return getColumnOrNull(path.name())
//    }
//
//    override fun <R> getColumnOrNull(column: ColumnReference<R>): DataColumn<R>? {
//        return table.columns[column.name()]?.let { ColumnAsDataColumn(it) }
//    }
//
//    override fun <R> aggregate(body: AggregateGroupedBody<T, R>): DataRow<T> {
//        TODO("Not supported")
//    }
//
//    override fun columnNames(): List<String> = table.columns.map { it.name }
//
//    override fun columnTypes(): List<KType> = table.columns.map { it.type }
//
//    override fun columns(): List<AnyCol> = columnList.map { ColumnAsDataColumn(it) }
//
//    override fun columnsCount(): Int = columnList.size
//
//    override fun getColumnIndex(name: String): Int {
//        return columnList.indexOfFirst { it.name == name }
//    }
//
//    override fun rows(): List<DataRow<T>> = table.rows.indices.map { RowAsDataRow(this, it) }
//
//    override fun rowsCount(): Int = table.rows.size
//
//    override fun rowsReversed(): List<DataRow<T>> = rows().reversed()
//
//    override fun <C> values(byRow: Boolean, columns: ColumnsSelector<T, C>): Sequence<C> {
//        //implementation from [DataFrameImpl]
//        val cols = get(columns)
//        return if (byRow) sequence {
//            indices().forEach { row ->
//                cols.forEach {
//                    yield(it[row])
//                }
//            }
//        }
//        else sequence {
//            cols.forEach { col ->
//                col.values().forEach {
//                    yield(it)
//                }
//            }
//        }
//    }
//
//}