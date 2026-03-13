package obj.nested.enum.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Foo(val value: Sort) {
    @Serializable
    enum class Sort {
        ASC, DESC;
    }
}
