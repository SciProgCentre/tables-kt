package space.kscience.tables.io

import kotlinx.coroutines.runBlocking
import space.kscience.dataforge.io.toByteArray
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.int
import space.kscience.dataforge.values.string
import space.kscience.tables.RowTable
import space.kscience.tables.valueRow
import kotlin.test.Test
import kotlin.test.assertEquals


@DFExperimental
class TextRowsTest {
    val table = RowTable<Value> {
        val a by column<Value>()
        val b by column<Value>()
        valueRow(a to 1, b to "b1")
        valueRow(a to 2, b to "b2")
    }

    @Test
    fun testTableWriteRead() = runBlocking {
        val envelope = table.toTextEnvelope()
        val string = envelope.data!!.toByteArray().decodeToString()
        println(string)
        val table = envelope.readTextRows()
        val rows = table.rowSequence().toList()
        assertEquals(1, rows[0].getOrNull("a")?.int)
        assertEquals("b2", rows[1].getOrNull("b")?.string)
    }
}