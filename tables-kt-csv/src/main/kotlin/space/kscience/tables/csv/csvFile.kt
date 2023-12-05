package space.kscience.tables.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.ValueType
import space.kscience.tables.*
import java.nio.file.Path

public fun Table.Companion.readAsCsv(
    path: Path,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
    formatModifier: CSVFormat.Builder.() -> CSVFormat.Builder = {
        setHeader().setSkipHeaderRecord(true)
    },
): Table<Value> = CSVParser.parse(
    path,
    Charsets.UTF_8,
    csvFormat.builder().formatModifier().build()
).use { parser ->
    RowTable(
        headers = parser.headerNames.map { ColumnHeader(it, ValueType.STRING) },
        rows = parser.records.map { CsvRow(it) }
    )
}

public inline fun Table.Companion.readAsCsvRows(
    path: Path,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
    formatModifier: CSVFormat.Builder.() -> CSVFormat.Builder = {
        setHeader().setSkipHeaderRecord(true)
    },
    block: Rows<Value>.() -> Unit,
): Unit = CSVParser.parse(
    path,
    Charsets.UTF_8,
    csvFormat.builder().formatModifier().build()
).use { parser ->
    CsvRows(parser, parser.headerNames).block()
}

public fun Table.Companion.writeFileAsCsv(
    path: Path,
    table: Table<Value>,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
) {
    csvFormat
        .builder().setHeader(*table.headers.map { it.name }.toTypedArray()).build()
        .print(path, Charsets.UTF_8)
        .use { printer ->
            table.rows.forEach { row ->
                printer.printRecord(table.headers.map { row[it].toString() })
            }
        }
}