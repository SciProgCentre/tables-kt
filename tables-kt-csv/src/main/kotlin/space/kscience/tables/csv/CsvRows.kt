package space.kscience.tables.csv

import org.apache.commons.csv.CSVRecord
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.ValueType
import space.kscience.dataforge.meta.lazyParseValue
import space.kscience.tables.ColumnHeader
import space.kscience.tables.Row
import space.kscience.tables.Rows
import space.kscience.tables.TableHeader

internal class CsvRow(val record: CSVRecord) : Row<Value> {
    override fun getOrNull(column: String): Value? = record.get(column)?.lazyParseValue()
}


public class CsvRows(private val rowsProvider: Iterable<CSVRecord>, private val names: List<String>) : Rows<Value> {
    override val headers: TableHeader<Value> get() = names.map { ColumnHeader(it, ValueType.STRING) }

    override fun rowSequence(): Sequence<Row<Value>> = rowsProvider.asSequence().map(::CsvRow)
}
