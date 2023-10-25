package space.kscience.tables

import kotlin.test.Test
import kotlin.test.assertTrue

class ColumnTableTest {
    @Test
    fun columnBuilder() {
        val columnTable = ColumnTable<Double>(100) {
            val a by ColumnHeader.typed<Double>()
            val b by ColumnHeader.typed<Double>()

            a.fill { it.toDouble() }
            columns[b] = List(100) { it.toDouble() }
            column("c") { it[a] - it[b] }
        }
        assertTrue {
            columnTable.columns["c"].listValues().all { it == 0.0 }
        }
    }
}