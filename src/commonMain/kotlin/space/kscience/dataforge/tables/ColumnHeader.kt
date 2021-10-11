package space.kscience.dataforge.tables

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.get
import space.kscience.dataforge.meta.string
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.ValueType
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
        public inline fun <reified T> forType(
            name: String,
            builder: ColumnScheme.() -> Unit = {}
        ): ColumnHeader<T> = SimpleColumnHeader(
            name, typeOf<T>(), ColumnScheme(builder).meta
        )

        /**
         * A delegated builder for typed column header
         */
        public inline fun <reified T> typed(
            crossinline builder: ColumnScheme.() -> Unit
        ): ReadOnlyProperty<Any?, ColumnHeader<T>> = ReadOnlyProperty { _, property ->
            forType(property.name, builder)
        }

        public fun forValue(
            name: String,
            builder: ValueColumnScheme.() -> Unit = {}
        ): ColumnHeader<Value> = SimpleColumnHeader(
            name, typeOf<Value>(), ValueColumnScheme(builder).meta
        )

        public fun value(
            builder: ValueColumnScheme.() -> Unit = {}
        ): ReadOnlyProperty<Any?, ColumnHeader<Value>> = ReadOnlyProperty { _, property ->
            forValue(property.name, builder)
        }
    }
}

public data class SimpleColumnHeader<T>(
    override val name: String,
    override val type: KType,
    override val meta: Meta
) : ColumnHeader<T>


public val ColumnHeader<Value>.valueType: ValueType? get() = meta["valueType"].string?.let { ValueType.valueOf(it) }
