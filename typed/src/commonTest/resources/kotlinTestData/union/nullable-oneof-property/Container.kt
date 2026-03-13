package union.nullable.oneof.property.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Container(val value: String?)
