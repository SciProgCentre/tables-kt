package space.kscience.tables

import space.kscience.dataforge.meta.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public typealias TableHeader<C> = List<ColumnHeader<C>>

public typealias ValueTableHeader = List<ColumnHeader<Value>>

public interface ColumnHeader<out T> {
    public val name: String
    public val type: KType
    public val meta: Meta

    public companion object {
        /**
         * A delegated builder for typed column header
         */
        public inline fun <reified T> typed(
            crossinline builder: ColumnScheme.() -> Unit = {},
        ): ReadOnlyProperty<Any?, ColumnHeader<T>> = ReadOnlyProperty { _, property ->
            ColumnHeader(property.name, builder)
        }

        public fun value(
            valueType: ValueType = ValueType.STRING,
            builder: ValueColumnScheme.() -> Unit = {},
        ): ReadOnlyProperty<Any?, ColumnHeader<Value>> = ReadOnlyProperty { _, property ->
            ColumnHeader(property.name, valueType, builder)
        }
    }
}

@Suppress("FunctionName")
public inline fun <reified T> ColumnHeader(
    name: String,
    builder: ColumnScheme.() -> Unit = {},
): ColumnHeader<T> = SimpleColumnHeader(name, typeOf<T>(), ColumnScheme(builder).meta)

@Suppress("FunctionName")
public fun ColumnHeader(
    name: String,
    valueType: ValueType,
    builder: ValueColumnScheme.() -> Unit = {},
): ColumnHeader<Value> = SimpleColumnHeader(
    name, typeOf<Value>(), ValueColumnScheme {
        this.valueType = valueType
        builder()
    }.meta
)

public data class SimpleColumnHeader<T>(
    override val name: String,
    override val type: KType,
    override val meta: Meta,
) : ColumnHeader<T>


public val ColumnHeader<Value>.valueType: ValueType?
    get() = meta[ValueColumnScheme::valueType.name].enum<ValueType>()
