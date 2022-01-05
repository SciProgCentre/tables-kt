package space.kscience.tables.io

import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import space.kscience.dataforge.io.Binary
import space.kscience.dataforge.io.readSafeUtf8Line
import space.kscience.dataforge.values.Value
import space.kscience.tables.*

/**
 * Finite table created from [Binary] with fixed width text table
 */
internal class TextTable(
    override val headers: ValueTableHeader,
    private val binary: Binary,
    val index: List<Int>,
) : Table<Value> {

    override val columns: Collection<Column<Value>> get() = headers.map { RowTableColumn(this, it) }

    override val rows: List<Row<Value>> get() = index.map { readAt(it) }

    override fun rowSequence(): Sequence<Row<Value>> = TextRows(headers, binary).rowSequence()

    private fun readAt(offset: Int): Row<Value> {
        return binary.read(offset) {
            val line = readSafeUtf8Line()
            return@read line.readRow(headers)
        }
    }

    override fun getOrNull(row: Int, column: String): Value? {
        val offset = index[row]
        return readAt(offset).getOrNull(column)
    }
}


/**
 * A flow of indexes of string start offsets ignoring empty strings
 */
private fun Binary.lineIndexFlow(): Flow<Int> = read {
    //TODO replace by line reader
    val text = readBytes().decodeToString()
    text.lineSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .scan(0) { acc, str -> acc + str.length }.asFlow()
//        var counter: Int = 0
//        flow {
//            do {
//                val line = readUTF8Line()
//                counter += line?.length ?: 0
//                if (!line.isNullOrBlank()) {
//                    emit(counter)
//                }
//            } while (!endOfInput)
//        }
}


/**
 * Create a row offset index for [TextRows]
 */
private suspend fun Binary.buildRowIndex(): List<Int> = lineIndexFlow().toList()


/**
 * Read given binary as TSV [Value] table.
 * This method does not read the whole table into memory. Instead, it reads it ones and saves line offset index. Then
 * it reads specific lines on-demand.
 */
public suspend fun Binary.readTextTable(header: ValueTableHeader): Table<Value>{
    val index = buildRowIndex()
    return TextTable(header, this, index)
}
