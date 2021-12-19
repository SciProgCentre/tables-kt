package space.kscience.tables.csv

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.apache.commons.csv.CSVRecord
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.ValueType
import space.kscience.dataforge.values.lazyParseValue
import space.kscience.tables.ColumnHeader
import space.kscience.tables.Row
import space.kscience.tables.Rows
import space.kscience.tables.TableHeader

internal class CsvRow(val record: CSVRecord) : Row<Value> {
    override fun getOrNull(column: String): Value? = record.get(column)?.lazyParseValue()
}


public class CsvRows(private val rowsProvider: Iterable<CSVRecord>, private val names: List<String>) : Rows<Value> {
    override val headers: TableHeader<Value> get() = names.map { ColumnHeader.forValue(it, ValueType.STRING) }

    override fun rowFlow(): Flow<Row<Value>> = rowsProvider.asFlow().map(::CsvRow)
}