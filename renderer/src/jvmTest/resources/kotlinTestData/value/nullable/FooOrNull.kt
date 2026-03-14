package obj.value.cls.nullable.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class FooOrNull(val value: String?)
