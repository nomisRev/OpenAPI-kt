package obj.nested.enum.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class Foo(val id: Long, val sort: Sort) {
    @Serializable
    enum class Sort {
        ASC, DESC;
    }
}
