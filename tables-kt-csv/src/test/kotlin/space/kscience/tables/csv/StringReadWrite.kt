package space.kscience.tables.csv

import org.apache.commons.csv.CSVFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.string
import space.kscience.tables.RowTable
import space.kscience.tables.Table
import space.kscience.tables.get
import space.kscience.tables.valueRow

internal class StringReadWrite{
    val table = RowTable<Value> {
        val a by column<Value>()
        val b by column<Value>()
        valueRow(a to 1, b to "b1")
        valueRow(a to 2, b to "b2")
    }

    @Test
    fun writeRead(){
        val string = Table.writeStringAsCsv(table)
        println(string)
        val reconstructed = Table.readAsCsv(string)

        assertEquals("b2", reconstructed[1, "b"].string)
    }

    @Test
    fun writeReadTsv(){
        val format = CSVFormat.TDF
        val string = Table.writeStringAsCsv(table, format)
        println(string)
        val reconstructed = Table.readAsCsv(string,format)

        assertEquals("b2", reconstructed[1, "b"].string)
    }
}