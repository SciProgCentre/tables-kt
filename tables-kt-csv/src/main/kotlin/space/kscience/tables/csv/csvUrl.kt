package space.kscience.tables.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.ValueType
import space.kscience.tables.ColumnHeader
import space.kscience.tables.RowTable
import space.kscience.tables.Rows
import space.kscience.tables.Table
import java.net.URL

public fun Table.Companion.readAsCsv(
    url: URL,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
    formatModifier: CSVFormat.Builder.() -> CSVFormat.Builder = {
        setHeader().setSkipHeaderRecord(true)
    },
): Table<Value> = CSVParser.parse(
    url,
    Charsets.UTF_8,
    csvFormat.builder().formatModifier().build()
).use { parser ->
    RowTable(
        headers = parser.headerNames.map { ColumnHeader(it, ValueType.STRING) },
        rows = parser.records.map { CsvRow(it) }
    )
}

public inline fun Table.Companion.readAsCsvRows(
    url: URL,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
    formatModifier: CSVFormat.Builder.() -> CSVFormat.Builder = {
        setHeader().setSkipHeaderRecord(true)
    },
    block: Rows<Value>.() -> Unit,
): Unit = CSVParser.parse(
    url,
    Charsets.UTF_8,
    csvFormat.builder().formatModifier().build()
).use { parser ->
    CsvRows(parser, parser.headerNames).block()
}