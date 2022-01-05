package space.kscience.tables.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.ValueType
import space.kscience.tables.ColumnHeader
import space.kscience.tables.RowTable
import space.kscience.tables.Table
import space.kscience.tables.get

public fun Table.Companion.readAsCsv(
    string: String,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
    formatModifier: CSVFormat.Builder.() -> CSVFormat.Builder = {
        setHeader().setSkipHeaderRecord(true)
    },
): Table<Value> = CSVParser.parse(
    string,
    csvFormat.builder().formatModifier().build()
).use { parser ->
    RowTable(
        headers = parser.headerNames.map { ColumnHeader(it, ValueType.STRING) },
        rows = parser.records.map { CsvRow(it) }
    )
}

public fun Table.Companion.writeStringAsCsv(
    table: Table<Value>,
    csvFormat: CSVFormat = CSVFormat.DEFAULT,
): String {
    return buildString {
        csvFormat
            .builder().setHeader(*table.headers.map { it.name }.toTypedArray()).build()
            .print(this)
            .use { printer ->
                table.rows.forEach { row ->
                    printer.printRecord(table.headers.map { row[it].toString() })
                }
            }
    }
}
