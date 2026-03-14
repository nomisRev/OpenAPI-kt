package collection.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Foo(val items: List<Item>) {
    @Serializable
    data class Item(val id: String, val name: String)
}
