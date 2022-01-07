package space.kscience.tables

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.meta.enum
import space.kscience.dataforge.meta.string
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.ValueType

public open class ColumnScheme : Scheme() {
    public var title: String? by string()

    public companion object : SchemeSpec<ColumnScheme>(::ColumnScheme)
}

public val ColumnHeader<*>.properties: ColumnScheme get() =  ColumnScheme.read(meta)

public class ValueColumnScheme : ColumnScheme() {
    public var valueType: ValueType by enum(ValueType.STRING)

    public companion object : SchemeSpec<ValueColumnScheme>(::ValueColumnScheme)
}

public val ColumnHeader<Value>.properties: ValueColumnScheme get() =  ValueColumnScheme.read(meta)