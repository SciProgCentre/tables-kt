package space.kscience.tables.io

import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import space.kscience.dataforge.io.Binary
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.lazyParseValue
import space.kscience.tables.MapRow
import space.kscience.tables.Row
import space.kscience.tables.Rows
import space.kscience.tables.ValueTableHeader

/**
 * Read a lin as a fixed width [Row]
 */
internal fun String.readRow(header: ValueTableHeader): Row<Value> {
    val values = trim().split("\\s+".toRegex()).map { it.lazyParseValue() }

    if (values.size == header.size) {
        val map = header.map { it.name }.zip(values).toMap()
        return MapRow(map)
    } else {
        error("Can't read line \"${this}\". Expected ${header.size} values in a line, but found ${values.size}")
    }
}

/**
 * Finite or infinite [Rows] created from a fixed width text binary
 */
internal class TextRows(override val headers: ValueTableHeader, private val binary: Binary) : Rows<Value> {

    override fun rowFlow(): Flow<Row<Value>> = binary.read {
        val text = readBytes().decodeToString()
        text.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.readRow(headers) }.asFlow()
//        flow {
//            do {
//                val line = readUTF8Line()
//                if (!line.isNullOrBlank()) {
//                    val row = readRow(headers, line)
//                    emit(row)
//                }
//            } while (!endOfInput)
//        }
    }

}