package space.kscience.dataforge.dataframe

import org.jetbrains.kotlinx.dataframe.api.add
import org.junit.jupiter.api.Test
import space.kscience.tables.ColumnHeader
import space.kscience.tables.ColumnTable
import space.kscience.tables.get
import kotlin.math.pow
import kotlin.test.assertEquals

internal class DataFrameTableTest {
    @Test
    fun convertTableToDataFrame() {
        val x by ColumnHeader.typed<Double?>()
        val y by ColumnHeader.typed<Double?>()

        val table = ColumnTable<Double?>(100U) {
            x.fill { it.toDouble() }
            y.values = x.values.map { it?.pow(2) }
        }
        val dataFrame = table.toDataFrame()

        //println( dataFrame)

        val newFrame = dataFrame.add {
            "z".from { "x".invoke<Double>() + "y".invoke<Double>() + 1.0 }
        }


        //println(newFrame)

        val newTable = newFrame.asTable()

        assertEquals(newTable.columns[x], table.columns[x])
    }
}